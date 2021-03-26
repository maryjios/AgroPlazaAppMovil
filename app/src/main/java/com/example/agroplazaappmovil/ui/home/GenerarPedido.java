package com.example.agroplazaappmovil.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.RadioGroup;
import android.widget.TextView;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GenerarPedido extends AppCompatActivity {

    TextView nom_pro, valor_pro, envio_pro, descuento_pro, unidad_pro;
    EditText cantidad, nom_vendedor, valor_total, documento_cl, direccion_cl;
    Button btnaumntar, btndisminuir, btn_atras_compra;
    ImageView img_publicacion;
    int valor = 1;
    String id_publicacion;

    SweetAlertDialog pDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_generar_pedido);

        pDialog = new SweetAlertDialog(GenerarPedido.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Cargando ...");
        pDialog.setCancelable(true);
        pDialog.show();

        img_publicacion = findViewById(R.id.img_view);
        nom_pro = findViewById(R.id.nom_producto);
        valor_pro = findViewById(R.id.vl_producto);
        envio_pro = findViewById(R.id.envio_pro);
        descuento_pro = findViewById(R.id.descuento);
        unidad_pro = findViewById(R.id.unidad_pedido);

        cantidad = findViewById(R.id.cantidad);
        btnaumntar = findViewById(R.id.btnaumentar);
        btndisminuir = findViewById(R.id.btndisminuir);
        btn_atras_compra = findViewById(R.id.btn_atras_compra);

        nom_vendedor = findViewById(R.id.vendedor);
        valor_total = findViewById(R.id.valor_pedido);
        documento_cl = findViewById(R.id.documento_cl);
        direccion_cl = findViewById(R.id.direccion_pedido);

        btn_atras_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                GenerarPedido.super.onBackPressed();
            }
        });

        SharedPreferences persistencia = getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String documento = persistencia.getString("documento", "");
        String direccion = persistencia.getString("direccion", "");

        Intent intent = getIntent();
        int stock = (intent.getStringExtra("stock") != null) ? Integer.parseInt(intent.getStringExtra("stock")) : 0;
        id_publicacion = intent.getStringExtra("id_publicacion");

        cargarDatos(documento, direccion);

        if (stock == 0) {
            btndisminuir.setVisibility(View.GONE);
            cantidad.setVisibility(View.GONE);
            btnaumntar.setVisibility(View.GONE);
            unidad_pro.setVisibility(View.GONE);
        } else {
            if (stock == 1)
                btnaumntar.setEnabled(false);

            btnaumntar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (valor < stock)
                        cantidad.setText(String.valueOf(++valor));

                    if (valor == stock)
                        btnaumntar.setEnabled(false);

                    if (valor > 1)
                        btndisminuir.setEnabled(true);
                }
            });

            btndisminuir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (valor > 1)
                        cantidad.setText(String.valueOf(--valor));

                    if (valor <= 1)
                        btndisminuir.setEnabled(false);

                    if (valor < stock)
                        btnaumntar.setEnabled(true);
                }
            });


        }
    }

    public void cargarImagen(String img) {
        RequestQueue hilo = Volley.newRequestQueue(getApplicationContext());

        String url = "https://agroplaza.solucionsoftware.co/public/dist/img/publicaciones/publicacion" + id_publicacion + "/" + img;
        url = url.replace(" ","%20");

        final String finalUrl = url;
        ImageRequest imageRequest = new ImageRequest (url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        img_publicacion.setImageBitmap(response);

                        pDialog.dismiss();
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getApplicationContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
            }
        }
        );
        hilo.add(imageRequest);
    }

    public void cargarDatos(String documento, String direccion) {
        RequestQueue hilo = Volley.newRequestQueue(getApplicationContext());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/getDatosPublicacion?id=" + id_publicacion;

        JsonObjectRequest solicitud = new JsonObjectRequest(Request.Method.GET, url, null, new Response.Listener<JSONObject>() {
            @Override
            public void onResponse(JSONObject response) {
                try {
                    JSONArray registros = response.optJSONArray("datos_publicacion");

                    String imagen = "";
                    for (int i = 0; i < registros.length(); i++) {
                        JSONObject dato = registros.getJSONObject (i);

                        nom_pro.setText(dato.getString("titulo"));
                        valor_pro.setText(valor_pro.getText() + dato.getString("precio"));
                        envio_pro.setText((dato.getString("envio").equalsIgnoreCase("SI")) ? "Incluye envio" : "Envio gratis");
                        nom_vendedor.setText(dato.getString("nombre_usuario"));
                        unidad_pro.setText(dato.getString("abreviatura"));

                        //String[] obtenerValor = valor_pro.getText().toString().split(" : ");
                        //obtenerValor = obtenerValor[1].split(" ");
                        Float precio = Float.parseFloat(dato.getString("precio"));

                        //String[] obtenerDescuento = descuento_pro.getText().toString().split(" : ");
                        //obtenerDescuento = obtenerDescuento[1].split("%");
                        Float descuento = Float.parseFloat(dato.getString("descuento"));
                        descuento = precio * (descuento / 100);

                        descuento_pro.setText(descuento_pro.getText() + "(" + dato.getString("descuento") + "%) : $ " + descuento);

                        valor_total.setText(String.valueOf(precio - descuento));

                        documento_cl.setText(documento);
                        direccion_cl.setText(direccion);

                        imagen = dato.getString("imagen");
                    }

                    cargarImagen(imagen);

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
}