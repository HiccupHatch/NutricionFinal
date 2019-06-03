package com.example.hiccup.nutricion.Models;

import java.util.Objects;

public class Caloria {

    private String dniC;
    private String fecha;
    private String tipoComida;
    private String codAli;
    private Integer cantidad;

    public String getDniC() {
        return dniC;
    }

    public void setDniC(String dniC) {
        this.dniC = dniC;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getTipoComida() {
        return tipoComida;
    }

    public void setTipoComida(String tipoComida) {
        this.tipoComida = tipoComida;
    }

    public String getCodigoAlimento() {
        return codAli;
    }

    public void setCodigoAlimento(String codAli) {
        this.codAli = codAli;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Caloria caloria = (Caloria) o;
        return Objects.equals(dniC, caloria.dniC) &&
                Objects.equals(fecha, caloria.fecha) &&
                Objects.equals(tipoComida, caloria.tipoComida);
    }

    @Override
    public int hashCode() {

        return Objects.hash(dniC, fecha, tipoComida);
    }
}