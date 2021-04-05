package com.example.agroplazaappmovil.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.ImageButton;
import android.widget.TextView;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.ui.home.AdapterDescuentos;
import com.example.agroplazaappmovil.ui.home.Descuentos;
import com.example.agroplazaappmovil.ui.home.DetallePublicacion;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class PerfilVendedor extends AppCompatActivity {
    TextView nombre_y_ubicacion;
    
    ArrayList<PublicacionesPerfil> listaPublicacionesPerfil;
    RecyclerView recycler;
    AdapterPublicacionesPerfil adapter;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_perfil_vendedor);

        Intent intent = getIntent ();
        nombre_y_ubicacion = findViewById (R.id.nombreAndUbicacion);

        nombre_y_ubicacion.setText (intent.getStringExtra ("nombre_v") + " | " + intent.getStringExtra ("ciudad_v") + ", " + intent.getStringExtra ("departamento_v"));

        String id_u = intent.getStringExtra ("id_u");
        consultarEspecializacionVendedor (id_u);

        Toast.makeText (getApplicationContext (), "EEEE: " + id_u, Toast.LENGTH_LONG).show ();

        ImageButton regresar = findViewById (R.id.btn_Atras);
        regresar.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                PerfilVendedor.super.onBackPressed ();
            }
        });

        listarPublicacionesPerfilPerfil (id_u);
    }

    public void listarPublicacionesPerfilPerfil (String id_u) {
        
        recycler = findViewById (R.id.recycler_publi_recientes);
        recycler.setLayoutManager (new LinearLayoutManager (getApplicationContext (), LinearLayoutManager.HORIZONTAL, false));

        RequestQueue hilo = Volley.newRequestQueue (this.getApplicationContext ());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/ListarPublicacionesPerfilMovil?user=" + id_u;

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                listaPublicacionesPerfil = new ArrayList<> ();


                JSONArray lista_clientes = response.optJSONArray ("registros_publicaciones");
                Log.i ("Consulta", lista_clientes.toString ());
                try {

                    for (int i = 0; i < lista_clientes.length (); i++) {

                        JSONObject temp = lista_clientes.getJSONObject (i);
                        String id = temp.getString ("id_publicacion");
                        String foto = temp.getString ("imagen");
                        String titulo = temp.getString ("titulo");
                        String precio = temp.getString ("precio");
                        String envio = "";
                        String unidad = temp.getString ("abreviatura");
                        String valor_descuento = temp.getString ("descuento");
                        String descripcion = temp.getString ("descripcion");
                        String stock = temp.getString ("stock");

                        String valor_envio = temp.getString ("envio");
                        if (valor_envio.equals ("SI")) {
                            envio = "Incluye envio";

                        } else if (!valor_envio.equals ("SI")) {
                            envio = "";
                        }

                        PublicacionesPerfil pub = new PublicacionesPerfil (id, foto, titulo, envio, valor_descuento, precio, descripcion, unidad, stock);
                        listaPublicacionesPerfil.add (pub);
                    }

                    adapter = new AdapterPublicacionesPerfil (listaPublicacionesPerfil);
                    adapter.setOnclickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            Intent intent = new Intent (getApplicationContext ().getApplicationContext (), DetallePublicacion.class);
                            intent.putExtra ("id", listaPublicacionesPerfil.get (recycler.getChildAdapterPosition (v)).id);
                            intent.putExtra ("titulo", listaPublicacionesPerfil.get (recycler.getChildAdapterPosition (v)).titulo);
                            intent.putExtra ("unidad", listaPublicacionesPerfil.get (recycler.getChildAdapterPosition (v)).unidad);
                            intent.putExtra ("precio", listaPublicacionesPerfil.get (recycler.getChildAdapterPosition (v)).precio);
                            intent.putExtra ("descripcion", listaPublicacionesPerfil.get (recycler.getChildAdapterPosition (v)).descripcion);
                            intent.putExtra ("stock", listaPublicacionesPerfil.get (recycler.getChildAdapterPosition (v)).stock);

                            startActivity (intent);
                        }
                    });
                    recycler.setAdapter (adapter);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }
            }
        }, new Response.ErrorListener () {
            @Override
            public void onErrorResponse (VolleyError error) {
            }
        });
        hilo.add (solicitud);
    }

    public void consultarEspecializacionVendedor (String id_u) {

        RequestQueue hilo = Volley.newRequestQueue (this);
        String url = "https://agroplaza.solucionsoftware.co/ModuloPedidos/nombreEspecializacionVendedor?usuario=" + id_u;

        StringRequest solicitud = new StringRequest (Request.Method.GET, url,
                new Response.Listener<String> () {
                    @Override
                    public void onResponse (String response) {

                        Toast.makeText (getApplicationContext (), "Registrada" + response, Toast.LENGTH_LONG).show ();
                    }
                },
                new Response.ErrorListener () {
                    @Override
                    public void onErrorResponse (VolleyError error) {
                        // Codigo de error del servidor
                        // Se ejecuta cuando no llega el tipo solicitado String.
                        Toast.makeText (getApplicationContext (), "Error Servidor: " + error.getMessage (), Toast.LENGTH_LONG).show ();
                        if (error.getMessage () != null) {
                            Log.i ("Error Servidor: ", error.getMessage ());
                        } else {
                            Log.i ("Error Servidor: ", "Error desconocido");
                        }
                    }
                });
        hilo.add (solicitud);

    }
}