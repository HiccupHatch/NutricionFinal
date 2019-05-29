package com.example.hiccup.nutricion.Models;


public class Alimento {

    private String idAli;
    private String nombre;
    private Integer calorias;

    public String getIdAli() {
        return idAli;
    }

    public void setIdAli(String idAli) {
        this.idAli = idAli;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public Integer getCalorias() {
        return calorias;
    }

    public void setCalorias(Integer calorias) {
        this.calorias = calorias;
    }

}