package operators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import operations.Compra;
import operations.Venda;
import utils.Configurations;
import utils.SubscriberRunnable;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;

public class Broker {
    private Channel channel;
    private String exchangeName;
    private Thread subscriberThread = new Thread();
    private ArrayList<String> subscribedTopics = new ArrayList<String>();

    public Broker(String exchangeName) {
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(Configurations.getHost());
        try {
            Connection connection = factory.newConnection();
            this.channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
        this.exchangeName = exchangeName;
        Runnable r = new SubscriberRunnable(subscribedTopics);
        subscriberThread = new Thread(r);
    }

    public void sendMsg(Compra compra) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String msg = mapper.writeValueAsString(compra);
        String routingKey = "compra." + compra.getAtivo();
        channel.basicPublish(this.exchangeName, routingKey, null, msg.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + routingKey + "':'" + msg + "'");
    }

    public void sendMsg(Venda venda) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String msg = mapper.writeValueAsString(venda);
        String routingKey = "venda." + venda.getAtivo();
        channel.basicPublish(this.exchangeName, routingKey, null, msg.getBytes("UTF-8"));
        System.out.println(" [x] Sent '" + routingKey + "':'" + msg + "'");
    }

    public void subscribe(String topic){
        subscribedTopics.add(topic);
        subscriberThread.interrupt();
        Runnable r = new SubscriberRunnable(subscribedTopics);
        subscriberThread = new Thread(r);
        subscriberThread.start();

    }
}

