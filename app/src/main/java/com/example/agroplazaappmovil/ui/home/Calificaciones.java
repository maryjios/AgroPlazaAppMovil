package com.example.agroplazaappmovil.ui.home;

public class Calificaciones {
    String id;
    String nombre_usuario;
    String total_estrellas;
    String fecha_calificacion;
    String descripcion_calificacion;
    String foto_evidencia;


    public Calificaciones (String id, String nombre_usuario,String total_estrellas, String fecha_calificacion, String descripcion_calificacion, String foto_evidencia) {
        this.id = id;
        this.nombre_usuario = nombre_usuario;
        this.total_estrellas = total_estrellas;
        this.fecha_calificacion = fecha_calificacion;
        this.descripcion_calificacion = descripcion_calificacion;
        this.foto_evidencia = foto_evidencia;

    }
}
