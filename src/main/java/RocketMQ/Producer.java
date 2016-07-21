package RocketMQ;

import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.remoting.exception.RemotingException;

/**
 * Created by antong on 15/12/31.
 */
public class Producer {
    public static void main(String[] args) throws MQClientException, InterruptedException, RemotingException, MQBrokerException {
        RocketMQDemo.sendMessage("好");
    }
}
