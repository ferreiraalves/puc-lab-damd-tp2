package operations;

public class Venda {
    private int quant;
    private float val;
    private String corretora;

    public Venda(int quant, float val, String corretora) {
        this.quant = quant;
        this.val = val;
        this.corretora = corretora;
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
