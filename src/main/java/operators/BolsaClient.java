package operators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import operations.Compra;
import utils.Configurations;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BolsaClient {

    private static ObjectMapper mapper = new ObjectMapper();

    private static String exchangeName = Configurations.getBrokerExchange();

    public static void processCompra(String msg, String ativo){
        try {
            Compra compra = mapper.readValue(msg, Compra.class);
            compra.setAtivo(ativo);
            Offers.addCompra(compra);
            notifyBrokerQueue(compra);


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public void processVenda(String msg, String ativo){

    }

    public static void notifyBrokerQueue(Compra compra){
        ConnectionFactory factory = new ConnectionFactory();

        factory.setHost(Configurations.getHost());
        try {
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            String routingKey = "compra." + compra.getAtivo();
            String msg = mapper.writeValueAsString(compra);
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + msg + "'");
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}


