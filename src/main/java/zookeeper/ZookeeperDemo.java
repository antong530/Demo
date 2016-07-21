package zookeeper;

import Demo.Executor.Command;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by antong on 16/7/20.
 */
public class ZookeeperDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(ZookeeperDemo.class);

    public static void main(String[] args) {
        for (int i = 0; i < 2; i++) {
            final int finalI = i;
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ZookeeperClient.getInstance(new Command("write") {
                        @Override
                        public void run() throws InterruptedException {
                            LOGGER.info("===>正在写执行任务 {}", finalI);
                            Thread.sleep(10000);
                        }
                    }).doShareLock();
                }
            }).start();
            new Thread(new Runnable() {
                @Override
                public void run() {
                    ZookeeperClient.getInstance(new Command("11") {
                        @Override
                        public void run() throws InterruptedException {
                            LOGGER.info("===>正在执行读任务 {}", finalI);
                            Thread.sleep(3000);
                        }
                    }).doShareLock();
                }
            }).start();
        }
    }
}
