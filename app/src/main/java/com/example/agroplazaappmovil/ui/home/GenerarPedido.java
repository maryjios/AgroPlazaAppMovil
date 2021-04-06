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
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.RegistroUsuarios;
import com.example.agroplazaappmovil.ui.perfil.EditarDatos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class GenerarPedido extends AppCompatActivity {

    TextView nom_pro, valor_pro, envio_pro, descuento_pro, unidad_pro;
    EditText cantidad, nom_vendedor, valor_total, documento_cl, direccion_cl;
    Button btnaumntar, btndisminuir, btn_atras_compra, btn_generar_pedido;
    ImageView img_publicacion;
    Float precio, descuento;
    String id_publicacion;
    int valor;

    SweetAlertDialog pDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_generar_pedido);

        cargando();

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
        int stock = (!intent.getStringExtra("stock").isEmpty()) ? Integer.parseInt(intent.getStringExtra("stock")) : 0;
        id_publicacion = intent.getStringExtra("id_publicacion");
        int valor_unidad = (!intent.getStringExtra("valor_unidad").isEmpty()) ? Integer.parseInt(intent.getStringExtra("valor_unidad")) : 0;
        String tipo_publicacion = intent.getStringExtra("tipo_publicacion");

        cantidad.setText(valor_unidad + "");

        valor = valor_unidad;

        cargarDatos(documento, direccion, tipo_publicacion);

        if (stock == 0) {
            btndisminuir.setVisibility(View.GONE);
            cantidad.setVisibility(View.GONE);
            btnaumntar.setVisibility(View.GONE);
            unidad_pro.setVisibility(View.GONE);
        } else {
            if (stock == valor_unidad || valor_unidad * 2 > stock)
                btnaumntar.setEnabled(false);

            btnaumntar.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (valor < stock)
                        cantidad.setText(String.valueOf(valor += valor_unidad));

                    if (valor == stock || valor_unidad * 2 > stock)
                        btnaumntar.setEnabled(false);

                    if (valor > valor_unidad)
                        btndisminuir.setEnabled(true);

                    calcularTotal(valor_unidad);
                }
            });

            btndisminuir.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    if (valor > valor_unidad)
                        cantidad.setText(String.valueOf(valor -= valor_unidad));

                    if (valor <= valor_unidad)
                        btndisminuir.setEnabled(false);

                    if (valor < stock)
                        btnaumntar.setEnabled(true);

                    calcularTotal(valor_unidad);
                }
            });
        }

        btn_generar_pedido = findViewById(R.id.solicitar_pedido);
        btn_generar_pedido.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                realizarPedido(stock, persistencia);
            }
        });
    }

    public void cargando() {
        pDialog = new SweetAlertDialog(GenerarPedido.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Cargando ...");
        pDialog.setCancelable(true);
        pDialog.show();
    }

    public void calcularTotal(int valor_unidad) {
        int cant = Integer.parseInt(cantidad.getText().toString()) / valor_unidad;
        Float valor_descuento = precio * (descuento / 100);
        Float total = (precio * cant) - valor_descuento;

        valor_total.setText(String.valueOf(total));
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

    public void cargarDatos(String documento, String direccion, String tipo_publicacion) {
        RequestQueue hilo = Volley.newRequestQueue(getApplicationContext());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/getDatosPublicacion?id=" + id_publicacion + "&tipo=" + tipo_publicacion;

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

                        if (tipo_publicacion.equals("PRODUCTO"))
                            unidad_pro.setText(dato.getString("abreviatura"));
                        
                        precio = Float.parseFloat(dato.getString("precio"));
                        
                        descuento = Float.parseFloat(dato.getString("descuento"));
                        Float valor_descuento = precio * (descuento / 100);

                        descuento_pro.setText(descuento_pro.getText() + "(" + dato.getString("descuento") + "%) : $ " + valor_descuento);

                        valor_total.setText(String.valueOf(precio - valor_descuento));

                        documento_cl.setText(documento);
                        direccion_cl.setText(direccion);

                        imagen = dato.getString("imagen");
                        Log.e("prueba", dato.getString("imagen"));
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

    public void realizarPedido(int stock, SharedPreferences persistencia) {
        cargando();

        String id_perfil = persistencia.getString("id", "0");
        String documento = documento_cl.getText().toString();
        String direccion = direccion_cl.getText().toString();

        if (!documento.isEmpty() && !direccion.isEmpty()) {
            String cant_pedido = cantidad.getText().toString();

            String precio_pedido = String.valueOf(precio);

            String descuento_pedido = String.valueOf(descuento);

            String total_pedido = valor_total.getText().toString();

            RequestQueue hilo = Volley.newRequestQueue(this);
            String url = "https://agroplaza.solucionsoftware.co/ModuloPedidos/GenerarPedidoMovil";

            StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                    new Response.Listener<String>() {
                        @Override
                        public void onResponse(String response) {

                            String[] mensaje = response.split("\"");

                            if (mensaje[1].equalsIgnoreCase("INVALID##DOCUMENT")) {
                                pDialog.dismiss();
                                new SweetAlertDialog(GenerarPedido.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("El documento ya existe!")
                                        .setContentText("El documento ingresado esta registrado con otro usuario.")
                                        .show();
                            } else if (mensaje[1].equalsIgnoreCase("ERROR##INSERT")) {
                                pDialog.dismiss();
                                new SweetAlertDialog(GenerarPedido.this, SweetAlertDialog.ERROR_TYPE)
                                        .setTitleText("Oops...")
                                        .setContentText("Error al generar el pedido!")
                                        .show();
                            } else if (mensaje[1].trim().equalsIgnoreCase("OK##DATA##INSERT")) {
                                SharedPreferences.Editor editor = persistencia.edit();
                                editor.putString("documento", documento);
                                editor.putString("direccion", direccion);

                                editor.commit();

                                pDialog.dismiss();
                                new SweetAlertDialog(GenerarPedido.this, SweetAlertDialog.SUCCESS_TYPE)
                                        .setTitleText("Pedido Realizado!")
                                        .setContentText("Tu pedido ha sido enviado.")
                                        .setConfirmText("Hecho!")
                                        .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                                            @Override
                                            public void onClick(SweetAlertDialog sDialog) {
                                                GenerarPedido.super.onBackPressed();
                                                sDialog.dismiss();
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

                            pDialog.dismiss();
                        }
                    }) {
                protected Map<String, String> getParams() {
                    Map<String, String> parametros = new HashMap<String, String>();
                    parametros.put("cantidad", cant_pedido);
                    parametros.put("valor_compra", precio_pedido);
                    parametros.put("descuento", descuento_pedido);
                    parametros.put("valor_total", total_pedido);
                    parametros.put("direccion", direccion);
                    parametros.put("id_usuario", id_perfil);
                    parametros.put("id_publicacion", id_publicacion);
                    parametros.put("documento", documento);
                    return parametros;
                }
            };

            hilo.add(solicitud);
        } else {
            pDialog.dismiss();
            new SweetAlertDialog(GenerarPedido.this, SweetAlertDialog.ERROR_TYPE)
                    .setTitleText("FALTAN DATOS!")
                    .setContentText("Ingresa tu documento y direccion de domicilio.")
                    .show();
        }
    }
}