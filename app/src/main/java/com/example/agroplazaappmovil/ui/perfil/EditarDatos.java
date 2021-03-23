package com.example.agroplazaappmovil.ui.perfil;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.Login;
import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.RegistroUsuarios;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditarDatos extends AppCompatActivity {

    EditText edit_documento, edit_nombres, edit_apellidos, edit_direccion, edit_telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_datos);

        edit_documento = findViewById(R.id.editar_documento);
        edit_nombres = findViewById(R.id.editar_nombres);
        edit_apellidos = findViewById(R.id.editar_apellidos);
        edit_direccion = findViewById(R.id.editar_direccion);
        edit_telefono = findViewById(R.id.editar_telefono);

        Button regresar = findViewById(R.id.btn_atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarDatos.super.onBackPressed();
            }
        });

        SharedPreferences persistencia = getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String documento = persistencia.getString("documento", "");
        String nombres = persistencia.getString("nombres", "");
        String apellidos = persistencia.getString("apellidos", "");
        String direccion = persistencia.getString("direccion", "");
        String telefono = persistencia.getString("telefono", "");

        edit_documento.setText(documento);
        edit_nombres.setText(nombres);
        edit_apellidos.setText(apellidos);
        edit_direccion.setText(direccion);
        edit_telefono.setText(telefono);

        Button btn_editar = findViewById(R.id.btn_editar_perfil);
        btn_editar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                actualizarDatos(persistencia);
            }
        });
    }

    public void actualizarDatos(SharedPreferences persistencia) {
        String id_perfil = persistencia.getString("id", "0");

        String documento = edit_documento.getText().toString();
        String nombres = edit_nombres.getText().toString();
        String apellidos = edit_apellidos.getText().toString();
        String direccion = edit_direccion.getText().toString();
        String telefono = edit_telefono.getText().toString();

        SweetAlertDialog pDialog = new SweetAlertDialog(EditarDatos.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Espera ...");
        pDialog.setCancelable(false);
        pDialog.show();

        if (!nombres.isEmpty() && !apellidos.isEmpty() && !telefono.isEmpty()) {
            RequestQueue hilo = Volley.newRequestQueue(this);
            String url = "https://agroplaza.solucionsoftware.co/ModuloUsuarios/EditarDatosMovil";

            StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            String[] mensaje = response.split("\"");
                            Toast.makeText(EditarDatos.this, mensaje[1], Toast.LENGTH_LONG).show();

                            if (mensaje[1].equalsIgnoreCase("INVALID##DOCUMENT")) {
                                pDialog.dismiss();
                                new SweetAlertDialog(EditarDatos.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Ya existe!")
                                        .setContentText("El documento ingresado ya esta registrado en el sistema.")
                                        .show();
                            } else if (mensaje[1].equalsIgnoreCase("ERROR##UPDATE")) {
                                pDialog.dismiss();
                                new SweetAlertDialog(EditarDatos.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Error al actualizar los campos!")
                                        .show();
                            } else if (mensaje[1].trim().equalsIgnoreCase("OK##DATA##UPDATE")) {
                                SharedPreferences.Editor editor = persistencia.edit();
                                editor.putString("documento", documento);
                                editor.putString("nombres", nombres);
                                editor.putString("apellidos", apellidos);
                                editor.putString("direccion", direccion);
                                editor.putString("telefono", telefono);

                                editor.commit();

                                pDialog.dismiss();
                                new SweetAlertDialog(EditarDatos.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Actualizacion Correcta!")
                                        .setContentText("Tus datos han sido actualizados.")
                                        .setConfirmText("Hecho!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                EditarDatos.super.onBackPressed();
                                            }
                                        })
                                        .show();
                            }
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
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("id_perfil", id_perfil);
                    parametros.put("documento", documento);
                    parametros.put("nombres", nombres);
                    parametros.put("apellidos", apellidos);
                    parametros.put("direccion", direccion);
                    parametros.put("telefono", telefono);
                    return parametros;
                }
            };
            hilo.add(solicitud);
        } else {
            pDialog.dismiss();
            new SweetAlertDialog(EditarDatos.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("FALTAN DATOS!")
                    .setContentText("Llena por lo menos los campos obligatorios del formulario.")
                    .show();
        }
    }
}