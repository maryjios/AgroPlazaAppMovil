package com.example.agroplazaappmovil.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
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
import com.example.agroplazaappmovil.ui.perfil.EditarCiudad;
import com.example.agroplazaappmovil.ui.perfil.EditarDatos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;
import java.util.List;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetallePublicacion extends AppCompatActivity {

    TextView titulo, precio;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_detalle_publicacion);

        Intent intent = getIntent();
        titulo = findViewById (R.id.titulo_detalle);
        precio = findViewById (R.id.precio_detalle);
        titulo.setText (intent.getStringExtra("titulo"));
        precio.setText (intent.getStringExtra("descripcion"));

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
    }
}


