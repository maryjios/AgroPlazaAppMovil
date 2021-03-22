package com.example.agroplazaappmovil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class RegistroUsuarios extends AppCompatActivity {

    EditText campo_email, campo_nombres, campo_apellidos, campo_telefono, campo_password, confirm_password;
    Spinner spinner_genero, spinner_ciudad;
    Button btn_registrar_cliente;
    int[] cod_ciudades;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.registro_usuarios);

        campo_email = findViewById(R.id.campo_email);
        campo_nombres = findViewById(R.id.campo_nombres);
        campo_apellidos = findViewById(R.id.campo_apellidos);
        campo_telefono = findViewById(R.id.campo_telefono);
        campo_password = findViewById(R.id.campo_password);
        confirm_password = findViewById(R.id.confirm_password);
        spinner_genero = findViewById(R.id.genero);
        spinner_ciudad = findViewById(R.id.ciudad);

        btn_registrar_cliente = findViewById(R.id.btn_registrar_cliente);

        btn_registrar_cliente.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                registrarCliente();
            }
        });

        String[] generos = {"Seleccione Genero", "FEMENINO", "MASCULINO", "OTRO"};

        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinners, generos);
        spinner_genero.setAdapter(adapter);

        cargarCiudades();
    }

    public void cargarCiudades() {
        RequestQueue hilo = Volley.newRequestQueue(getApplicationContext());
        String url = "https://agroplaza.solucionsoftware.co/ModuloUsuarios/CargarCiudades";

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray registros = response.optJSONArray("ciudades");

                    String[] ciudades = new String[registros.length() + 1];
                    cod_ciudades = new int[ciudades.length];

                    cod_ciudades[0] = 0;
                    ciudades[0] = "Seleccione Ciudad";
                    for (int i = 0; i < registros.length(); i++) {
                        JSONObject fila = registros.getJSONObject(i);

                        cod_ciudades[i + 1] = fila.getInt("id");
                        ciudades[i + 1] = fila.getString("nombre").toUpperCase();
                    }

                    cargarSpinnerCiudades(ciudades);

                } catch (JSONException e) {
                    e.printStackTrace();
                    Log.i("Error extrayendo datos ", e.getMessage());
                }
            }
        }, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error Servidor: " + error.getMessage(), Toast.LENGTH_LONG).show();
                if (error.getMessage() != null) {
                    Log.i("Error Servidor: ", error.getMessage());
                } else {
                    Log.i("Error Servidor: ", "Error desconocido");
                }
            }
        });

        hilo.add(solicitud);
    }

    public void cargarSpinnerCiudades(String[] ciudades) {
        ArrayAdapter<String> adapter = new ArrayAdapter<>(this, R.layout.spinners, ciudades);
        spinner_ciudad.setAdapter(adapter);
    }

    public void registrarCliente() {
        String valor_email = campo_email.getText().toString();
        String valor_nombres = campo_nombres.getText().toString();
        String valor_apellidos = campo_apellidos.getText().toString();
        String valor_telefono = campo_telefono.getText().toString();
        String valor_password = campo_password.getText().toString();
        String valor_confirm_password = confirm_password.getText().toString();
        String valor_genero = spinner_genero.getSelectedItem().toString();
        int cod_ciudad = cod_ciudades[spinner_ciudad.getSelectedItemPosition()];

        SweetAlertDialog pDialog = new SweetAlertDialog(RegistroUsuarios.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Espera ...");
        pDialog.setCancelable(false);
        pDialog.show();

        if (!valor_email.isEmpty() && !valor_nombres.isEmpty() && !valor_apellidos.isEmpty()
                && !valor_telefono.isEmpty() && !valor_password.isEmpty() && !valor_confirm_password.isEmpty() &&
                spinner_genero.getSelectedItemPosition() > 0 && cod_ciudad > 0) {
            if (valor_password.equals(valor_confirm_password)) {
                // Instrucciones de Volley
                RequestQueue hilo = Volley.newRequestQueue(this);
                String url = "https://agroplaza.solucionsoftware.co/ModuloUsuarios/InsertarMovil";

                StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                        new Response.Listener<String>() {
                            @Override
                            public void onResponse(String response) {

                                if (response.trim().equalsIgnoreCase("OK#CORRECT#DATA")) {
                                    Intent intent = new Intent(getApplicationContext(), Login.class);
                                    intent.putExtra("mensaje", "registro");
                                    startActivity(intent);
                                    finish();
                                } else if (response.trim().equalsIgnoreCase("ERROR#INVALID#DATA")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(RegistroUsuarios.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("Oops...")
                                            .setContentText("Error al registrarse!")
                                            .show();
                                } else if (response.trim().equalsIgnoreCase("FAIL#EMAIL")) {
                                    pDialog.dismiss();
                                    new SweetAlertDialog(RegistroUsuarios.this, SweetAlertDialog.ERROR_TYPE)
                                            .setTitleText("YA EXISTE!")
                                            .setContentText("El correo ingresado ya esta registrado!")
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
                        parametros.put("nombres", valor_nombres);
                        parametros.put("apellidos", valor_apellidos);
                        parametros.put("telefono", valor_telefono);
                        parametros.put("genero", valor_genero);
                        parametros.put("ciudad", cod_ciudad + "");
                        return parametros;
                    }
                };
                hilo.add(solicitud);

            } else {
                pDialog.dismiss();
                new SweetAlertDialog(RegistroUsuarios.this, SweetAlertDialog.ERROR_TYPE)
                        .setTitleText("NO COINCIDEN!")
                        .setContentText("Las contraseñas no coinciden!")
                        .show();
            }
        } else {
            pDialog.dismiss();
            new SweetAlertDialog(RegistroUsuarios.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("FALTAN DATOS!")
                    .setContentText("Llena todo el formulario.")
                    .show();
        }
    }
}