import java.util.List;

/**
 * Created by antong on 15/12/11.
 */
public class ProducerAndConsumer1 {
    private volatile static boolean flag = true;
    public static Integer sum = 0;
    private static final Integer MAX = 5;

    static class ProducerAndConsumer implements Runnable {
        String temp;
        public ProducerAndConsumer(String temp){
            this.temp = temp;
        }
        public void run() {
            try {
                if(temp.equals("Producer")){
                    synchronized (this){
                        if(sum == MAX){
                            wait();
                        }
                        sum++;
                        notify();
                    }
                }
                if(temp.equals("Consumer")){
                    synchronized (this){
                        if(sum == 0){
                            wait();
                        }
                        sum--;
                        notify();
                    }
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
    }


    public static void main(String[] args){
        new Thread(new ProducerAndConsumer("Producer")).start();
        new Thread(new ProducerAndConsumer("Consumer")).start();
    }


}
