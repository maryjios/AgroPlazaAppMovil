package com.example.agroplazaappmovil.ui.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.R;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class EditarEmail extends AppCompatActivity {

    EditText edit_correo, confirm_correo, correo_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_email);

        SharedPreferences persistencia = getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String correo = persistencia.getString("email", "NaN");

        TextView correo_actual = findViewById(R.id.correo_actual);
        correo_actual.setText(correo);

        Button regresar = findViewById(R.id.btn_atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarEmail.super.onBackPressed();
            }
        });

        edit_correo = findViewById(R.id.editar_correo);
        confirm_correo = findViewById(R.id.confirm_correo);
        correo_password = findViewById(R.id.correo_password);

        Button btn_editar_correo = findViewById(R.id.btn_editar_correo);
        btn_editar_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarCorreo(persistencia);
            }
        });
    }

    public void cambiarCorreo(SharedPreferences persistencia) {
        String id_perfil = persistencia.getString("id", "0");

        String correo = edit_correo.getText().toString();
        String c_correo = confirm_correo.getText().toString();
        String password = correo_password.getText().toString();

        SweetAlertDialog pDialog = new SweetAlertDialog(EditarEmail.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Espera ...");
        pDialog.setCancelable(false);
        pDialog.show();

        if (!correo.isEmpty() && !c_correo.isEmpty() && !password.isEmpty()) {
            if (correo.equals(c_correo)) {
                RequestQueue hilo = Volley.newRequestQueue(this);
                String url = "https://agroplaza.solucionsoftware.co/ModuloUsuarios/EditarEmailMovil";

                StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                String[] mensaje = response.split("\"");    

                                if (mensaje[1].equalsIgnoreCase("INVALID##EMAIL")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(EditarEmail.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Ya existe!")
                                            .setContentText("El correo ingresado ya esta registrado en el sistema.")
                                            .show();

                                    correo_password.setText("");
                                } else if (mensaje[1].equalsIgnoreCase("INVALID##PASSWORD")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(EditarEmail.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Contraseña incorrecta")
                                            .setContentText("Debe ingresar la contraseña de su perfil.")
                                            .show();

                                    correo_password.setText("");
                                } else if (mensaje[1].equalsIgnoreCase("ERROR##UPDATE")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(EditarEmail.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Error al actualizar el correo!")
                                            .show();

                                    correo_password.setText("");
                                } else if (mensaje[1].trim().equalsIgnoreCase("OK##EMAIL##UPDATE")) {
                                    SharedPreferences.Editor editor = persistencia.edit();
                                    editor.putString("email", correo);

                                    editor.commit();

                                    pDialog.dismiss();
                                    new SweetAlertDialog(EditarEmail.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Actualizacion Correcta!")
                                            .setContentText("Tu correo ha sido actualizado.")
                                            .setConfirmText("Hecho!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    EditarEmail.super.onBackPressed();
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
                        parametros.put("email", correo);
                        parametros.put("password", password);
                        return parametros;
                    }
                };
                hilo.add(solicitud);
            } else {
                pDialog.dismiss();
                new SweetAlertDialog(EditarEmail.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("LOS CORREOS NO COINCIDEN!")
                        .setContentText("Ambos correos no coinciden. Debes verificar que sean iguales.")
                        .show();
            }
        } else {
            pDialog.dismiss();
            new SweetAlertDialog(EditarEmail.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("FALTAN DATOS!")
                    .setContentText("Llena todos los datos del formulario.")
                    .show();
        }
    }
}