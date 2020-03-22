package utils;

import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class SubscriberRunnable implements Runnable {

    private final ArrayList<String> topics;
    private static final String EXCHANGE_NAME = "bolsa-jonas123";

    public SubscriberRunnable(ArrayList<String> topics) {
        this.topics = topics;
    }

    public void run() {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Configurations.getHost());
        Connection connection = null;
        try {
            connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
            String queueName = channel.queueDeclare().getQueue();
            for (String topic: this.topics) {
                channel.queueBind(queueName, EXCHANGE_NAME, topic);
            }

            Consumer consumer = new DefaultConsumer(channel) {
                @Override
                public void handleDelivery(String consumerTag, Envelope envelope,
                                           AMQP.BasicProperties properties, byte[] body) throws IOException {
                    String message = new String(body, "UTF-8");
                    String routingKey = envelope.getRoutingKey();
                    System.out.println(" [x] Received '" + routingKey + "\t"  + message + "'");
                }
            };
            channel.basicConsume(queueName, true, consumer);
            System.out.println("MyThread running");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}
