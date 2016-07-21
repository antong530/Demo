package RocketMQ;

import com.alibaba.rocketmq.client.exception.MQClientException;

/**
 * Created by antong on 15/12/31.
 */
public class Consumer {
    public static void main(String[] args) throws MQClientException {
        RocketMQDemo.receiveMessage();
    }
}
