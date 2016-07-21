package RocketMQ;

import com.alibaba.rocketmq.client.consumer.DefaultMQPushConsumer;
import com.alibaba.rocketmq.client.consumer.listener.*;
import com.alibaba.rocketmq.client.exception.MQBrokerException;
import com.alibaba.rocketmq.client.exception.MQClientException;
import com.alibaba.rocketmq.client.producer.DefaultMQProducer;
import com.alibaba.rocketmq.client.producer.MessageQueueSelector;
import com.alibaba.rocketmq.client.producer.SendResult;
import com.alibaba.rocketmq.client.producer.SendStatus;
import com.alibaba.rocketmq.client.producer.selector.SelectMessageQueueByHash;
import com.alibaba.rocketmq.common.consumer.ConsumeFromWhere;
import com.alibaba.rocketmq.common.message.Message;
import com.alibaba.rocketmq.common.message.MessageExt;
import com.alibaba.rocketmq.common.message.MessageQueue;
import com.alibaba.rocketmq.common.protocol.heartbeat.MessageModel;
import com.alibaba.rocketmq.remoting.exception.RemotingException;
import com.dajie.common.queue.QueueEntity;
import com.dajie.common.queue.rocket.RocketQueuePutter;
import org.apache.commons.collections.CollectionUtils;

import java.util.Iterator;
import java.util.List;
import java.util.Random;

/**
 * Created by antong on 15/12/31.
 */
public class RocketMQDemo {
    private static String PRODUCT_GROUP = "product";
    private static String CONSUMER_GROUP = "consumer";
    private static String ROCKET_SERVER = "192.168.11.73:9876";
    private static final RocketMQDemo.RandomShardExtractor RANDOM_SHARD_EXTRACTOR = new RocketMQDemo.RandomShardExtractor();
    private RocketMQDemo() {

    }

    public static DefaultMQProducer getProducer() {
        DefaultMQProducer producer = new DefaultMQProducer(PRODUCT_GROUP);
        producer.setNamesrvAddr(ROCKET_SERVER);
        return producer;
    }

    public static DefaultMQPushConsumer getConsumer() {
        DefaultMQPushConsumer consumer = new DefaultMQPushConsumer(CONSUMER_GROUP);
        consumer.setConsumeFromWhere(ConsumeFromWhere.CONSUME_FROM_LAST_OFFSET);
        consumer.setNamesrvAddr(ROCKET_SERVER);
        return consumer;
    }

    public static boolean sendMessage(String info) throws InterruptedException, RemotingException, MQClientException, MQBrokerException {
        Message message = new Message("test", "tags", "1", info.getBytes());
        DefaultMQProducer producer = getProducer();
        producer.start();
        for (int i = 0; i < 10; i++) {
            SendResult result = producer.send(message,new SelectMessageQueueByHash(),i);
        }
        producer.shutdown();
        return true;
    }

    public static void receiveMessage() throws MQClientException {
        DefaultMQPushConsumer consumer = getConsumer();
        consumer.subscribe("test", "*");
        consumer.registerMessageListener(new MessageListenerOrderly() {
            @Override
            public ConsumeOrderlyStatus consumeMessage(List<MessageExt> list, ConsumeOrderlyContext consumeOrderlyContext) {
                if (CollectionUtils.isNotEmpty(list)) {
                    Iterator it = list.iterator();
                    while (it.hasNext()) {
                        MessageExt msg = (MessageExt) it.next();
                        System.out.println(msg.toString());
                    }
                }
                return ConsumeOrderlyStatus.SUCCESS;
            }
        });
        consumer.start();
    }
    private static class RandomShardExtractor<T extends QueueEntity> implements RocketQueuePutter.ShardExtractor<T> {
        private final Random random;

        private RandomShardExtractor() {
            this.random = new Random();
        }

        public int shard(T t) {
            return Math.abs(this.random.nextInt());
        }
    }

}
