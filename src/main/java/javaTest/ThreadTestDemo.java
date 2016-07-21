package javaTest;

/**
 * Created by antong on 16/7/21.
 */
public class ThreadTestDemo {
    //java责任链模式
    public static void main(String[] args) {
        FutureTaskTest futureTaskTest = new FutureTaskTest(new CallableTest() {
            @Override
            public Object call() {
                return 111;
            }
        });
        new ThreadTest(futureTaskTest).start();
        System.out.println(futureTaskTest.get());
    }
}
