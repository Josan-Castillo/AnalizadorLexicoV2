package fes.aragon.herramientas;

public class ErrorLexico extends Exception {
    private String error;
    private int numeroColumna;
    private int numeroLinea;
    
    public ErrorLexico(String error){
        super(error);
    }

    public ErrorLexico(String error, int numeroColumna, int numeroLinea) {
        super(error);
        this.error = error;
        this.numeroColumna = numeroColumna;
        this.numeroLinea = numeroLinea;
    }

    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public int getNumeroColumna() {
        return numeroColumna;
    }

    public void setNumeroColumna(int numeroColumna) {
        this.numeroColumna = numeroColumna;
    }

    public int getNumeroLinea() {
        return numeroLinea;
    }

    public void setNumeroLinea(int numeroLinea) {
        this.numeroLinea = numeroLinea;
    }
}
