package operators;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import operations.Compra;
import operations.Venda;
import utils.CSVReader;

import java.util.ArrayList;
import java.util.HashMap;

public class Offers {
    static HashMap<String, ArrayList<Compra>> offerBookCompra = new HashMap<String, ArrayList<Compra>>();
    static HashMap<String, ArrayList<Venda>> offerBookVenda = new HashMap<String, ArrayList<Venda>>();

    public static HashMap<String, ArrayList<Compra>> getOfferBookCompra() {
        return offerBookCompra;
    }

    private static ObjectMapper mapper = new ObjectMapper();

    public static ArrayList<String> validAtivos = CSVReader.getCodigos();

    public static void add(Compra compra){
        try{
            if (validAtivos.contains(compra.getAtivo())){
                if (!offerBookCompra.containsKey(compra.getAtivo())){
                    offerBookCompra.put(compra.getAtivo(),new ArrayList<Compra>());
                }
                offerBookCompra.get(compra.getAtivo()).add(compra);
                System.out.println("[INFO] NEW COMPRA: " + mapper.writeValueAsString(compra));
            }else{
                System.out.println("[ERROR] INVALID COMPRA: " + mapper.writeValueAsString(compra));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


    }

    public static void add(Venda venda){
        try{
            if(validAtivos.contains(venda.getAtivo())){
                if (!offerBookVenda.containsKey(venda.getAtivo())){
                    offerBookVenda.put(venda.getAtivo(),new ArrayList<Venda>());
                }
                offerBookVenda.get(venda.getAtivo()).add(venda);
                System.out.println("[INFO] NEW VENDA: " + mapper.writeValueAsString(venda));
            }else{
                System.out.println("[ERROR] INVALID COMPRA: " + mapper.writeValueAsString(venda));
            }
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }


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
        try{
            System.out.println("[INFO] REMOVENDO COMPRA: " + mapper.writeValueAsString(compra));
            offerBookCompra.get(compra.getAtivo()).remove(compra);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }

    public static void remove(Venda venda){
        try{
            System.out.println("[INFO] REMOVENDO COMPRA: " + mapper.writeValueAsString(venda));
            offerBookVenda.get(venda.getAtivo()).remove(venda);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
    }
}
