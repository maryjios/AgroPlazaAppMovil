package com.example.agroplazaappmovil.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
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
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.ui.perfil.EditarDatos;
import com.example.agroplazaappmovil.ui.perfil.EditarEmail;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.HashMap;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class DetallePedido extends AppCompatActivity {
    String id_pedido, titulo_pedido, fecha_pedido, estado_pedido,id_publicacion, id_valoracion;
    TextView nombre_consumidor, id_consumidor, direccion_consumidor, telefono_consumidor;
    TextView TituloProducto, precioPedido, envioPedido, vendedor, textoPrecioUnit, textoCantidad, textoDescuento, textoTotal;
    Button btn_perfilVendedor, boton_calificar;
    ImageView fotoPublicacion;
    LinearLayout cPedidoEntregado;
    SweetAlertDialog pDialog;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detalle_pedido);

        mostrarAlertaCarga();

        Intent intent = getIntent ();
        id_pedido = intent.getStringExtra ("id");
        titulo_pedido = intent.getStringExtra ("titulo");
        fecha_pedido = intent.getStringExtra ("fecha");
        estado_pedido = intent.getStringExtra ("estado");
        id_publicacion = intent.getStringExtra ("id_publi");

        nombre_consumidor = findViewById (R.id.nombreCliente);
        id_consumidor = findViewById (R.id.cedulaCliente);
        direccion_consumidor = findViewById (R.id.direccionCliente);
        telefono_consumidor = findViewById (R.id.telefonoCliente);
        boton_calificar = findViewById (R.id.btnCalificar);

        cPedidoEntregado = findViewById (R.id.contentPedidoEntregado);
        if (!estado_pedido.equalsIgnoreCase ("FINALIZADO"))
            boton_calificar.setVisibility (View.GONE);

        if (estado_pedido.equalsIgnoreCase ("ENTREGADO"))
            cPedidoEntregado.setVisibility (View.VISIBLE);

        Button btnChatPedido = findViewById (R.id.btnChatPedido);
        btnChatPedido.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (getApplicationContext (), Chat_Activity.class);
                intent.putExtra ("numero_pedido", id_pedido);
                intent.putExtra ("titulo_publicacion", titulo_pedido);
                startActivity (intent);
            }
        });

        boton_calificar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent (getApplicationContext (), CalificarPublicacion.class);
                intent.putExtra ("id_publicacion", id_publicacion);
                intent.putExtra ("id_valoracion", id_valoracion);
                startActivity (intent);
            }
        });

        ImageButton regresar = findViewById(R.id.btn_Atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetallePedido.super.onBackPressed();
            }
        });

        Button btn_finalizar = findViewById(R.id.btn_finalizar_pedido);
        btn_finalizar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finalizarPedido();
            }
        });

        llenarDatosDetallePedido();
        llenarDatosConsumidorDetallePedido();
        setFotoDetalle();
    }

    private void setFotoDetalle () {
        fotoPublicacion = findViewById (R.id.imgProducto);
        RequestQueue request = Volley.newRequestQueue (getApplicationContext ());
        String url = "https://agroplaza.solucionsoftware.co/public/dist/img/publicaciones/publicacion" + id_publicacion + "/" + "foto_1.jpg";
        url = url.replace (" ", "%20");

        final String finalUrl = url;
        ImageRequest imageRequest = new ImageRequest (url,
                new Response.Listener<Bitmap> () {
                    @Override
                    public void onResponse (Bitmap response) {
                        fotoPublicacion.setImageBitmap (response);
                        pDialog.dismiss();
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener () {
            @Override
            public void onErrorResponse (VolleyError error) {
                Toast.makeText (getApplicationContext (), "Error: " + error.getMessage (), Toast.LENGTH_LONG).show ();
            }
        }
        );
        request.add (imageRequest);
    }

    public void llenarDatosDetallePedido () {
        TituloProducto = findViewById (R.id.TituloProducto);
        precioPedido = findViewById (R.id.precioPedido);
        envioPedido = findViewById (R.id.envioPedido);
        textoPrecioUnit = findViewById (R.id.textoPrecioUnit);
        textoCantidad = findViewById (R.id.textoCantidad);
        textoDescuento = findViewById (R.id.textoDescuento);
        textoTotal = findViewById (R.id.textoTotal);
        btn_perfilVendedor = findViewById (R.id.verPerfilVendedor);
        vendedor = findViewById (R.id.vendedor);
        RequestQueue hilo = Volley.newRequestQueue (getApplicationContext ());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPedidos/DatosDetallePedido?pedido=" + id_pedido;

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                JSONArray datosDetalle = response.optJSONArray ("datos");

                try {
                    for (int i = 0; i < datosDetalle.length (); i++) {
                        JSONObject dato = datosDetalle.getJSONObject (i);

                        TituloProducto.setText (dato.getString ("titulo_p"));
                        precioPedido.setText (dato.getString ("precio_p"));

                        if (dato.getString ("envio_p").equalsIgnoreCase ("NO")){
                            envioPedido.setVisibility (View.GONE);
                        } else {
                            envioPedido.setVisibility (View.VISIBLE);
                        }
                        btn_perfilVendedor.setText (dato.getString ("nombre_vendedor"));
                        textoPrecioUnit.setText (dato.getString ("precio_p"));
                        textoCantidad.setText (dato.getString ("cantidad_p"));
                        textoDescuento.setText ("$"+dato.getString ("descuento_p"));
                        textoTotal.setText (dato.getString ("total_p"));

                        // Datos necesarios en la actividad Perfil Vendedor
                        String tipo_v = dato.getString ("tipo_v");
                        String ciudad_v = dato.getString ("ciudad_v");
                        String departamento_v = dato.getString ("departamento_v");
                        String nombre_v = dato.getString ("nombre_vendedor");
                        String id_u = dato.getString ("id_u");

                        vendedor.setText (tipo_v);

                        btn_perfilVendedor.setOnClickListener (new View.OnClickListener () {
                            @Override
                            public void onClick (View v) {
                                Intent intent = new Intent (getApplicationContext (), PerfilVendedor.class);
                                intent.putExtra ("tipo_v", tipo_v);
                                intent.putExtra ("ciudad_v", ciudad_v);
                                intent.putExtra ("nombre_v", nombre_v);
                                intent.putExtra ("departamento_v", departamento_v);
                                intent.putExtra ("id_u", id_u);
                                startActivity (intent);
                            }
                        });

                        id_valoracion = dato.getString("id_valoracion");
                        if (!id_valoracion.isEmpty()) {
                            boton_calificar.setVisibility(View.GONE);
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse (VolleyError error) {

            }
        });

        hilo.add(solicitud);
    }

    public void llenarDatosConsumidorDetallePedido () {
        SharedPreferences persistencia = getApplicationContext ().getSharedPreferences("datos_login", Context.MODE_PRIVATE);

        nombre_consumidor.setText (persistencia.getString ("nombres", "") + " " + persistencia.getString ("apellidos", ""));
        id_consumidor.setText (persistencia.getString ("documento", ""));
        direccion_consumidor.setText (persistencia.getString ("direccion", ""));
        telefono_consumidor.setText (persistencia.getString ("telefono", ""));
    }

    public void mostrarAlertaCarga() {
        pDialog = new SweetAlertDialog(DetallePedido.this, SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Espera ...");
        pDialog.setCancelable(false);
        pDialog.show();
    }

    public void finalizarPedido() {
        new SweetAlertDialog(DetallePedido.this, SweetAlertDialog.WARNING_TYPE)
                .setTitleText("Finalizar Pedido")
                .setContentText("¿Confirmas que el pedido llego correctamente?")
                .setConfirmText("SI")
                .setConfirmClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();

                        mostrarAlertaCarga();

                        RequestQueue hilo = Volley.newRequestQueue(getApplicationContext());
                        String url = "https://agroplaza.solucionsoftware.co/ModuloPedidos/FinalizarPedido";

                        StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                                new Response.Listener<String>() {
                                    @Override
                                    public void onResponse(String response) {

                                        String[] mensaje = response.split("\"");

                                        if (mensaje[1].trim().equalsIgnoreCase("ERROR##UPDATE")) {
                                            pDialog.dismiss();
                                            new SweetAlertDialog(DetallePedido.this, SweetAlertDialog.ERROR_TYPE)
                                                    .setTitleText("Oops...")
                                                    .setContentText("Error al finalizar el pedido!")
                                                    .show();

                                        } else if (mensaje[1].trim().equalsIgnoreCase("OK##STATUS##UPDATE")) {
                                            boton_calificar.setVisibility (View.VISIBLE);
                                            cPedidoEntregado.setVisibility (View.GONE);

                                            pDialog.dismiss();
                                            new SweetAlertDialog(DetallePedido.this, SweetAlertDialog.SUCCESS_TYPE)
                                                    .setTitleText("Pedido Finalizado!")
                                                    .setContentText("El pedido ha concluido correctamente.")
                                                    .show();
                                        }
                                    }
                                },
                                new Response.ErrorListener() {
                                    @Override
                                    public void onErrorResponse(VolleyError error) {
                                        // Codigo de error del servidor
                                        // Se ejecuta cuando no llega el tipo solicitado String.
                                        pDialog.dismiss();
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
                                parametros.put("pedido", id_pedido);
                                return parametros;
                            }
                        };

                        hilo.add(solicitud);
                    }
                })
                .setCancelText("NO")
                .setCancelClickListener(new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sweetAlertDialog) {
                        sweetAlertDialog.dismissWithAnimation();
                    }
                })
                .show();
    }
}