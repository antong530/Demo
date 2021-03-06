package Ali.RocketMQ;


import com.aliyun.openservices.ons.api.*;

import java.util.Properties;

/**
 * Created by antong on 15/12/30.
 */
public class RocketMQDemo {
    public static void Producer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ProducerId, "PID_antonTest");
        properties.put(PropertyKeyConst.AccessKey, "siH5MgcjV1bJrJEA");
        properties.put(PropertyKeyConst.SecretKey, "H4sPENSWxMo2FXRXn6TA3rrMu4wvta");
        Producer producer = ONSFactory.createProducer(properties);
        // 在发送消息前，必须调用start方法来启动Producer，只需调用一次即可。
        producer.start();

        //循环发送消息
        while (true) {
            Message msg = new Message( //
                    // Message Topic
                    "antongTest",
                    // Message Tag,
                    // 可理解为Gmail中的标签，对消息进行再归类，方便Consumer指定过滤条件在ONS服务器过滤
                    "TagA",
                    // Message Body
                    // 任何二进制形式的数据， ONS不做任何干预，
                    // 需要Producer与Consumer协商好一致的序列化和反序列化方式
                    "Hello ONS".getBytes());
            // 设置代表消息的业务关键属性，请尽可能全局唯一。
            // 以方便您在无法正常收到消息情况下，可通过ONS Console查询消息并补发。
            // 注意：不设置也不会影响消息正常收发
            msg.setKey("ORDERID_100");
            // 发送消息，只要不抛异常就是成功
            SendResult sendResult = producer.send(msg);
            System.out.println(sendResult);
        }

        // 在应用退出前，销毁Producer对象
        // 注意：如果不销毁也没有问题
    }

    public static void Consumer() {
        Properties properties = new Properties();
        properties.put(PropertyKeyConst.ConsumerId, "CID_antongTest");
        properties.put(PropertyKeyConst.AccessKey, "siH5MgcjV1bJrJEA");
        properties.put(PropertyKeyConst.SecretKey, "H4sPENSWxMo2FXRXn6TA3rrMu4wvta");
        Consumer consumer = ONSFactory.createConsumer(properties);
        consumer.subscribe("antongTest", "*", new MessageListener() {
            public Action consume(Message message, ConsumeContext context) {
                System.out.println("Receive: " + message);
                return Action.CommitMessage;
            }
        });
        consumer.start();
        System.out.println("Consumer Started");
    }
}
