package org.juancatalan.edgepaircoverageplugin.DTO;

public class SituacionPruebaDTO {
    private int nodoInicio;
    private int nodoMedio;
    private int nodoFinal;
    private String aristaInicioMedio;
    private String aristaMedioFinal;

    public int getNodoInicio() {
        return nodoInicio;
    }

    public void setNodoInicio(int nodoInicio) {
        this.nodoInicio = nodoInicio;
    }

    public int getNodoMedio() {
        return nodoMedio;
    }

    public void setNodoMedio(int nodoMedio) {
        this.nodoMedio = nodoMedio;
    }

    public int getNodoFinal() {
        return nodoFinal;
    }

    public void setNodoFinal(int nodoFinal) {
        this.nodoFinal = nodoFinal;
    }

    public String getAristaInicioMedio() {
        return aristaInicioMedio;
    }

    public void setAristaInicioMedio(String aristaInicioMedio) {
        this.aristaInicioMedio = aristaInicioMedio;
    }

    public String getAristaMedioFinal() {
        return aristaMedioFinal;
    }

    public void setAristaMedioFinal(String aristaMedioFinal) {
        this.aristaMedioFinal = aristaMedioFinal;
    }

    @Override
    public String toString() {
        return "SituacionPruebaDTO{" +
                "nodoInicio=" + nodoInicio +
                ", nodoMedio=" + nodoMedio +
                ", nodoFinal=" + nodoFinal +
                ", aristaInicioMedio='" + aristaInicioMedio + '\'' +
                ", aristaMedioFinal='" + aristaMedioFinal + '\'' +
                '}';
    }
}
