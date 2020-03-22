package operators;

import operations.Transaction;

import java.util.ArrayList;

public class Transactions {

    static ArrayList<Transaction> transactions = new ArrayList<>();

    public static ArrayList<Transaction> getTransactions() {
        return transactions;
    }

    public static void add(Transaction transaction){
        transactions.add(transaction);
    }

}
