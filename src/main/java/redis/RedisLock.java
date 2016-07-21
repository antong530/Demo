package redis;

import org.apache.commons.lang3.time.DateUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Date;

/**
 * Created by antong on 16/7/19.
 */
public abstract class RedisLock {
    private static final Logger LOGGER = LoggerFactory.getLogger(RedisLock.class);
    private static final RedisFactory.StringOperation lock = RedisFactory.getStringOperation();
    //redis分布式锁

    /**
     * setnx（key，时间）确定是否锁上
     * 如果没有锁上，获取老1key的时间跟当前时间对比是否超时
     * 如果超时getset这个key新的时间得到老2key的时间
     * 判断得到的老1key时间与老2key的时间是否相同
     * 如果相同则中间没有别的线程操作，否则中间被别的线程获取
     */
    abstract void toDo();

    private boolean lock() {
        boolean isLock = lock.setNx("lock", new Date().getTime() + "");
        if (!isLock) {
            LOGGER.info("===>第一次未获取到锁");
            Date oldDate = new Date(Long.parseLong(lock.get("lock")));
            Date nowDate = new Date();
            if (DateUtils.addSeconds(nowDate, -10).compareTo(oldDate) > 0) {
                LOGGER.info("===>发生了死锁的情况");
                Date date = new Date(Long.parseLong(lock.getSet("lock", new Date().getTime()+"")));
                if (oldDate.compareTo(date) != 0) {
                    LOGGER.info("===>已经有别的线程处理了死锁的情况");
                    return false;
                } else {
                    LOGGER.info("===>处理死锁的情况");
                    return true;
                }
            }
            return false;
        }
        LOGGER.info("===>第一次获取到锁");
        return true;

    }

    private void unlock() {
        lock.del("lock");
    }

    public boolean doWork(){
        if(lock()){
            toDo();
            unlock();
            return true;
        }
        return false;
    }

}
