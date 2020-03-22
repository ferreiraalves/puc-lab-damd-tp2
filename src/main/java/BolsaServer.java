import operators.BolsaClient;
import utils.CSVReader;
import com.rabbitmq.client.*;
import utils.Configurations;

import javax.swing.*;
import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;


public class BolsaServer {

    private static final String EXCHANGE_NAME = Configurations.getBrokerExchange();

    public static void main(String[] args) throws IOException, TimeoutException {

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost(Configurations.getHost());
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "*.*");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            String routingKey = delivery.getEnvelope().getRoutingKey();
            System.out.println(" [x] Received '" + routingKey + "\t"  + message + "'");
            String operation = routingKey.split("[.]")[0];
            String ativo = routingKey.split("[.]")[1];
            if (operation.equals("compra")){
                BolsaClient.processCompra(message, ativo);

            } else if(operation.equals("venda")){
                BolsaClient.processVenda(message, ativo);
                System.out.println("PROCESSANDO VENDA");
            } else{
                System.out.println("FALHA");
            }

        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

    }
}
