package com.example.agroplazaappmovil.ui.home;

public class Descuentos {
    String id;
    String titulo;
    String precio;
    String envio;
    String descuento;
    String foto;
    String descripcion;

    public Descuentos (String titulo, String precio, String envio, String descuento, String foto, String id, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.precio = precio;
        this.envio = envio;
        this.descuento = descuento;
        this.foto = foto;
        this.descripcion = descripcion;
    }
}
