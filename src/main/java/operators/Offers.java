package operators;

import operations.Compra;

import java.util.ArrayList;
import java.util.HashMap;

public class Offers {
    static HashMap<String, ArrayList<Compra>> offerBook = new HashMap<String, ArrayList<Compra>>();

    public static HashMap<String, ArrayList<Compra>> getOfferBook() {
        return offerBook;
    }

    public static void addCompra(Compra compra){
        if (!offerBook.containsKey(compra.getAtivo())){
            offerBook.put(compra.getAtivo(),new ArrayList<Compra>());
        }
        offerBook.get(compra.getAtivo()).add(compra);
        System.out.println("NEW COMPRA ADDED");
    }
}
