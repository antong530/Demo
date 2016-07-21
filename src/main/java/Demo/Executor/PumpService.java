package Demo.Executor;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * Created by antong on 16/7/12.
 */
public class PumpService {
    private static final PumpService pumpService = new PumpService();
    private static ExecutorService executorService = Executors.newFixedThreadPool(6);

    public static PumpService getPumpService() {
        return pumpService;
    }

    public void pump(final Command command) {
        executorService.execute(new Runnable() {
            @Override
            public void run() {
                try {
                    command.run();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
            }
        });
    }


    public static void main(String[] args) {
        PumpService pumpService = PumpService.getPumpService();
        for (int i = 0; i < 10; i++) {
            final int finalI = i;
            pumpService.pump(new Command() {
                @Override
                public void run() {
                    System.out.println("===>"+ finalI);
                }
            });
        }
    }
}
