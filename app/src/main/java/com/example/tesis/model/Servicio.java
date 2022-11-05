package com.example.tesis.model;

public class Servicio {
    String nombre;
    String descripcion;
    String iddelcreador;
    String FechaDeCreacion;
    String Photo;

    Double promedio;
    Double votantes;
    Double estrellas;

    public Servicio(){}

    public Servicio(String nombre, String descripcion, String iddelcreador,
                    String fechaDeCreacion , String photo, Double promedio , Double votantes, Double estrellas) {
        this.nombre = nombre;
        this.descripcion = descripcion;
        this.iddelcreador = iddelcreador;
        this.FechaDeCreacion = fechaDeCreacion;
        this.Photo = photo;
        this.promedio = promedio;
        this.votantes = votantes;
        this.estrellas = estrellas;
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

    public Double getPromedio() {
        return promedio;
    }

    public void setPromedio(Double promedio) {
        this.promedio = promedio;
    }

    public Double getVotantes() {
        return votantes;
    }

    public void setVotantes(Double votantes) {
        this.votantes = votantes;
    }

    public Double getEstrellas() {return estrellas;   }

    public void setEstrellas(Double estrellas) { this.estrellas = estrellas; }


}
