import org.apache.commons.lang.math.NumberUtils;

import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.*;
import java.util.concurrent.CountDownLatch;
import java.util.concurrent.LinkedBlockingQueue;

/**
 * Created by antong on 16/1/27.
 */
public class Demo {
    //    private static final List<Integer> list = new ArrayList<Integer>();
//    public static void main(String[] args) throws NoSuchMethodException, IllegalAccessException, InvocationTargetException {
//        Integer i = 653554;
//        Integer j = 1;
//        System.out.print(i==j);
//    }
//
//    private static void setAttirbute(A ctsCoreModel,String methodName,Object value,Class c) throws NoSuchMethodException, InvocationTargetException, IllegalAccessException {
//        if(methodName.contains("Int")){
//            value = Integer.valueOf((String) value);
//        }
//        Method method = ctsCoreModel.getClass().getDeclaredMethod("set"+methodName,c);
//        method.invoke(ctsCoreModel, value);
//    }
//
//    static class A{
//        private Integer intFree1;
//        private String stringFree1;
//        private String txtFree1;
//
//        public Integer getIntFree1() {
//            return intFree1;
//        }
//
//        public void setIntFree1(Integer intFree1) {
//            this.intFree1 = intFree1;
//        }
//
//        public String getStringFree1() {
//            return stringFree1;
//        }
//
//        public void setStringFree1(String stringFree1) {
//            this.stringFree1 = stringFree1;
//        }
//
//        public String getTxtFree1() {
//            return txtFree1;
//        }
//
//        public void setTxtFree1(String txtFree1) {
//            this.txtFree1 = txtFree1;
//        }
//
//        @Override
//        public String toString() {
//            return "A{" +
//                    "intFree1=" + intFree1 +
//                    ", stringFree1='" + stringFree1 + '\'' +
//                    ", txtFree1='" + txtFree1 + '\'' +
//                    '}';
//        }
//    }
//    public static void main(String[] args){
//        final CountDownLatch countDownLatch = new CountDownLatch(3);
//        for(int i = 0; i<3;i++){
//            final int finalI = i;
//            new Thread(new Runnable() {
//                @Override
//                public void run() {
//                    try{
//                        Thread.sleep(finalI*1000);
//                        System.out.println("===>>>>>"+ finalI);
//                        countDownLatch.countDown();
//                    }catch (Exception e){
//
//                    }
//                }
//            }).start();
//        }
//        try {
//            countDownLatch.await();
//        } catch (InterruptedException e) {
//            e.printStackTrace();
//        }
//    }
    private static final LinkedBlockingQueue<String> queue = new LinkedBlockingQueue<String>();

    /**
     * 如何队列已满，put将会被阻塞，当take取走后，判断减少前统计线程是否等于容量，唤醒put线程
     * 如果队列为空，take将会被阻塞，当put放入后，判断增加前统计线程是否等于0，唤醒take线程
     * 如果队列不满，put不会被阻塞更新统计，释放put线程，
     * 如果队列不满，take不会被阻塞更新统计，释放take线程，
     * @param args
     */
    public static void main(String[] args) {
        try {
            queue.put("1");
            queue.put("1");
        } catch (InterruptedException e) {
            e.printStackTrace();
        }
    }
}

