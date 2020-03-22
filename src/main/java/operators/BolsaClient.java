package operators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import operations.Compra;
import operations.Transaction;
import operations.Venda;
import utils.Configurations;

import java.io.IOException;
import java.util.concurrent.TimeoutException;

public class BolsaClient {

    private static ObjectMapper mapper = new ObjectMapper();

    private static String exchangeName = Configurations.getBolsaExchange();

    public static void processCompra(String msg, String ativo){
        try {
            Compra compra = mapper.readValue(msg, Compra.class);
            compra.setAtivo(ativo);
            Venda venda = Offers.checkMatchingOffer(compra);
            if (venda == null){
                Offers.add(compra);
                notifyBolsaQueue(compra);
            }else{
                Offers.remove(venda);
                Transaction transaction = new Transaction(compra, venda);
                Transactions.add(transaction);
                transaction.notifyBolsaQueue();
            }


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public static void processVenda(String msg, String ativo){
        try {
            Venda venda = mapper.readValue(msg, Venda.class);
            venda.setAtivo(ativo);
            Compra compra = Offers.checkMatchingOffer(venda);
            if (compra != null) {
                Offers.remove(compra);
                Transaction transaction = new Transaction(compra, venda);
                Transactions.add(transaction);
                transaction.notifyBolsaQueue();
            }
            else{
                Offers.add(venda);
                notifyBolsaQueue(venda);

            }


        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }

    }

    public static void notifyBolsaQueue(Compra compra){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(Configurations.getHost());
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            String routingKey = "compra." + compra.getAtivo();
            String msg = mapper.writeValueAsString(compra);
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + msg + "'");
            connection.close();

        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
    public static void notifyBolsaQueue(Venda venda){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(Configurations.getHost());
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            String routingKey = "compra." + venda.getAtivo();
            String msg = mapper.writeValueAsString(venda);
            channel.basicPublish(exchangeName, routingKey, null, msg.getBytes("UTF-8"));
            System.out.println(" [x] Sent '" + routingKey + "':'" + msg + "'");
            connection.close();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (TimeoutException e) {
            e.printStackTrace();
        }
    }
}


