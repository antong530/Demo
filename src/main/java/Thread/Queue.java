package Thread;

import java.util.ArrayList;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;

/**
 * Created by antong on 16/1/26.
 */
public class Queue {
    private ReentrantLock putLock = new ReentrantLock();
    private Condition putCondition = putLock.newCondition();
    private ReentrantLock takeLock = new ReentrantLock();
    private Condition takeCondition = takeLock.newCondition();
    private AtomicInteger count = new AtomicInteger(0);
    private final Integer MAX = 10;
    private List<Integer> list = new ArrayList<Integer>();
    public void put(Integer i) throws InterruptedException {
        final ReentrantLock putLock = this.putLock;
        int c = -1;
        putLock.lock();
        try {
            while (count.get() == MAX) {
                putCondition.await();
            }
            list.add(i);
            c = count.getAndIncrement();
            if (c + 1 < MAX) {
                putCondition.signal();
            }
        }finally {
            putLock.unlock();
        }
        if(c == 0){
            final ReentrantLock takeLock = this.takeLock;
            takeLock.lock();
            try {
                takeCondition.signal();
            }finally {
                takeLock.unlock();
            }
        }
    }

    public Integer take() throws InterruptedException {
        final ReentrantLock takeLock = this.takeLock;
        takeLock.lock();
        int c = -1;
        Integer i = -1;
        try{
            while (count.get() == 0){
                takeCondition.await();
            }
            i = list.remove(count.get());
            c = count.getAndDecrement();
            if(c > 1){
                takeCondition.signal();
            }
        }finally {
            takeLock.unlock();
        }
        if(c == MAX){
            final ReentrantLock putLock = this.putLock;
            putLock.lock();
            try {
                putCondition.signal();
            }finally {
                putLock.unlock();
            }
        }
        return i;
    }
}
