package Demo.Thread;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by antong on 16/7/12.
 */
public class ProducerAndConsumer<T> {
    private static ProducerAndConsumer producerAndConsumer;
    private List<T> list = new ArrayList<T>();
    private static AtomicInteger size = new AtomicInteger(0);
    private static int capacity = 10;
    private ReentrantLock putLock = new ReentrantLock();
    private Condition notFull = putLock.newCondition();
    private ReentrantLock takeLock = new ReentrantLock();
    private Condition notEmpty = takeLock.newCondition();

    private ProducerAndConsumer() {
    }

    public static ProducerAndConsumer getInstance() {
        if (producerAndConsumer == null) {
            synchronized (ProducerAndConsumer.class) {
                if (producerAndConsumer == null) {
                    producerAndConsumer = new ProducerAndConsumer();
                }
            }
        }
        return producerAndConsumer;
    }

    public void push(T t) {
        putLock.lock();
        if (size.get() == capacity) {
            try {
                notFull.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list.add(t);
        Integer i = size.getAndIncrement();
        notFull.signal();
        putLock.unlock();
        if (i == 0) {
            takeLock.lock();
            notEmpty.signal();
            takeLock.unlock();
        }

    }

    public void take(T t) {
        takeLock.lock();
        if (size.get() == capacity) {
            try {
                notEmpty.await();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
        list.remove(t);
        Integer i = size.getAndDecrement();
        notEmpty.signal();
        takeLock.unlock();
        if (i == capacity) {
            putLock.lock();
            notFull.signal();
            putLock.unlock();
        }

    }
}
