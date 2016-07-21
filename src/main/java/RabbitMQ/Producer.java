package RabbitMQ;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

/**
 * Created by antong on 15/12/29.
 */
public class Producer {
    public static void main(String[] args) throws IOException, TimeoutException {
        RabbitMQDemo.sendExchange();
    }
}
