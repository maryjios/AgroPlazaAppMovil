package com.example.agroplazaappmovil.ui.home;

public class Post_Servicios {
    String id;
    String titulo;
    String precio;
    String descuento;
    String foto;
    String descripcion;
    String envio;

    public Post_Servicios(String titulo, String precio, String envio, String descuento, String foto, String id, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.precio = precio;
        this.descuento = descuento;
        this.foto = foto;
        this.descripcion = descripcion;
        this.envio = envio;
    }
}
