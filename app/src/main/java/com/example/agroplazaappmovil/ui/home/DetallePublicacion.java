package com.example.agroplazaappmovil.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
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
import com.example.agroplazaappmovil.ui.dashboard.Chat_Activity;

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
    String id_publicacion;

    ArrayList<PreguntasRespuestas> listaPreguntasRespuestas;
    RecyclerView recycler;
    AdapterPreguntasRespuestas adapter;

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

        Float valor_precio = Float.parseFloat (intent.getStringExtra ("precio"));
        int precio_int = Math.round (valor_precio);
        titulo.setText (intent.getStringExtra ("titulo"));
        precio.setText ("$"+precio_int);
        unidad.setText ("x " + intent.getStringExtra ("unidad"));
        stock.setText (" STOCK: "+intent.getStringExtra ("stock")+intent.getStringExtra ("unidad"));
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
                intent_compra.putExtra ("id_publicacion", intent.getStringExtra("id"));

                if (!intent.getStringExtra("stock").equals("null"))
                    intent_compra.putExtra ("stock", intent.getStringExtra("stock"));

                intent_compra.putExtra ("precio", intent.getStringExtra("precio"));
                intent_compra.putExtra ("descuento", intent.getStringExtra("descuento"));
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
        Button btn_chat = findViewById (R.id.btn_chat);
        btn_chat.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intencion=new Intent(getApplicationContext(), Chat_Activity.class);
                startActivity(intencion);

            }
        });
       consultarPreguntasRespuestas (id_publicacion);
    }


    public void consultarPreguntasRespuestas (String id_publicacion) {

        // Recycler preguntas, respuestas
        recycler = findViewById (R.id.recycler_preguntas_respuestas);
        recycler.setLayoutManager (new LinearLayoutManager (getApplicationContext (), LinearLayoutManager.VERTICAL,false));
        

        RequestQueue hilo = Volley.newRequestQueue (getApplicationContext ());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/ConsultarPreguntas?publicacion=" + id_publicacion;

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                listaPreguntasRespuestas = new ArrayList<> ();


                JSONArray lista_preguntas_respuestas = response.optJSONArray ("registros_preguntas");
                try {
                    Log.i ("DAtos", lista_preguntas_respuestas.toString ());

                    if (lista_preguntas_respuestas.length ()<=0){
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


