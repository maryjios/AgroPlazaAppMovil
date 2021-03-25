package com.example.agroplazaappmovil.ui.home;

public class Publicaciones{
    String id;
    String titulo;
    String precio;
    String envio;
    String unidad;
    String descuento;
    String foto;
    String descripcion;
    String stock;

    public Publicaciones (String titulo, String precio, String envio, String descuento, String foto, String id, String descripcion, String unidad, String stock) {
        this.id = id;
        this.titulo = titulo;
        this.precio = precio;
        this.envio = envio;
        this.descuento = descuento;
        this.unidad = unidad;
        this.foto = foto;
        this.descripcion = descripcion;
        this.stock = stock;
    }
}
