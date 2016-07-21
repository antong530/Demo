package redis;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Created by antong on 16/7/20.
 */
public class RedisDemo {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisDemo.class);

    public static void main(String[] args) {
        final RedisLock lockWork = new RedisLock() {
            @Override
            void toDo() {
                LOGGER.info("===>doWork");
            }
        };
        for (int i = 0; i < 10; i++) {
            new Thread(new Runnable() {
                @Override
                public void run() {
                    lockWork.doWork();
                }
            }).start();
        }
    }
}
