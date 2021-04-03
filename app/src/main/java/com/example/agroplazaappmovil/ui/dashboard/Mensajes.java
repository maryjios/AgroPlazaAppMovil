package com.example.agroplazaappmovil.ui.dashboard;

public class Mensajes {

    protected int id;
    protected String mensaje;
    protected String fecha;
    protected String nombre_remitente;

    public Mensajes(int id, String mensaje, String fecha, String nombre_remitente) {
        this.id = id;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.nombre_remitente = nombre_remitente;
    }

    public String getNombreRemitente() {
        return nombre_remitente;
    }

    public void setNombreRemitente(String nombre_remitente) {
        this.nombre_remitente = nombre_remitente;
    }

    public String getMensaje() {
        return mensaje;
    }

    public void setMensaje(String mensaje) {
        this.mensaje = mensaje;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

}
