package javaTest;

import java.util.HashSet;
import java.util.Set;
import java.util.concurrent.BlockingQueue;

/**
 * Created by antong on 16/7/25.
 */

/**
 * 在调用execute方法后调用addWorker方法，创建worker（任务）时在worker上创建Thread并付予任务和线程，调用thread的start创建线程，执行Worker的run方法执行任务
 */
public class ThreadPoolTest {
    private int corePoolSize;
    private int maxPoolSize;
    private BlockingQueue blockingQueue;
    private HashSet workers;
    private int nowSize;
    private ThreadFactory threadFactory;

    public ThreadPoolTest(int corePoolSize, int maxPoolSize, BlockingQueue blockingQueue, ThreadFactory threadFactory) {
        this.corePoolSize = corePoolSize;
        this.maxPoolSize = maxPoolSize;
        this.blockingQueue = blockingQueue;
        this.threadFactory = threadFactory;
    }

    class Worker implements Runnable {
        private Runnable target;
        private Thread thread;

        public Worker(Runnable target) {
            this.target = target;
            this.thread = threadFactory.newThread(this);
        }

        @Override
        public void run() {
            runWorker(this);
        }
    }

    class ThreadFactory {
        public Thread newThread(Runnable target) {
            return new Thread(target);
        }
    }

    public void execute(Runnable runnable) {
        if (nowSize < corePoolSize) {
            addWorker(runnable);
        } else if(nowSize > corePoolSize && nowSize < maxPoolSize && !blockingQueue.isEmpty()){
            addWorker(runnable);
        }
    }

    private void addWorker(Runnable runnable) {
        Worker worker = new Worker(runnable);
        worker.thread.start();
        workers.add(worker);
    }

    private void runWorker(Worker worker){
        worker.target.run();
    }
}
