package com.example.agroplazaappmovil.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
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
import com.android.volley.toolbox.Volley;
import com.denzcoskun.imageslider.models.SlideModel;
import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.ui.perfil.EditarDatos;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

public class DetallePedido extends AppCompatActivity {
    String id_pedido, titulo_pedido, fecha_pedido, estado_pedido,id_publicacion;
    TextView nombre_consumidor, id_consumidor, direccion_consumidor, telefono_consumidor;
    TextView TituloProducto, precioPedido, envioPedido, vendedor, textoPrecioUnit, textoCantidad, textoDescuento, textoTotal;
    Button verPerfilVendedor;
    ImageView fotoPublicacion;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detalle_pedido);

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

        LinearLayout cPedidoEntregado = findViewById (R.id.contentPedidoEntregado);
        if (estado_pedido.equalsIgnoreCase ("ENTREGADO")){
            cPedidoEntregado.setVisibility (View.VISIBLE);
        }else{
            cPedidoEntregado.setVisibility (View.GONE);
        }

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

        ImageButton regresar = findViewById(R.id.btn_Atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetallePedido.super.onBackPressed();
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
        verPerfilVendedor = findViewById (R.id.verPerfilVendedor);
        textoPrecioUnit = findViewById (R.id.textoPrecioUnit);
        textoCantidad = findViewById (R.id.textoCantidad);
        textoDescuento = findViewById (R.id.textoDescuento);
        textoTotal = findViewById (R.id.textoTotal);

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
                        }else {
                            envioPedido.setVisibility (View.VISIBLE);
                        }
                        verPerfilVendedor.setText (dato.getString ("nombre_vendedor"));
                        textoPrecioUnit.setText (dato.getString ("precio_p"));
                        textoCantidad.setText (dato.getString ("cantidad_p"));
                        textoDescuento.setText ("$"+dato.getString ("descuento_p"));
                        textoTotal.setText (dato.getString ("total_p"));

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
        nombre_consumidor.setText (persistencia.getString ("nombres", "")+" "+persistencia.getString ("apellidos", ""));
        id_consumidor.setText (persistencia.getString ("documento", ""));
        direccion_consumidor.setText (persistencia.getString ("direccion", ""));
        telefono_consumidor.setText (persistencia.getString ("telefono", ""));

    }
}