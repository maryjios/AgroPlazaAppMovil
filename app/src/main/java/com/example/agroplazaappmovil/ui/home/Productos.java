package com.example.agroplazaappmovil.ui.home;

import java.io.Serializable;

public class Productos implements Serializable {
    String id;
    String titulo;
    String precio;
    String envio;
    String descuento;
    String foto;
    String descripcion;


    public Productos (String titulo, String precio, String envio, String descuento, String foto, String id, String descripcion) {
        this.id = id;
        this.titulo = titulo;
        this.precio = precio;
        this.envio = envio;
        this.descuento = descuento;
        this.foto = foto;
        this.descripcion = descripcion;
    }


}
