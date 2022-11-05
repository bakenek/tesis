package com.example.tesis.model;

public class usuario {

    String Photo;
    String nombre;
    String correo;
    String contacto;

    Double estrellas;

    public usuario(){}

    public usuario(String photo, String nombre, String correo, String contacto, Double estrellas) {
        this.Photo = photo;
        this.nombre = nombre;
        this.correo = correo;
        this.contacto = contacto;
        this.estrellas = estrellas;
    }

    public String getPhoto() {
        return Photo;
    }

    public void setPhoto(String photo) {
        Photo = photo;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public String getCorreo() {
        return correo;
    }

    public void setCorreo(String correo) {
        this.correo = correo;
    }

    public String getContacto() {
        return contacto;
    }

    public void setContacto(String contacto) {
        this.contacto = contacto;
    }

    public Double getEstrellas() {
        return estrellas;
    }

    public void setEstrellas(Double estrellas) {
        this.estrellas = estrellas;
    }
}
