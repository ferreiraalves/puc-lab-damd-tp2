package operations;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonInclude;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.rabbitmq.client.BuiltinExchangeType;
import com.rabbitmq.client.Channel;
import com.rabbitmq.client.Connection;
import com.rabbitmq.client.ConnectionFactory;
import utils.Configurations;

import java.io.IOException;
import java.util.UUID;
import java.util.concurrent.TimeoutException;

public class Transaction {

    private String id;
    private Compra compra;
    private Venda venda;

    private String exchangeName = Configurations.getBolsaExchange();
    private  ObjectMapper mapper = new ObjectMapper();

    public Transaction(Compra compra, Venda venda) {
        this.compra = compra;
        this.venda = venda;
        this.id = UUID.randomUUID().toString();
        try {
            System.out.println("[INFO] NEW TRANSACTION: " + mapper.writeValueAsString(compra) + mapper.writeValueAsString(venda));
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public Compra getCompra() {
        return compra;
    }

    public void setCompra(Compra compra) {
        this.compra = compra;
    }

    public Venda getVenda() {
        return venda;
    }

    public void setVenda(Venda venda) {
        this.venda = venda;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public void notifyBolsaQueue(){
        try {
            ConnectionFactory factory = new ConnectionFactory();
            factory.setHost(Configurations.getHost());
            Connection connection = factory.newConnection();
            Channel channel = connection.createChannel();
            channel.exchangeDeclare(exchangeName, BuiltinExchangeType.TOPIC);
            String routingKey = "transaction." + compra.getAtivo();
            String msg = mapper.writeValueAsString(this);
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
