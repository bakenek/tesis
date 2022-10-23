package com.example.tesis.model;

public class Servicio {
    String nombre;
    String descripcion;
    String iddelcreador;
    String FechaDeCreacion;
    String Photo;

    public Servicio(){}

    public Servicio(String nombre, String descripcion, String iddelcreador, String fechaDeCreacion , String photo) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.iddelcreador = iddelcreador;
        this.FechaDeCreacion = fechaDeCreacion;
        this.Photo = photo;
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

    public String getPhoto() {return Photo;}

    public void setPhoto(String photo) {Photo = photo;}

}
