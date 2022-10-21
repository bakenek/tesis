package com.example.tesis.model;

public class notificaciones {
    String titulo;
    String cuerpo;
    String idnotificado;


    public notificaciones(){}

    public notificaciones(String titulo, String cuerpo, String idnotificado) {
        this.titulo = titulo;
        this.cuerpo = cuerpo;
        this.idnotificado = idnotificado;
    }

    public String getTitulo() {return titulo;    }

    public void setTitulo(String titulo) {
        this.titulo = titulo;
    }

    public String getCuerpo() {return cuerpo;    }

    public void setCuerpo(String cuerpo) {
        this.cuerpo = cuerpo;
    }

    public String getIdnotificado() {
        return idnotificado;
    }

    public void setIdnotificado(String idnotificado) {
        this.idnotificado = idnotificado;
    }


}
