package operators;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import operations.Compra;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.util.concurrent.TimeoutException;

public class Broker {

    private Channel channel;
    private String exchangeName;

    public Broker(String exchangeName) {
        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
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
    }

    public void send_msg(Compra compra) throws IOException {
        ObjectMapper mapper = new ObjectMapper();
        String msg = mapper.writeValueAsString(compra);
        String routingKey = "compra." + compra.getAtivo();
        channel.basicPublish(this.exchangeName, routingKey, null, msg.getBytes("UTF-8"));
    }
}

