package com.example.agroplazaappmovil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.net.Uri;
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
import com.example.agroplazaappmovil.ui.perfil.EditarCiudad;

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

        SharedPreferences persistencia = getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String email = persistencia.getString("email", "NaN");
        if (!email.equalsIgnoreCase("NaN")) {
            Intent intent = new Intent(getApplicationContext(), Principal.class);
            startActivity(intent);
            finish();
        }

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

        String mensaje = getIntent().getStringExtra("mensaje");
        if (mensaje != null) {
            if (mensaje.equals("registrado")) {
                new SweetAlertDialog(Login.this, SweetAlertDialog.SUCCESS_TYPE)
                        .setTitleText("EXITO!")
                        .setContentText("Has sido registrado satisfactoriamente!")
                        .show();
            } else if (mensaje.equals("desactivado")) {
                new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                        .setTitleText("Tu cuenta ha sido desactivada!")
                        .setContentText("Ya no podras ingresar al sistema con esta cuenta. Si deseas recuperarla debes contactar con un administrador.")
                        .show();
            }
        }
    }

    public void validarDatosSesion() {
        String valor_email = campo_email.getText().toString();
        String valor_password = campo_password.getText().toString();

        SweetAlertDialog pDialog = new SweetAlertDialog(Login.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Cargando ...");
        pDialog.setCancelable(false);
        pDialog.show();

        // Instrucciones de Volley
        RequestQueue hilo = Volley.newRequestQueue(this);
        String url = "https://agroplaza.solucionsoftware.co/inicio/validarDatosIngresoMovil";

        StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        if (response.trim().equalsIgnoreCase("ERROR##INVALID##DATA")) {
                            pDialog.dismiss();
                            new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Datos Incorrectos!")
                                    .show();
                        } else if (response.trim().equalsIgnoreCase("NOT##ACCESS")) {
                            pDialog.dismiss();
                            new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("NO PUEDES INGRESAR!")
                                    .setContentText("Solo los clientes pueden ingresar a la app movil. Si quieres acceder con tu usuario hazlo desde la pagina web.")
                                    .setConfirmText("Ir a la pagina")
                                    .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                        @Override
                                        public void onClick(SweetAlertDialog sDialog) {
                                            String url = "http://agroplaza.solucionsoftware.co/";
                                            Intent i = new Intent(Intent.ACTION_VIEW);
                                            i.setData(Uri.parse(url));
                                            startActivity(i);
                                            sDialog.dismiss();
                                        }
                                    })
                                    .show();
                        } else if (response.trim().equalsIgnoreCase("NOT##STATUS")) {
                            pDialog.dismiss();
                            new SweetAlertDialog(Login.this, SweetAlertDialog.WARNING_TYPE)
                                    .setTitleText("ESTAS INACTIVADO!")
                                    .setContentText("Tu usuarios se encuentra inactivado. Si quieres recuperar tu usuario envia una peticion al correo: agroplaza@gmail.com.")
                                    .show();
                        } else {
                            String[] mensaje = response.split("#&&#");

                            if (mensaje[0].equalsIgnoreCase("OK##DATA##LOGIN")) {
                                String[] datos = mensaje[1].split("##");

                                String id = datos[0];
                                String email = datos[1];
                                String documento = datos[2];
                                String nombres = datos[3];
                                String apellidos = datos[4];
                                String id_ciudad = datos[5];
                                String direccion = datos[6];
                                String telefono = datos[7];
                                String genero = datos[8];
                                String avatar = datos[9];

                                SharedPreferences persistencia = getSharedPreferences("datos_login", Context.MODE_PRIVATE);
                                SharedPreferences.Editor editor = persistencia.edit();
                                editor.putString("id", id);
                                editor.putString("email", email);
                                editor.putString("documento", documento);
                                editor.putString("nombres", nombres);
                                editor.putString("apellidos", apellidos);
                                editor.putString("id_ciudad", id_ciudad);
                                editor.putString("direccion", direccion);
                                editor.putString("telefono", telefono);
                                editor.putString("genero", genero);
                                editor.putString("avatar", avatar);

                                editor.commit();

                                pDialog.dismiss();

                                Intent intent = new Intent(getApplicationContext(), Principal.class);
                                startActivity(intent);
                                finish();
                            } else {
                                pDialog.dismiss();
                                new SweetAlertDialog(Login.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Algo ha fallado...")
                                        .setContentText("Error en el servidor.")
                                        .show();
                            }
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