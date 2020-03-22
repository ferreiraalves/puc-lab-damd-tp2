import operations.Compra;
import operators.Broker;
import utils.CSVReader;

import java.io.IOException;

public class Main {
    public static void main(String[] args) {
        Broker broker = new Broker("bolsa-jonas123");
        try {
            Compra compra = new Compra(2,  3.50f, "Jonas", "BBDC4");
            broker.send_msg(compra);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
