import java.util.List;

/**
 * Created by antong on 15/12/11.
 */
public class ProducerAndConsumer {
    private volatile static boolean flag = true;
    public  Integer sum = 0;
    private static final Integer MAX = 5;

    public void add() throws InterruptedException {
        synchronized (this){
            if(sum == MAX){
                wait();
            }
            sum++;
            notify();
        }
    }
    public void get() throws InterruptedException {
        synchronized (this){
            if(sum == 0){
                wait();
            }
            sum--;
            notify();
        }
    }
    static class Producer implements Runnable {
        private ProducerAndConsumer producerAndConsumer;

        Producer(ProducerAndConsumer producerAndConsumer) {
            this.producerAndConsumer = producerAndConsumer;
        }

        public void run() {
            try {
                producerAndConsumer.add();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }
    static class Consumer implements Runnable{
        private ProducerAndConsumer producerAndConsumer;
        Consumer(ProducerAndConsumer producerAndConsumer){
            this.producerAndConsumer = producerAndConsumer;
        }
        public void run() {
            try {
                producerAndConsumer.get();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }
        }
    }

    public static void main(String[] args){
        ProducerAndConsumer producerAndConsumer = new ProducerAndConsumer();
        new Thread(new Producer(producerAndConsumer)).start();
        new Thread(new Consumer(producerAndConsumer)).start();
    }


}
