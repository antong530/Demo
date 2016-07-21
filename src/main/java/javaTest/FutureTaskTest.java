package javaTest;

/**
 * Created by antong on 16/7/21.
 */
public class FutureTaskTest implements RunnableTest{
    private CallableTest callableTest;
    private Object outcome;

    public FutureTaskTest(CallableTest callableTest){
        this.callableTest = callableTest;
    }
    public void run(){
        this.outcome = callableTest.call();
    }

    public Object get(){
        return outcome;
    }

}
