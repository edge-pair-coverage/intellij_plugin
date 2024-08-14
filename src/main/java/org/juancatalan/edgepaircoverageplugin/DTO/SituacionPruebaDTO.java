package org.juancatalan.edgepaircoverageplugin.DTO;

import java.util.Objects;

public class SituacionPruebaDTO {
    private String nodoInicio;
    private String nodoMedio;
    private String nodoFinal;
    private String aristaInicioMedio;
    private String aristaMedioFinal;

    public String getNodoInicio() {
        return nodoInicio;
    }

    public void setNodoInicio(String nodoInicio) {
        this.nodoInicio = nodoInicio;
    }

    public String getNodoMedio() {
        return nodoMedio;
    }

    public void setNodoMedio(String nodoMedio) {
        this.nodoMedio = nodoMedio;
    }

    public String getNodoFinal() {
        return nodoFinal;
    }

    public void setNodoFinal(String nodoFinal) {
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

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof SituacionPruebaDTO that)) return false;
        return Objects.equals(nodoInicio, that.nodoInicio) && Objects.equals(nodoMedio, that.nodoMedio) && Objects.equals(nodoFinal, that.nodoFinal) && Objects.equals(aristaInicioMedio, that.aristaInicioMedio) && Objects.equals(aristaMedioFinal, that.aristaMedioFinal);
    }

    @Override
    public int hashCode() {
        return Objects.hash(nodoInicio, nodoMedio, nodoFinal, aristaInicioMedio, aristaMedioFinal);
    }
}
