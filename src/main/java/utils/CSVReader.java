package utils;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;


public class CSVReader {

    private  ArrayList<String> codigos = new ArrayList<String>();

    public CSVReader() {
        readcsv();
    }

    public ArrayList<String> getCodigos() {
        return codigos;
    }

    public void setCodigos(ArrayList<String> codigos) {
        this.codigos = codigos;
    }

    public void readcsv() {

        String csvFile = "1688438_acoes_bovespa.csv";
        String line = "";
        String cvsSplitBy = ";";

        try (BufferedReader br = new BufferedReader(new FileReader(csvFile, StandardCharsets.UTF_8))) {
            br.readLine();
            while ((line = br.readLine()) != null) {

                String[] campos = line.split(cvsSplitBy);

                this.codigos.add(campos[0]);

            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
