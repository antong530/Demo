package ZookeeperTest;

import org.apache.commons.beanutils.BeanUtils;
import org.apache.commons.collections.CollectionUtils;
import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingServer;
import org.apache.zookeeper.CreateMode;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.List;

/**
 * Created by antong on 16/1/28.
 */
public class ZookeeperDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperDemo.class);
    private static TestingServer server;

    static {
        try {
            server = new TestingServer(2181, new File("/Users/antong/IdeaProjects/zookeeper-test/zk-book-data"));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static TestingServer getServer(){
        return server;
    }

    public static CuratorFramework startZkClient(){
        CuratorFramework curatorFramework = CuratorFrameworkFactory.builder()
                .connectString(getServer().getConnectString())
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        curatorFramework.start();
        return curatorFramework;
    }

    public static void testZookeeper() throws Exception {
        TestingServer server = getServer();
        CuratorFramework curatorFramework = startZkClient();
        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/test/test");
        List<String> list = curatorFramework.getChildren().forPath("/test");
        LOGGER.info("====> list={}",list);
        curatorFramework.close();
        server.close();
    }

    public static void delete() throws Exception {
        TestingServer server = getServer();
        CuratorFramework curatorFramework = startZkClient();
        curatorFramework.delete().forPath("/test/test");
    }


    public static void masterLock() throws Exception {
        TestingServer server = getServer();
        CuratorFramework curatorFramework = startZkClient();
        if(curatorFramework.checkExists().forPath("/test/test") != null){
            final PathChildrenCache pathChildrenCache = new PathChildrenCache(curatorFramework,"/test",true);
            pathChildrenCache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
            pathChildrenCache.getListenable().addListener(new PathChildrenCacheListener() {
                @Override
                public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                    if(pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED){
                        curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/test/test");
                        LOGGER.info("====> 上一个任务执行成功，下个任务抢到可执行锁");
                        curatorFramework.delete().forPath("/test/test");
                    }
                }
            });
        }else{
            curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL).forPath("/test/test");
            LOGGER.info("====> 抢到可执行锁");
            Thread.sleep(5000);
            curatorFramework.delete().forPath("/test/test");
        }
    }

    public static void pessimisticLock() throws Exception {
        TestingServer server = getServer();
        CuratorFramework curatorFramework = startZkClient();
        String node = curatorFramework.create().creatingParentsIfNeeded().withMode(CreateMode.EPHEMERAL_SEQUENTIAL).forPath("/test/test");
        List<String> nodes = curatorFramework.getChildren().forPath("/test");
        if(CollectionUtils.isNotEmpty(nodes) && nodes.get(0).equals(node)){
            LOGGER.info("===> 正在执行任务");
        }
        if(!nodes.get(0).equals(node)){
            String tmpNode = node.substring(0,node.indexOf("/test/test")) + (Long.parseLong(node.substring(node.indexOf("/test/test"))) - 1);
            final NodeCache nodeCache = new NodeCache(curatorFramework,tmpNode,true);
            nodeCache.getListenable().addListener(new NodeCacheListener() {
                @Override
                public void nodeChanged() throws Exception {
                    LOGGER.info("====> 启动下一个任务");
                }
            });
        }
    }
    public static void main(String[] args) throws Exception {

    }

}
