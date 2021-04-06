package com.example.agroplazaappmovil.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.Toast;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.ui.perfil.EditarCiudad;

import java.io.ByteArrayOutputStream;
import java.util.Hashtable;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class CalificarPublicacion extends AppCompatActivity {

    String id_publicacion;
    RatingBar puntaje_estrellas;
    EditText campo_descripcion;
    ImageView img_foto;
    Bitmap foto;

    static final int REQUEST_IMAGE_CAPTURE = 1;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_calificar_publicacion);

        Intent intent = getIntent ();
        String id_valoracion = intent.getStringExtra("id_valoracion");
        if (!id_valoracion.isEmpty()) {
            CalificarPublicacion.super.onBackPressed();
        }

        id_publicacion = intent.getStringExtra ("id_publicacion");

        img_foto = findViewById(R.id.img_foto);

        puntaje_estrellas = findViewById(R.id.puntaje_estrellas);
        puntaje_estrellas.setOnRatingBarChangeListener(new RatingBar.OnRatingBarChangeListener() {
            @Override
            public void onRatingChanged(RatingBar ratingBar, float rating, boolean fromUser) {
                ratingBar.setRating((int) rating);
            }
        });

        campo_descripcion = findViewById(R.id.campo_descripcion);

        Button btn_tomar_foto = findViewById(R.id.btn_tomar_foto);
        btn_tomar_foto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                tomarFoto();
            }
        });

        Button btn_guardar_calificacion = findViewById(R.id.btn_guardar_calificacion);
        btn_guardar_calificacion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                enviarCalificacion();
            }
        });

        ImageButton regresar = findViewById(R.id.btn_Atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CalificarPublicacion.super.onBackPressed();
            }
        });
    }

    private void tomarFoto() {
        Intent takePictureIntent = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        if (takePictureIntent.resolveActivity(getPackageManager()) != null) {
            startActivityForResult(takePictureIntent, REQUEST_IMAGE_CAPTURE);
        }
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == REQUEST_IMAGE_CAPTURE && resultCode == RESULT_OK) {
            Bundle extras = data.getExtras();
            foto = (Bitmap) extras.get("data");
            img_foto.setVisibility(View.VISIBLE);
            img_foto.setImageBitmap(foto);
        }
    }

    public String obtenerImagenBase64(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.JPEG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    public void enviarCalificacion() {
        int valoracion = (int) puntaje_estrellas.getRating();
        String descripcion = campo_descripcion.getText().toString();

        if (valoracion > 0 && !descripcion.isEmpty()) {
            SweetAlertDialog pDialog = new SweetAlertDialog(CalificarPublicacion.this, SweetAlertDialog.PROGRESS_TYPE);
            pDialog.getProgressHelper().setBarColor(Color.GREEN);
            pDialog.setTitleText("Subiendo imagen ...");
            pDialog.setCancelable(false);
            pDialog.show();

            String imagen = (foto != null) ? obtenerImagenBase64(foto) : "";

            SharedPreferences persistencia = getSharedPreferences("datos_login", Context.MODE_PRIVATE);
            String id_usuario = persistencia.getString("id", "NaN");

            RequestQueue hilo = Volley.newRequestQueue(getApplicationContext());

            String url = "https://agroplaza.solucionsoftware.co/ModuloPedidos/CalificarPublicacion";
            StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            String[] mensaje = response.split("\"");

                            if (mensaje[1].equalsIgnoreCase("OK##INSERT")) {
                                pDialog.dismiss();
                                new SweetAlertDialog(CalificarPublicacion.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Valoracion Realizada!")
                                        .setContentText("Has calificado la publicacion exitosamente.")
                                        .setConfirmText("Hecho!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                sDialog.dismiss();
                                                CalificarPublicacion.super.onBackPressed();
                                            }
                                        })
                                        .show();
                            } else {
                                pDialog.dismiss();
                                new SweetAlertDialog(CalificarPublicacion.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Ha ocurrido un fallo al guardar la calificacion!")
                                        .show();
                            }
                        }
                    },
                    new Response.ErrorListener() {
                        @Override
                        public void onErrorResponse(VolleyError volleyError) {
                            Toast.makeText(getApplicationContext(), "Error: "+volleyError.getMessage(), Toast.LENGTH_LONG).show();
                            pDialog.dismiss();
                        }
                    }){
                @Override
                protected Map<String, String> getParams() throws AuthFailureError {
                    Map<String,String> params = new Hashtable<String, String>();

                    params.put("imagen", imagen);
                    params.put("id_usuario", id_usuario);
                    params.put("id_publicacion", id_publicacion);
                    params.put("valoracion", String.valueOf(valoracion));
                    params.put("descripcion", descripcion);

                    return params;
                }
            };

            //Agregar solicitud a la cola
            hilo.add(stringRequest);
        } else {
            new SweetAlertDialog(CalificarPublicacion.this, SweetAlertDialog.WARNING_TYPE)
                    .setTitleText("FALTAN DATOS!")
                    .setContentText("Debes dar una valoracion y una descripcion")
                    .show();
        }
    }
}