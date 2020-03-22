package operations;

import com.fasterxml.jackson.annotation.JsonIgnore;

public class Venda {
    private int quant;
    private float val;
    private String corretora;
    @JsonIgnore
    private String ativo;


    public Venda(int quant, float val, String corretora, String ativo) {
        this.quant = quant;
        this.val = val;
        this.corretora = corretora;
        this.ativo = ativo;
    }

    public String getAtivo() {
        return ativo;
    }

    public void setAtivo(String ativo) {
        this.ativo = ativo;
    }

    public int getQuant() {
        return quant;
    }

    public void setQuant(int quant) {
        this.quant = quant;
    }

    public float getVal() {
        return val;
    }

    public void setVal(float val) {
        this.val = val;
    }

    public String getCorretora() {
        return corretora;
    }

    public void setCorretora(String corretora) {
        this.corretora = corretora;
    }
}
