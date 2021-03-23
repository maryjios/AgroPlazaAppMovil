package com.example.agroplazaappmovil.ui.home;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

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
    ArrayList<Publicaciones> listaPublicaciones;
    RecyclerView recycler;
    AdapterPublicaciones adapter;
    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actividad = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences persistencia = actividad.getContext().getSharedPreferences("datos_login", Context.MODE_PRIVATE);

        TextView nombreUser = actividad.findViewById(R.id.textBienvenidaPincipal);
        nombreUser.setText("Hola, "+""+persistencia.getString ("nombres", "").toString ()+","+" Bienvenido");

        ConsultarNombreCiudadUser();
        CargarPublicacionesEnFragment();
        return actividad;
    }
    public void ConsultarNombreCiudadUser(){

        SharedPreferences persistencia = actividad.getContext().getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String ciudad = persistencia.getString ("id_ciudad", "");

        TextView ubicacion = actividad.findViewById(R.id.textUbicacion);
        RequestQueue hilo = Volley.newRequestQueue (this.getActivity());
        String url = "https://agroplaza.solucionsoftware.co/ModuloUsuarios/nombreCiudad/?ciudad="+ciudad;

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {

                JSONArray n_ciudadYdepartamento = response.optJSONArray ("nCiudadyDepartamento");
                String la_ciudad = "";
                String el_departamento = "";
                try {
                    for (int i = 0; i < n_ciudadYdepartamento.length (); i++) {
                        JSONObject dato = n_ciudadYdepartamento.getJSONObject (i);
                        la_ciudad = dato.getString ("la_ciudad");
                        el_departamento = dato.getString ("el_departamento").toString ();
                    }

                    SharedPreferences persistencia = getContext ().getSharedPreferences("datos_login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = persistencia.edit();
                    editor.putString("departamento", el_departamento);
                    editor.putString("departamento", la_ciudad);
                    editor.commit();

                    ubicacion.setText(la_ciudad+","+el_departamento);
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

    public void CargarPublicacionesEnFragment(){
        recycler = actividad.findViewById(R.id.mi_recycler);

        recycler.setLayoutManager(new GridLayoutManager (this.getActivity(), 2));
        RequestQueue hilo = Volley.newRequestQueue (this.getActivity());

        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/ListarPublicacionesMovil";

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                listaPublicaciones = new ArrayList<> ();

                JSONArray lista_clientes = response.optJSONArray ("registros_publicaciones");
                try {
                    for (int i = 0; i < lista_clientes.length (); i++) {
                        
                        JSONObject temp = lista_clientes.getJSONObject (i);
                        String descuento = "";
                        String envio = "";
                        String id = temp.getString ("id_publicacion");
                        String foto = temp.getString ("imagen");
                        String titulo = temp.getString ("titulo");
                        String precio = temp.getString ("precio");
                        String valor_descento = temp.getString ("descuento");
                        String descripcion = temp.getString ("descripcion");

                        if (valor_descento.equals("0")){
                            descuento = "";
                        }else{
                            descuento = temp.getString ("descuento");
                        }

                        String valor_envio = temp.getString ("envio");
                        if (valor_envio.equals ("SI")){
                            envio = "Incluye envio";
                        }else if (!valor_envio.equals ("SI")){
                            envio = "";
                        }

                        Publicaciones pub = new Publicaciones (titulo, precio, envio, descuento, foto,id, descripcion);
                        listaPublicaciones.add (pub);
                    }
                    adapter = new AdapterPublicaciones (listaPublicaciones);
                    adapter.setOnclickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), DetallePublicacion.class);
                            intent.putExtra("id", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).id);
                            intent.putExtra("titulo", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).titulo);
                            intent.putExtra("descripcion", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).descripcion);
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
        hilo.add(solicitud);

    }
}