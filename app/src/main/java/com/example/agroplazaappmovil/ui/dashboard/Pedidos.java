package com.example.agroplazaappmovil.ui.dashboard;

public class Pedidos {
    String numero_pedido;
    String titulo_publicacion;
    String fecha_pedido;
    String estado_pedido;
    String id_publi;

    public Pedidos (String numero_pedido, String titulo_publicacion, String fecha_pedido, String estado_pedido, String id_publi) {
        this.numero_pedido = numero_pedido;
        this.titulo_publicacion = titulo_publicacion;
        this.fecha_pedido = fecha_pedido;
        this.estado_pedido = estado_pedido;
        this.id_publi = id_publi;
    }
}
