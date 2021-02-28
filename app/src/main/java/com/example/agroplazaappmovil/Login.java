package com.example.agroplazaappmovil;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class Login extends AppCompatActivity {

    EditText campo_email, campo_password;
    TextView btn_registrar;
    Button btn_ingresar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.login);

        campo_email = findViewById(R.id.campo_email);
        campo_password = findViewById(R.id.campo_password);
        btn_ingresar = findViewById(R.id.btn_ingresar);
        btn_registrar = findViewById(R.id.txt_btn_registro);

        btn_ingresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                validarDatosSesion();
            }
        });

        btn_registrar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarUsuario();
            }
        });

        Bundle mensaje = this.getIntent().getExtras();
        if (mensaje != null) {
            new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                    .setTitleText("EXITO!")
                    .setContentText("Has sido registrado satisfactoriamente!")
                    .show();
        }
    }

    public void validarDatosSesion() {
        String valor_email = campo_email.getText().toString();
        String valor_password = campo_password.getText().toString();

        SweetAlertDialog pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Cargando ...");
        pDialog.setCancelable(true);
        pDialog.show();

        // Instrucciones de Volley
        RequestQueue hilo = Volley.newRequestQueue(this);
        String url = "https://agroplaza.solucionsoftware.co/inicio/validarDatosIngreso";

        StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equalsIgnoreCase("OK##DATA##LOGIN")) {
                            Intent intent = new Intent(getApplicationContext(), MenuInicio.class);
                            startActivity(intent);
                            finish();
                        } else if (response.trim().equalsIgnoreCase("ERROR##INVALID##DATA")) {
                            pDialog.dismiss();
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Datos Incorrectos!")
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
                parametros.put("email", valor_email);
                parametros.put("password", valor_password);
                return parametros;
            }
        };
        hilo.add(solicitud);
    }

    public void registrarUsuario() {

        Intent intent = new Intent(getApplicationContext(), RegistroUsuarios.class);

        startActivity(intent);

    }
}