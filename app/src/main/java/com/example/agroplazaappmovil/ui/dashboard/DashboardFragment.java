package com.example.agroplazaappmovil.ui.dashboard;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.R;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.util.ArrayList;

public class DashboardFragment extends Fragment {

    View actividad;
    ArrayList<Pedidos> listaPedidos;
    RecyclerView recycler;
    AdapterPedidos adapter;

    public View onCreateView (@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actividad = inflater.inflate (R.layout.fragment_dashboard, container, false);
        ConsultarPedidos ();

        return actividad;
    }

    public void ConsultarPedidos () {

        // Recycler
        recycler = actividad.findViewById (R.id.recycler_historial_pedido);
        recycler.setLayoutManager (new LinearLayoutManager (this.getActivity (), LinearLayoutManager.VERTICAL, false));

        SharedPreferences persistencia = getContext ().getSharedPreferences ("datos_login", Context.MODE_PRIVATE);
        String id_usuario = persistencia.getString ("id", "");

        RequestQueue hilo = Volley.newRequestQueue (this.getActivity ());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPedidos/ConsultarPedidosUsuario?id_usuario=" + id_usuario;

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                listaPedidos = new ArrayList<> ();

                JSONArray lista_pedidos = response.optJSONArray ("pedidosUser");
                try {
                    if (lista_pedidos.length ()<=0){
                        TextView conten_vacio = actividad.findViewById (R.id.sin_pedidos);
                        conten_vacio.setVisibility (View.VISIBLE);
                        conten_vacio.setText ("No has hecho ningun pedido aun!");
                    }
                    for (int i = 0; i < lista_pedidos.length (); i++) {

                        JSONObject temp = lista_pedidos.getJSONObject (i);
                        String id = temp.getString ("id_p");
                        String titulo = temp.getString ("titulo_p");
                        String fecha = temp.getString ("fecha_p");
                        String estado = temp.getString ("estado_p");
                        String id_publi = temp.getString ("id_publicacion");

                        Pedidos pub = new Pedidos (id, titulo, fecha, estado, id_publi);
                        listaPedidos.add (pub);

                    }
                    adapter = new AdapterPedidos (listaPedidos);
                    adapter.setOnclickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), DetallePedido.class);
                            intent.putExtra("id", listaPedidos.get (recycler.getChildAdapterPosition (v)).numero_pedido);
                            intent.putExtra("titulo", listaPedidos.get (recycler.getChildAdapterPosition (v)).titulo_publicacion);
                            intent.putExtra("fecha", listaPedidos.get (recycler.getChildAdapterPosition (v)).fecha_pedido);
                            intent.putExtra("estado", listaPedidos.get (recycler.getChildAdapterPosition (v)).estado_pedido);
                            intent.putExtra("id_publi", listaPedidos.get (recycler.getChildAdapterPosition (v)).id_publi);
                            startActivity(intent);
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
}