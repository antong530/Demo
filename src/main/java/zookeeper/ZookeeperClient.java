package zookeeper;

import Demo.Executor.Command;
import org.apache.commons.lang.StringUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.NodeCache;
import org.apache.curator.framework.recipes.cache.NodeCacheListener;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.concurrent.CountDownLatch;

/**
 * Created by antong on 16/7/20.
 */
public class ZookeeperClient {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperClient.class);
    private static final String LOCK_EXCLUSIVE = "/exclusiveLock/ExclusiveLock";
    private static final String LOCK_SHARE = "/shareLock/ShareLock";
    private static final String LOCK = "/shareLock";
    private static final String WRITE = "_write$";
    private static final String READ = "_read$";
    private CuratorFramework client;
    private Command command;

    private ZookeeperClient(Command command) {
        this.command = command;
        this.client = CuratorFrameworkFactory.builder().connectString("localhost:2181,localhost:2182,localhost:2183")
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
    }


    public static ZookeeperClient getInstance(final Command command) {
        return new ZookeeperClient(command);
    }

    public boolean doExclusiveLock() {
        try {
            client.start();
            exclusiveLock();
        } catch (Exception e) {
            LOGGER.error("ERROR", e);
        } finally {
            return false;
        }
    }

    public boolean doShareLock() {
        try {
            client.start();
            shareLock();
        } catch (Exception e) {
            LOGGER.error("ERROR", e);
        } finally {
            return false;
        }
    }

    /**
     * 根据请求类型建立对应的锁
     * 如果是写锁，判断写锁是否为第一个，如果是执行任务关闭锁，如果不是建立上一个节点的监听器并阻塞，监听节点删除，解除阻塞，执行任务，删除锁
     * 如果是读锁，判断读锁是否为第一个，如果是执行任务关闭锁，如果不是建立上一个写节点的监听器并阻塞（如果以前节点没有写节点，则直接运行，删除锁），监听节点删除，解除阻塞，执行任务，删除锁
     */
    public void shareLock() {
        try {
            CountDownLatch countDownLatch = new CountDownLatch(1);
            List<String> childrenLocks = new ArrayList<String>();
            String lockName = "";
            if (command.getType().equals("write")) {
                lockName = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(LOCK_SHARE + WRITE);
                LOGGER.info("====> 写请求，写锁为 {}", lockName);
                childrenLocks = client.getChildren().forPath(LOCK);
                Collections.sort(childrenLocks, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                if (lockName.contains(childrenLocks.get(0))) {
                    LOGGER.info("=====> 前面没有其他锁，写请求正在执行任务 {}", lockName);
                    command.run();
                    client.delete().forPath(lockName);
                    LOGGER.info("=====> 任务执行完毕，释放 {}", lockName);
                } else {
                    LOGGER.info("=====> 在 {} 前面存在其他锁", lockName);
                    int i = childrenLocks.indexOf(lockName.substring(11));
                    addExclusiveListener(countDownLatch, LOCK + "/" + childrenLocks.get(i - 1));
                    countDownLatch.await();
                    command.run();
                    client.delete().forPath(lockName);
                }
            } else {
                lockName = client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath(LOCK_SHARE + READ);
                childrenLocks = client.getChildren().forPath(LOCK);
                Collections.sort(childrenLocks, new Comparator<String>() {
                    @Override
                    public int compare(String o1, String o2) {
                        return o1.compareTo(o2);
                    }
                });
                if (lockName.contains(childrenLocks.get(0))) {
                    command.run();
                    client.delete().forPath(lockName);
                } else {
                    int i = childrenLocks.indexOf(lockName.substring(11));
                    List<String> subChildren = childrenLocks.subList(0, i - 1);
                    String writeLockName = "";
                    for (String children : subChildren) {
                        if (children.contains(WRITE)) {
                            writeLockName = children;
                        }
                    }
                    if (StringUtils.isNotEmpty(writeLockName)) {
                        addExclusiveListener(countDownLatch, writeLockName);
                        countDownLatch.await();
                        command.run();
                        client.delete().forPath(lockName);
                    } else {
                        command.run();
                        client.delete().forPath(lockName);
                    }
                }
            }


        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    /**
     * 首先判断该锁是否被其他线程所持有
     * 如果持有，注册该节点删除的监听器，在注册监听器的方法后阻塞（等待释放阻塞信号）然后递归调用抢锁
     * 如果没有，抢锁，抢锁成功时正常启动任务，并释放锁
     * 当锁被释放后，监听器监听到释放锁事件，发送释放阻塞，让其递归调用注册监听器阻塞的递归调用抢锁
     *
     * @throws Exception
     */
    private void exclusiveLock() throws Exception {
        CountDownLatch latch = new CountDownLatch(1);
        if (client.checkExists().forPath(LOCK_EXCLUSIVE) == null) {
            LOGGER.info("====> LOCK_EXCLUSIVE没有被别的线程所占有，验证阶段");
            try {
                client.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath(LOCK_EXCLUSIVE);
                LOGGER.info("====> LOCK_EXCLUSIVE被自己占有，开始执行任务");
                command.run();
                client.delete().forPath(LOCK_EXCLUSIVE);
                LOGGER.info("====> LOCK_EXCLUSIVE被自己释放，任务执行完毕");
            } catch (Exception e) {
                LOGGER.info("====> LOCK_EXCLUSIVE没有被自己占有，注册节点删除监听器，抢锁阶段");
                addExclusiveListener(latch, LOCK_EXCLUSIVE);
                latch.await();
                exclusiveLock();
            }
        } else {
            LOGGER.info("====> LOCK_EXCLUSIVE没有被自己占有，注册节点监听器，验证阶段");
            addExclusiveListener(latch, LOCK_EXCLUSIVE);
            latch.await();
            exclusiveLock();
        }
    }

    private void addExclusiveListener(final CountDownLatch latch, String lockName) throws Exception {
        final NodeCache cache = new NodeCache(client, lockName, false);
        cache.start(true);
        cache.getListenable().addListener(new NodeCacheListener() {
            @Override
            public void nodeChanged() throws Exception {
                latch.countDown();
            }
        });
    }
}
