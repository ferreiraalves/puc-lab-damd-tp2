import utils.CSVReader;
import com.rabbitmq.client.*;

import java.io.IOException;
import java.util.ArrayList;
import java.util.concurrent.TimeoutException;


public class BolsaServer {

    private static final String EXCHANGE_NAME = "bolsa-jonas123";

    public static void main(String[] args) throws IOException, TimeoutException {
        CSVReader cr = new CSVReader();
        ArrayList<String> codigos = cr.getCodigos();

        ConnectionFactory factory = new ConnectionFactory();
        factory.setHost("localhost");
        Connection connection = factory.newConnection();
        Channel channel = connection.createChannel();

        channel.exchangeDeclare(EXCHANGE_NAME, BuiltinExchangeType.TOPIC);
        String queueName = channel.queueDeclare().getQueue();
        channel.queueBind(queueName, EXCHANGE_NAME, "compra.*");

        System.out.println(" [*] Waiting for messages. To exit press CTRL+C");

        Consumer consumer = new DefaultConsumer(channel) {
            @Override
            public void handleDelivery(String consumerTag, Envelope envelope,
                                       AMQP.BasicProperties properties, byte[] body) throws IOException {
                String message = new String(body, "UTF-8");
                System.out.println(" [x] Received '" + message + "'");
            }
        };
        channel.basicConsume(queueName, true, consumer);

    }
}
