package com.example.agroplazaappmovil.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.ImageSlider;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.agroplazaappmovil.Login;
import com.example.agroplazaappmovil.Principal;
import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.RegistroUsuarios;
import com.example.agroplazaappmovil.ui.perfil.EditarCiudad;
import com.example.agroplazaappmovil.ui.perfil.EditarDatos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetallePublicacion extends AppCompatActivity {

    TextView titulo, precio, descripcion, unidad, stock;
    EditText pregunta;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_publicacion);

        Intent intent = getIntent();
        titulo = findViewById (R.id.titulo_detalle);
        precio = findViewById (R.id.precio_detalle);
        descripcion = findViewById (R.id.descripcion_detalle);
        unidad = findViewById (R.id.unidad_detalle);
        stock = findViewById (R.id.stock_detalle);

        Float valor_precio = Float.parseFloat (intent.getStringExtra("precio"));
        int precio_int =  Math.round (valor_precio);
        titulo.setText (intent.getStringExtra("titulo"));
        precio.setText (Integer.toString(precio_int));
        unidad.setText (" x " + intent.getStringExtra("unidad"));
        stock.setText (intent.getStringExtra("stock"));
        descripcion.setText (intent.getStringExtra("descripcion"));

        ImageSlider imagenes = findViewById (R.id.imagenes_detalle);
        List<SlideModel> imagenes_publicacion = new ArrayList<>();

        String id_publicacion = intent.getStringExtra("id");
        RequestQueue hilo = Volley.newRequestQueue (getApplicationContext ());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/getImagenesPublicacion?id="+id_publicacion;

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                JSONArray imagenesPublicacion = response.optJSONArray ("imagenes");
                try {
                    for (int i = 0; i < imagenesPublicacion.length (); i++) {

                        JSONObject dato = imagenesPublicacion.getJSONObject (i);
                        String la_imagen = dato.getString ("imagen");
                        imagenes_publicacion.add (new SlideModel("https://agroplaza.solucionsoftware.co/public/dist/img/publicaciones/publicacion"+id_publicacion+"/"+la_imagen));
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
        Button regresar = findViewById(R.id.btn_atras_detalle);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetallePublicacion.super.onBackPressed();
            }
        });

        Button btn_generarPedido = findViewById(R.id.btn_comprar);
        btn_generarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent_compra = new Intent (getApplicationContext (), GenerarPedido.class);
                /* intent_compra.putExtra ("id_publicacion", intent.getStringExtra("id"));
                intent_compra.putExtra ("stock", intent.getStringExtra("stock"));
                intent_compra.putExtra ("precio", intent.getStringExtra("precio"));
                intent_compra.putExtra ("descuento", intent.getStringExtra("descuento")); */
                startActivity (intent_compra);
            }
        });

        pregunta = findViewById(R.id.editPregunta);
        pregunta.setOnKeyListener (new View.OnKeyListener () {
            @Override
            public boolean onKey (View v, int keyCode, KeyEvent event) {
                Log.i ("Tecla", ""+keyCode);
                return false;
            }
        });
       /* Button preguntar = findViewById(R.id.btnPreguntar);
        btn_generarPedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                registrarPregunta();
            }
        }); */

    }

    /* public void registrarPregunta(){

        String valor_pregunta = pregunta.getText().toString();

        RequestQueue hilo = Volley.newRequestQueue(this);
        String url = "https://agroplaza.solucionsoftware.co/ModuloUsuarios/InsertarPregunta";

        StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                       Toast.makeText (getApplicationContext (), "Registrada", Toast.LENGTH_LONG).show ();
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Codigo de error del servidor
                        // Se ejecuta cuando no llega el tipo solicitado String.
                        Toast.makeText(getApplicationContext(), "Error Servidor: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        if (error.getMessage() != null) {
                            Log.i("Error Servidor: ", error.getMessage());
                        } else {
                            Log.i("Error Servidor: ", "Error desconocido");
                        }
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<String, String> ();
                parametros.put("email", valor_pregunta);

                return parametros;
            }
        };
        hilo.add(solicitud);

    } */
}


