package com.example.agroplazaappmovil.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.GridLayoutManager;
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

public class HomeFragment extends Fragment {
    View actividad;
    ArrayList<Productos> listaPublicaciones;
    RecyclerView recycler;
    AdapterDatos adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actividad = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences persistencia = actividad.getContext().getSharedPreferences("datos_login", Context.MODE_PRIVATE);

        TextView nombreUser = actividad.findViewById(R.id.textBienvenidaPincipal);
        nombreUser.setText("Hola, "+""+persistencia.getString ("nombres", "").toString ()+","+" Bienvenido");

        TextView ubicacion = actividad.findViewById(R.id.textUbicacion);
        ubicacion.setText("Ahora mismo estas en - Pereira,Risaralda");

        recycler = actividad.findViewById(R.id.mi_recycler);

        recycler.setLayoutManager(new GridLayoutManager (this.getActivity(), 2));
        RequestQueue hilo = Volley.newRequestQueue (this.getActivity());

        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/ListarPublicacionesMovil";

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                listaPublicaciones = new ArrayList<> ();

                JSONArray lista_clientes = response.optJSONArray ("registros_publicaciones");

                Log.i ("mensaje", lista_clientes.toString ());
                try {

                    for (int i = 0; i < lista_clientes.length (); i++) {

                        JSONObject temp = lista_clientes.getJSONObject (i);
                        String id = temp.getString ("id_publicacion");
                        String foto = temp.getString ("imagen");
                        String titulo = temp.getString ("titulo") + " " + temp.getString ("precio");
                        String descripcion = "";

                        Productos pub = new Productos (titulo, descripcion, foto, id);
                        listaPublicaciones.add (pub);
                    }
                    adapter = new AdapterDatos (listaPublicaciones);
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

        hilo.add(solicitud);
        return actividad;
    }
}