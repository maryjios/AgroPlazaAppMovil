package com.example.agroplazaappmovil.ui.home;

public class PreguntasRespuestas {
    String nombre_cliente;
    String pregunta_cliente;
    String fecha_pregunta_cliente;
    String respuesta_vendedor;
    String fecha_respuesta_vendedor;

    public PreguntasRespuestas (String nombre_cliente, String pregunta_cliente, String fecha_pregunta_cliente, String respuesta_vendedor, String fecha_respuesta_vendedor) {
        this.nombre_cliente = nombre_cliente;
        this.pregunta_cliente = pregunta_cliente;
        this.fecha_pregunta_cliente = fecha_pregunta_cliente;
        this.respuesta_vendedor = respuesta_vendedor;
        this.fecha_respuesta_vendedor = fecha_respuesta_vendedor;
    }
}
