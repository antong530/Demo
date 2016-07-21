package RabbitMQ;

import com.rabbitmq.client.Channel;

/**
 * Created by antong on 15/12/29.
 */
public class Utils {
    private Channel channel;
    private String queueName;

    public Utils(Channel channel, String queueName) {
        this.channel = channel;
        this.queueName = queueName;
    }

    public Channel getChannel() {
        return channel;
    }

    public void setChannel(Channel channel) {
        this.channel = channel;
    }

    public String getQueueName() {
        return queueName;
    }

    public void setQueueName(String queueName) {
        this.queueName = queueName;
    }
}
