import org.apache.curator.framework.CuratorFramework;
import org.apache.curator.framework.CuratorFrameworkFactory;
import org.apache.curator.framework.recipes.cache.*;
import org.apache.curator.framework.recipes.leader.LeaderSelector;
import org.apache.curator.framework.recipes.leader.LeaderSelectorListenerAdapter;
import org.apache.curator.retry.ExponentialBackoffRetry;
import org.apache.curator.test.TestingCluster;
import org.apache.curator.test.TestingServer;
import org.apache.curator.test.TestingZooKeeperServer;
import org.apache.zookeeper.CreateMode;
import org.apache.zookeeper.data.Stat;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

/**
 * Created by antong on 15/12/2.
 */
public class ZookeeperTest {
    private static final Logger logger = LoggerFactory.getLogger(ZookeeperTest.class);
    private static final String PATH = "/test_master";
    private static final String SELF_X_PATH = "/test_x_master";
    private static final String SELF_S_PATH = "/test_s_master";
    public static void main(String[] args) throws Exception {
        selfSMaster();
    }

    public static void testClusterMaster() throws Exception {
        CuratorFramework client = getTestClusterClient();
            LeaderSelector leaderSelector = new LeaderSelector(client, PATH,
                    new LeaderSelectorListenerAdapter() {
                        public void takeLeadership(CuratorFramework client) throws Exception {
                            logger.info("我是master1");
                            Thread.sleep(10000);
                            logger.info(" " + client.getChildren().forPath(PATH));
                        }
                    }
            );
//            leaderSelector.autoRequeue();
            leaderSelector.start();
            Thread.sleep(Integer.MAX_VALUE);
    }

    public static CuratorFramework getTestClusterClient() throws Exception {
        TestingServer testingServer = new TestingServer(2181,new File("/Users/antong/IdeaProjects/zookeeper-test/zk-book-data"));
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(testingServer.getConnectString())
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
//        client.create().forPath(PATH);
        return client;
    }
    public static void selfXMaster() throws Exception {
        CuratorFramework client = getTestClusterClient();
        if(client.checkExists().forPath(SELF_X_PATH+"/master") != null){
            selfSMaster();
        }
        addNodeListener(client,SELF_X_PATH);
        client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL)
                .forPath(SELF_X_PATH+"/master");
        logger.info("已经获取可执行锁");
        Thread.sleep(10000);
        client.delete()
                .deletingChildrenIfNeeded()
                .forPath(SELF_X_PATH + "/master");
    }


    public static void addNodeListener(CuratorFramework client,final String path) throws Exception {
        PathChildrenCache cache = new PathChildrenCache(client,path,true);
        cache.start(PathChildrenCache.StartMode.POST_INITIALIZED_EVENT);
        cache.getListenable().addListener(
                new PathChildrenCacheListener() {
                    public void childEvent(CuratorFramework curatorFramework, PathChildrenCacheEvent pathChildrenCacheEvent) throws Exception {
                        if (pathChildrenCacheEvent.getType() == PathChildrenCacheEvent.Type.CHILD_REMOVED) {
                            if (path.equals(SELF_S_PATH)) {
                                selfSMaster();
                            } else if (path.equals(SELF_X_PATH)) {
                                selfXMaster();
                            }
                        }
                    }
                }
        );
    }
    public static void selfSMaster() throws Exception {
        CuratorFramework client = getTestClusterClient();
        addNodeListener(client, SELF_S_PATH);
        String lock = client.create()
                .creatingParentsIfNeeded()
                .withMode(CreateMode.EPHEMERAL_SEQUENTIAL)
                .forPath(SELF_S_PATH + "/master-");
        selfSMasterLis(client,lock);

    }

    public static void selfSMasterLis(CuratorFramework client,String lock) throws Exception {
        logger.info("开始获取childs");
        List<String> childs = client.getChildren().forPath(SELF_S_PATH);
        logger.info("childs为 " + childs);
        Collections.sort(childs, new Comparator<String>() {
            public int compare(String o1, String o2) {
                return o2.compareTo(o1);
            }
        });
        logger.info("childs" + childs.get(0));
        logger.info("lock" + lock);
        if((SELF_S_PATH+"/"+childs.get(0)).equals(lock)) {
            logger.info("已经获取可执行锁 " + lock);
            Thread.sleep(10000);
            client.delete()
                    .deletingChildrenIfNeeded()
                    .forPath(lock);
        }
    }


    public static void testserver() throws Exception {
        TestingServer testingServer = new TestingServer(2181,new File("/Users/antong/IdeaProjects/zookeeper-test/zk-book-data"));
        CuratorFramework client = CuratorFrameworkFactory.builder()
                .connectString(testingServer.getConnectString())
                .sessionTimeoutMs(5000)
                .retryPolicy(new ExponentialBackoffRetry(1000, 3))
                .build();
        client.start();
//        client.delete().deletingChildrenIfNeeded().forPath("/zkbook");
        client.create().creatingParentsIfNeeded().forPath("/zkbook/c1");
        client.setData().forPath("/zkbook/c1", "1".getBytes());
        logger.info(" " + client.getChildren().forPath("/zkbook"));
        Stat stat = new Stat();
        logger.info(" " + new String(client.getData().storingStatIn(stat).forPath("/zkbook/c1")));
        logger.info(" " + stat.getVersion());
        testingServer.close();
    }
}
