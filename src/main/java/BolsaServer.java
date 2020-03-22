import operators.BolsaClient;
import utils.CSVReader;
import com.rabbitmq.client.*;
import utils.Configurations;

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

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                String routingKey = envelope.getRoutingKey();
                System.out.println(" [x] Received '" + routingKey + "\t"  + message + "'");
                String operation = routingKey.split("[.]")[0];
                String ativo = routingKey.split("[.]")[1];
                if (operation.equals("compra")){
                    System.out.println("PROCESSANDO COMPRA");
                    BolsaClient.processCompra(message, ativo);

                } else if(operation.equals("venda")){
                    System.out.println("PROCESSANDO VENDA");
                } else{
                    System.out.println("FALHA");
                }

            }
        };
        DeliverCallback deliverCallback = (consumerTag, delivery) -> {
            String message = new String(delivery.getBody(), "UTF-8");
            System.out.println(" [x] Received '" +
                    delivery.getEnvelope().getRoutingKey() + "':'" + message + "'");
        };
        channel.basicConsume(queueName, true, deliverCallback, consumerTag -> { });

    }
}
