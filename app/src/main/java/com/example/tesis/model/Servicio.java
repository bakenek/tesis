package com.example.tesis.model;

public class Servicio {
    String nombre,descripcion,iddelcreador,  FechaDeCreacion;
    public Servicio(){}


    public Servicio(String nombre, String descripcion, String iddelcreador, String fechaDeCreacion) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.iddelcreador = iddelcreador;
        this.FechaDeCreacion = fechaDeCreacion;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getDescripcion() {
        return descripcion;
    }

    public void setDescripcion(String descripcion) {
        this.descripcion = descripcion;
    }

    public String getIddelcreador() {
        return iddelcreador;
    }

    public void setIddelcreador(String iddelcreador) {
        this.iddelcreador = iddelcreador;
    }

    public String getFechaDeCreacion() {
        return FechaDeCreacion;
    }

    public void setFechaDeCreacion(String fechaDeCreacion) {
        FechaDeCreacion = fechaDeCreacion;
    }
}
