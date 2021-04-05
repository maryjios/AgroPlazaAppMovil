package com.example.agroplazaappmovil.ui.dashboard;

public class Mensajes {

    protected int id;
    protected int id_pedido;
    protected int id_usuario;
    protected String mensaje;
    protected String fecha;
    protected String nombre_remitente;

    public Mensajes(int id_pedido, int id_usuario, String mensaje, String fecha, String nombre_remitente) {
        this.id_pedido = id_pedido;
        this.id_usuario = id_usuario;
        this.mensaje = mensaje;
        this.fecha = fecha;
        this.nombre_remitente = nombre_remitente;
    }

    public String getNombreRemitente() {
        return nombre_remitente;
    }

    public int getId_usuario() {
        return id_usuario;
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

    public int getId() {
        return id_pedido;
    }

    public void setId(int id_pedido) {
        this.id_pedido = id_pedido;
    }

}
