import operations.Compra;
import operations.Venda;
import operators.Broker;
import utils.CSVReader;
import utils.Configurations;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Broker broker = new Broker(Configurations.getBrokerExchange());
        try {
            broker.subscribe("*.*");
            Compra compra = new Compra(2,  3.50f, "Hugo", "BBDC4");
            broker.send_msg(compra);
            Venda venda = new Venda(2,  2.50f, "Jonas", "BBDC4");
            broker.send_msg(venda);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
