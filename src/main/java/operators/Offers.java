package operators;

import operations.Compra;
import operations.Venda;

import java.util.ArrayList;
import java.util.HashMap;

public class Offers {
    static HashMap<String, ArrayList<Compra>> offerBookCompra = new HashMap<String, ArrayList<Compra>>();
    static HashMap<String, ArrayList<Venda>> offerBookVenda = new HashMap<String, ArrayList<Venda>>();

    public static HashMap<String, ArrayList<Compra>> getOfferBookCompra() {
        return offerBookCompra;
    }

    public static void add(Compra compra){
        if (!offerBookCompra.containsKey(compra.getAtivo())){
            offerBookCompra.put(compra.getAtivo(),new ArrayList<Compra>());
        }
        offerBookCompra.get(compra.getAtivo()).add(compra);
        System.out.println("NEW COMPRA ADDED");
    }

    public static void add(Venda venda){
        if (!offerBookVenda.containsKey(venda.getAtivo())){
            offerBookVenda.put(venda.getAtivo(),new ArrayList<Venda>());
        }
        offerBookVenda.get(venda.getAtivo()).add(venda);
        System.out.println("NEW VENDA ADDED");
    }

    public static Compra checkMatchingOffer(Venda venda) {
        if(offerBookCompra.containsKey(venda.getAtivo())){
            for (Compra compra : offerBookCompra.get(venda.getAtivo())){
                if (compra.getVal() >= venda.getVal()){
                    return compra;
                }
            }
        }
        return null;
    }


    public static Venda checkMatchingOffer(Compra compra) {
        if (offerBookVenda.containsKey(compra.getAtivo())){
            for (Venda venda : offerBookVenda.get(compra.getAtivo())){
                if (compra.getVal() >= venda.getVal()){
                    return venda;
                }
            }
        }

        return null;
    }

    public static void remove(Compra compra){
        offerBookCompra.get(compra.getAtivo()).remove(compra);
    }

    public static void remove(Venda venda){
        offerBookVenda.get(venda.getAtivo()).remove(venda);
    }
}
