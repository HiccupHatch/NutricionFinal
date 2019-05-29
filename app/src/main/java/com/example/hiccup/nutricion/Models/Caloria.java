package com.example.hiccup.nutricion.Models;

public class Caloria {

    private String dni;
    private String fecha;
    private String tipoComida;
    private Integer codAli;
    private Integer cantidad;

    public String getDni() {
        return dni;
    }

    public void setDni(String dni) {
        this.dni = dni;
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

    public Integer getCodigoAlimento() {
        return codAli;
    }

    public void setCodigoAlimento(Integer codAli) {
        this.codAli = codAli;
    }

    public Integer getCantidad() {
        return cantidad;
    }

    public void setCantidad(Integer cantidad) {
        this.cantidad = cantidad;
    }

}