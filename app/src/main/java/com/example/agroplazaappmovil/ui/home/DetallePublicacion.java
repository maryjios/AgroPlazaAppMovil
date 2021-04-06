package com.example.agroplazaappmovil.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.agroplazaappmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


public class DetallePublicacion extends AppCompatActivity {

    TextView titulo, precio, descripcion, unidad, stock;
    EditText pregunta;
    String id_publicacion, tipo_publicacion;

    ArrayList<PreguntasRespuestas> listaPreguntasRespuestas;
    RecyclerView recycler;
    AdapterPreguntasRespuestas adapter;

    ArrayList<Calificaciones> listaCalificaciones;
    RecyclerView recycler_calificaciones;
    AdapterCalificaciones adapter_calificaciones;


    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detalle_publicacion);

        Intent intent = getIntent ();
        titulo = findViewById (R.id.titulo_detalle);
        precio = findViewById (R.id.precio_detalle);
        descripcion = findViewById (R.id.descripcion_detalle);
        unidad = findViewById (R.id.unidad_detalle);
        stock = findViewById (R.id.stock_detalle);
        tipo_publicacion = intent.getStringExtra("tipo_publicacion");

        Float valor_precio = Float.parseFloat (intent.getStringExtra ("precio"));
        int precio_int = Math.round (valor_precio);
        titulo.setText (intent.getStringExtra ("titulo"));
        precio.setText ("$" + precio_int);

        if (!tipo_publicacion.equals("SERVICIO")) {
            unidad.setText ("x " + intent.getStringExtra("valor_unidad") + intent.getStringExtra ("unidad"));
            stock.setText (" STOCK: " + intent.getStringExtra ("stock") + intent.getStringExtra ("unidad"));
        } else {
            unidad.setVisibility(View.INVISIBLE);
            stock.setVisibility(View.INVISIBLE);
        }

        descripcion.setText (intent.getStringExtra ("descripcion"));
        id_publicacion = intent.getStringExtra ("id");

        ImageSlider imagenes = findViewById (R.id.imagenes_detalle);
        List<SlideModel> imagenes_publicacion = new ArrayList<> ();

        String id_publicacion = intent.getStringExtra ("id");
        RequestQueue hilo = Volley.newRequestQueue (getApplicationContext ());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/getImagenesPublicacion?id=" + id_publicacion;

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                JSONArray imagenesPublicacion = response.optJSONArray ("imagenes");
                try {
                    for (int i = 0; i < imagenesPublicacion.length (); i++) {

                        JSONObject dato = imagenesPublicacion.getJSONObject (i);
                        String la_imagen = dato.getString ("imagen");
                        imagenes_publicacion.add (new SlideModel ("https://agroplaza.solucionsoftware.co/public/dist/img/publicaciones/publicacion" + id_publicacion + "/" + la_imagen));
                    }
                    imagenes.setImageList (imagenes_publicacion, true);

                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse (VolleyError error) {

            }
        });

        hilo.add (solicitud);
        Button regresar = findViewById (R.id.btn_atras_detalle);
        regresar.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                DetallePublicacion.super.onBackPressed ();
            }
        });

        Button btn_generarPedido = findViewById (R.id.btn_comprar);
        btn_generarPedido.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent_compra = new Intent (getApplicationContext (), GenerarPedido.class);
                intent_compra.putExtra ("id_publicacion", intent.getStringExtra ("id"));

                if (!intent.getStringExtra ("stock").equals ("null"))
                    intent_compra.putExtra ("stock", intent.getStringExtra ("stock"));

                intent_compra.putExtra ("precio", intent.getStringExtra("precio"));
                intent_compra.putExtra ("descuento", intent.getStringExtra("descuento"));
                intent_compra.putExtra ("valor_unidad", intent.getStringExtra("valor_unidad"));
                intent_compra.putExtra("tipo_publicacion", tipo_publicacion);
                startActivity (intent_compra);
            }
        });

        pregunta = findViewById (R.id.editPregunta);

        Button botonEnviar = findViewById (R.id.btnPreguntar);
        botonEnviar.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                registrarPregunta ();
            }
        });

        consultarPreguntasRespuestas (id_publicacion);
        llenarContenedorCalificaciones ();
    }

    public void llenarContenedorCalificaciones () {
        // Recycler preguntas, respuestas
        recycler_calificaciones = findViewById (R.id.recycler_calificaciones_publicacion);
        recycler_calificaciones.setLayoutManager (new GridLayoutManager (getApplicationContext (), 1));


        listaCalificaciones = new ArrayList<> ();

        String id = "1";
        String nombre_usuario = "Usuario 1";
        String total_estrellas = "5";
        String fecha_calificacion = "Ayer";
        String descripcion_calificacion = "Muy bien";
        String foto_evidencia = "foto.png";



        Calificaciones pub = new Calificaciones (id, nombre_usuario, total_estrellas, fecha_calificacion, descripcion_calificacion, foto_evidencia);
        listaCalificaciones.add (pub);


        adapter_calificaciones = new AdapterCalificaciones (listaCalificaciones);
        recycler_calificaciones.setAdapter (adapter_calificaciones);
    }

    public void consultarPreguntasRespuestas (String id_publicacion) {

        // Recycler preguntas, respuestas
        recycler = findViewById (R.id.recycler_preguntas_respuestas);
        recycler.setLayoutManager (new LinearLayoutManager (getApplicationContext (), LinearLayoutManager.VERTICAL, false));


        RequestQueue hilo = Volley.newRequestQueue (getApplicationContext ());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/ConsultarPreguntas?publicacion=" + id_publicacion;

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                listaPreguntasRespuestas = new ArrayList<> ();


                JSONArray lista_preguntas_respuestas = response.optJSONArray ("registros_preguntas");
                try {
                    Log.i ("DAtos", lista_preguntas_respuestas.toString ());

                    if (lista_preguntas_respuestas.length () <= 0) {
                        TextView conten_vacio = findViewById (R.id.sin_pregunta);
                        conten_vacio.setVisibility (View.VISIBLE);
                        conten_vacio.setText ("No hay ninguna pregunta");
                    }
                    for (int i = 0; i < lista_preguntas_respuestas.length (); i++) {

                        JSONObject temp = lista_preguntas_respuestas.getJSONObject (i);
                        String pregunta_cliente = temp.getString ("pregunta_c");
                        String fecha_pregunta_cliente = temp.getString ("fecha_pregunta_c");
                        String respuesta_vendedor = temp.getString ("respuesta_v");
                        String fecha_respuesta_vendedor = temp.getString ("fecha_pregunta_v");

                        PreguntasRespuestas pub = new PreguntasRespuestas (pregunta_cliente, fecha_pregunta_cliente, respuesta_vendedor, fecha_respuesta_vendedor);
                        listaPreguntasRespuestas.add (pub);

                    }

                    adapter = new AdapterPreguntasRespuestas (listaPreguntasRespuestas);
                    recycler.setAdapter (adapter);

                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse (VolleyError error) {
            }
        });
        hilo.add (solicitud);
    }

    public void registrarPregunta () {

        SharedPreferences persistencia = getApplicationContext ().getSharedPreferences ("datos_login", Context.MODE_PRIVATE);
        String id_user = persistencia.getString ("id", "");

        String valor_pregunta = pregunta.getText ().toString ();
        Log.i ("Eledido", "Pedido");
        RequestQueue hilo = Volley.newRequestQueue (this);
        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/RegistrarPregunta";

        StringRequest solicitud = new StringRequest (Request.Method.POST, url,
                new Response.Listener<String> () {
                    @Override
                    public void onResponse (String response) {

                        Toast.makeText (getApplicationContext (), "Registrada" + response, Toast.LENGTH_LONG).show ();
                    }
                },
                new Response.ErrorListener () {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                        // Codigo de error del servidor
                        // Se ejecuta cuando no llega el tipo solicitado String.
                        Toast.makeText (getApplicationContext (), "Error Servidor: " + error.getMessage (), Toast.LENGTH_LONG).show ();
                        if (error.getMessage () != null) {
                            Log.i ("Error Servidor: ", error.getMessage ());
                        } else {
                            Log.i ("Error Servidor: ", "Error desconocido");
                        }
                    }
                }) {
            protected Map<String, String> getParams () {
                Map<String, String> parametros = new HashMap<String, String> ();
                parametros.put ("id_publicacion", id_publicacion);
                parametros.put ("id_usuario", id_user);
                parametros.put ("pregunta", valor_pregunta);


                return parametros;
            }
        };

        hilo.add (solicitud);

    }
}


