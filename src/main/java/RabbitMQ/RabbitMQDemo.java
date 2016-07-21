package RabbitMQ;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.Random;
import java.util.concurrent.TimeoutException;

/**
 * Created by antong on 15/12/29.
 */
public class RabbitMQDemo {
    private static final String QUEUE_NAME = "test";
    private static final String EXCHANGE = "exchange_name";
    private static final String[] SEVERITIES = {"black","blue","red"};

    private static ConnectionFactory factory = new ConnectionFactory();
    private RabbitMQDemo(){}
    static {
        factory.setHost("localhost");
    }
    public static Connection getConnection() throws IOException, TimeoutException {
        return factory.newConnection();
    }

    public static void close(Channel channel,Connection connection) throws IOException, TimeoutException {
        channel.close();
        connection.close();
    }

    //发送队列  work-queues
    public static void sendQueue() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        //String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        String message = "Hello World!";
        for(int i = 0;i < 10;i++) {
            //String exchange（“”）, String routingKey（队列名称）, BasicProperties props, byte[] body
            channel.basicPublish("", QUEUE_NAME, null, (message+i).getBytes());
            System.out.println(" [x] Sent '" + message + "'" + i);
        }
        close(channel, connection);
    }
    //接受队列  work-queues
    public static void receiveQueue() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        //String queue, boolean durable, boolean exclusive, boolean autoDelete, Map<String, Object> arguments
        channel.queueDeclare(QUEUE_NAME, false, false, false, null);
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(QUEUE_NAME, true, consumer);
    }



    //广播模式
    public static void sendExchange() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        //String exchange, String type(队列模式)（fanout，direct，topic）
        channel.exchangeDeclare(EXCHANGE, "fanout");
        String message = "Hello World!";
        for(int i = 0;i < 10;i++) {
            //String exchange, String routingKey（“”）, BasicProperties props, byte[] body
            channel.basicPublish(EXCHANGE, "", null, (message+i).getBytes());
            System.out.println(" [x] Sent '" + message + "'" + i);
        }
        close(channel, connection);
    }
    //广播模式
    public static void receiveExchange() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE, "fanout");
        String queueName = channel.queueDeclare().getQueue();
        //String queue, String exchange, String routingKey(“”)
        channel.queueBind(queueName, EXCHANGE, "");
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C"+queueName);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }



    //直连模式
    public static void sendDirect() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        //String exchange, String type(队列模式)（fanout，direct，topic）
        channel.exchangeDeclare(EXCHANGE, "direct");
        String message = "Hello World!";
        for(int i = 0;i < 10;i++) {
            //String exchange, String routingKey（主题）, BasicProperties props, byte[] body
            channel.basicPublish(EXCHANGE, getSeverity(), null, (message+getSeverity()).getBytes());
            System.out.println(" [x] Sent '" + message + "'" + i);
        }
        close(channel, connection);
    }
    //直连模式
    public static void receiveDirect() throws IOException, TimeoutException {
        Connection connection = getConnection();
        Channel channel = connection.createChannel();
        channel.exchangeDeclare(EXCHANGE, "direct");
        String queueName = channel.queueDeclare().getQueue();
        //String queue, String exchange, String routingKey(主题)
        channel.queueBind(queueName, EXCHANGE, getSeverity());
        System.out.println(" [*] Waiting for messages. To exit press CTRL+C"+queueName);
        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope, AMQP.BasicProperties properties, byte[] body)
                    throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);
    }
    private static String getSeverity()
    {
        Random random = new Random();
        int ranVal = random.nextInt(3);
        return SEVERITIES[ranVal];
    }

}
