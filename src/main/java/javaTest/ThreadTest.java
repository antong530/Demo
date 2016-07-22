package javaTest;

/**
 * Created by antong on 16/7/21.
 */
/*
new Thread会调用init（）方法init方法会初始化线程组，线程设置是否是守护线程，设置runnable，初始化contextclassloader
每个线程执行都会添加到对应的线程组
开启线程 start0（）是个本地方法，有c实现，其中调用了target的run方法
futureTask实现了runnable接口，在其调用run方法时会将callback的run方法返回的值放到future的outcome上
 */
public class ThreadTest {
    public RunnableTest target;
    public ThreadTest(RunnableTest runnableTest){
        this.target = runnableTest;
    }

    public void run(){
        target.run();
    }

    public void start(){
        run();
    }
}
