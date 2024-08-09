package org.juancatalan.edgepaircoverageplugin.DTO;

import java.util.List;

    public class MethodReportDTO {
        private String nombre;
        private String grafo;
        private String grafoImagen;
        private int caminosImposibles;
        private List<SituacionPruebaDTO> caminos;
        private List<SituacionPruebaDTO> caminosCubiertos;
        private double porcentajeCobertura;

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getGrafo() {
        return grafo;
    }

    public void setGrafo(String grafo) {
        this.grafo = grafo;
    }

    public String getGrafoImagen() {
        return grafoImagen;
    }

    public void setGrafoImagen(String grafoImagen) {
        this.grafoImagen = grafoImagen;
    }

    public int getCaminosImposibles() {
        return caminosImposibles;
    }

    public void setCaminosImposibles(int caminosImposibles) {
        this.caminosImposibles = caminosImposibles;
    }

    public List<SituacionPruebaDTO> getCaminos() {
        return caminos;
    }

    public void setCaminos(List<SituacionPruebaDTO> caminos) {
        this.caminos = caminos;
    }

    public List<SituacionPruebaDTO> getCaminosCubiertos() {
        return caminosCubiertos;
    }

    public void setCaminosCubiertos(List<SituacionPruebaDTO> caminosCubiertos) {
        this.caminosCubiertos = caminosCubiertos;
    }

    public double getPorcentajeCobertura() {
        return porcentajeCobertura;
    }

    public void setPorcentajeCobertura(double porcentajeCobertura) {
        this.porcentajeCobertura = porcentajeCobertura;
    }

    @Override
    public String toString() {
        return "MethodReportDTO{" +
                "nombre='" + nombre + '\'' +
                ", grafo='" + grafo + '\'' +
                ", grafoImagen='" + grafoImagen + '\'' +
                ", caminosImposibles=" + caminosImposibles +
                ", caminos=" + caminos +
                ", caminosCubiertos=" + caminosCubiertos +
                ", porcentajeCobertura=" + porcentajeCobertura +
                '}';
    }
}
