package javaTest;

/**
 * Created by antong on 16/7/21.
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
