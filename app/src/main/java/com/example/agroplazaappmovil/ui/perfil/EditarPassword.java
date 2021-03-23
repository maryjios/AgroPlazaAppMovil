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

public class EditarPassword extends AppCompatActivity {

    EditText password_actual, edit_password, confirm_edit_password;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_password);

        SharedPreferences persistencia = getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String id_perfil = persistencia.getString("id", "0");

        Button regresar = findViewById(R.id.btn_atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarPassword.super.onBackPressed();
            }
        });

        password_actual = findViewById(R.id.password_actual);
        edit_password = findViewById(R.id.edit_password);
        confirm_edit_password = findViewById(R.id.confirm_edit_password);

        Button btn_editar_password = findViewById(R.id.btn_editar_password);
        btn_editar_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cambiarPassword(id_perfil);
            }
        });
    }

    public void cambiarPassword(String id_perfil) {
        String pass_actual = password_actual.getText().toString();
        String pass = edit_password.getText().toString();
        String confirm_pass = confirm_edit_password.getText().toString();

        SweetAlertDialog pDialog = new SweetAlertDialog(EditarPassword.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Espera ...");
        pDialog.setCancelable(false);
        pDialog.show();

        if (!pass_actual.isEmpty() && !pass.isEmpty() && !confirm_pass.isEmpty()) {
            if (pass.equals(confirm_pass)) {
                RequestQueue hilo = Volley.newRequestQueue(this);
                String url = "https://agroplaza.solucionsoftware.co/ModuloUsuarios/EditarPasswordMovil";

                StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                String[] mensaje = response.split("\"");

                                if (mensaje[1].equalsIgnoreCase("INVALID##PASSWORD")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(EditarPassword.this, SweetAlertDialog.WARNING_TYPE)
                                            .setTitleText("Contraseña actual incorrecta")
                                            .setContentText("Debe ingresar la contraseña de su perfil.")
                                            .show();
                                } else if (mensaje[1].equalsIgnoreCase("ERROR##UPDATE")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(EditarPassword.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Error al actualizar la contraseña!")
                                            .show();
                                } else if (mensaje[1].trim().equalsIgnoreCase("OK##PASSWORD##UPDATE")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(EditarPassword.this, SweetAlertDialog.SUCCESS_TYPE)
                                            .setTitleText("Actualizacion Correcta!")
                                            .setContentText("Tu contraseña ha sido actualizada.")
                                            .setConfirmText("Hecho!")
                                            .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                                @Override
                                                public void onClick(SweetAlertDialog sDialog) {
                                                    EditarPassword.super.onBackPressed();
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
                        parametros.put("pass_actual", pass_actual);
                        parametros.put("password", pass);
                        return parametros;
                    }
                };
                hilo.add(solicitud);
            } else {
                pDialog.dismiss();
                new SweetAlertDialog(EditarPassword.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("LAS CONTRASEÑAS NO COINCIDEN!")
                        .setContentText("Las contraseñas no coinciden. Debes verificar que sean iguales.")
                        .show();
            }
        } else {
            pDialog.dismiss();
            new SweetAlertDialog(EditarPassword.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("FALTAN DATOS!")
                    .setContentText("Llena todos los datos del formulario.")
                    .show();
        }
    }
}