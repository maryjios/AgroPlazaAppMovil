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
    ArrayList<Descuentos> listaDescuentos;

    RecyclerView recycler;
    RecyclerView recycler_descuentos;
    AdapterPublicaciones adapter;
    AdapterDescuentos adapter_des;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actividad = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences persistencia = actividad.getContext().getSharedPreferences("datos_login", Context.MODE_PRIVATE);

        TextView nombreUser = actividad.findViewById(R.id.textBienvenidaPincipal);
        nombreUser.setText("Hola, "+""+persistencia.getString ("nombres", "").toString ()+","+" Bienvenido");

        ConsultarNombreCiudadUser();
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
                String id_departamento = "";
                try {
                    for (int i = 0; i < n_ciudadYdepartamento.length (); i++) {
                        JSONObject dato = n_ciudadYdepartamento.getJSONObject (i);
                        la_ciudad = dato.getString ("la_ciudad");
                        el_departamento = dato.getString ("el_departamento");
                        id_departamento = dato.getString ("id_departamento");
                    }

                    SharedPreferences persistencia = getContext ().getSharedPreferences("datos_login", Context.MODE_PRIVATE);
                    SharedPreferences.Editor editor = persistencia.edit();
                    editor.putString("departamento", el_departamento);
                    editor.putString("ciudad", la_ciudad);
                    editor.putString("id_departamento", id_departamento);
                    editor.commit();
                    CargarPublicacionesEnFragment();

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
        recycler.setLayoutManager(new LinearLayoutManager(this.getActivity (), LinearLayoutManager.HORIZONTAL, false));

        // Recycler descuento
        recycler_descuentos = actividad.findViewById(R.id.mi_recycler_descuento);
        recycler_descuentos.setLayoutManager(new LinearLayoutManager(this.getActivity (), LinearLayoutManager.HORIZONTAL, false));

        SharedPreferences persistencia = getContext ().getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String id_departamento = persistencia.getString ("id_departamento", "");

        RequestQueue hilo = Volley.newRequestQueue (this.getActivity());
        String url = "https://agroplaza.solucionsoftware.co/ModuloPublicaciones/ListarPublicacionesMovil?departamento="+id_departamento;

        JsonObjectRequest solicitud = new JsonObjectRequest (Request.Method.GET, url, null, new Response.Listener<JSONObject> () {
            @Override
            public void onResponse (JSONObject response) {
                listaPublicaciones = new ArrayList<> ();
                listaDescuentos = new ArrayList<> ();

                JSONArray lista_clientes = response.optJSONArray ("registros_publicaciones");
                try {

                    for (int i = 0; i < lista_clientes.length (); i++) {

                        JSONObject temp = lista_clientes.getJSONObject (i);
                        String descuento = "";
                        String id = temp.getString ("id_publicacion");
                        String foto = temp.getString ("imagen");
                        String titulo = temp.getString ("titulo");
                        String precio = temp.getString ("precio");
                        String envio = "";
                        String unidad = temp.getString ("abreviatura");
                        String valor_descuento = temp.getString ("descuento");
                        String descripcion = temp.getString ("descripcion");
                        String stock = temp.getString ("stock");
                        String valor_unidad = temp.getString ("valor_unidad");

                        String valor_envio = temp.getString ("envio");
                        if (valor_envio.equals ("SI")){
                            envio = "Incluye envio";

                        }else if (!valor_envio.equals ("SI")){
                            envio = "";
                        }

                        if (valor_descuento.equals("0")){
                            descuento = "";

                        }else if (!valor_descuento.equals("0")){
                            String descuento_des = temp.getString ("descuento");
                            String id_des = temp.getString ("id_publicacion");
                            String foto_des = temp.getString ("imagen");
                            String titulo_des = temp.getString ("titulo");
                            String precio_des = temp.getString ("precio");
                            String envio_des = "";
                            if (valor_envio.equals ("SI")){
                                envio_des = "Envio gratis";
                            }else if (!valor_envio.equals ("SI")){
                                envio_des = "";
                            }
                            String descripcion_des = temp.getString ("descripcion");
                            Descuentos des = new Descuentos (titulo_des, precio_des, envio_des, descuento_des, foto_des,id_des, descripcion_des);
                            listaDescuentos.add (des);
                        }

                        Publicaciones pub = new Publicaciones (titulo, precio, envio, descuento, foto,id, descripcion, unidad, stock, valor_unidad);
                        listaPublicaciones.add (pub);

                    }
                    adapter = new AdapterPublicaciones (listaPublicaciones);
                    adapter.setOnclickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), DetallePublicacion.class);
                            intent.putExtra("id", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).id);
                            intent.putExtra("titulo", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).titulo);
                            intent.putExtra("unidad", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).unidad);
                            intent.putExtra("precio", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).precio);
                            intent.putExtra("descripcion", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).descripcion);
                            intent.putExtra("stock", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).stock);
                            intent.putExtra("valor_unidad", listaPublicaciones.get(recycler.getChildAdapterPosition(v)).valor_unidad);

                            startActivity(intent);
                        }
                    });
                    recycler.setAdapter (adapter);
                    adapter_des = new AdapterDescuentos (listaDescuentos);
                    adapter_des.setOnclickListener (new View.OnClickListener () {
                        @Override
                        public void onClick (View v) {
                            Intent intent = new Intent(getActivity().getApplicationContext(), DetallePublicacion.class);
                            intent.putExtra("id", listaDescuentos.get (recycler.getChildAdapterPosition (v)).id);
                            intent.putExtra("titulo", listaDescuentos.get (recycler.getChildAdapterPosition (v)).titulo);
                            intent.putExtra("precio", listaDescuentos.get (recycler.getChildAdapterPosition (v)).precio);
                            intent.putExtra("unidad", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).unidad);
                            intent.putExtra("descripcion", listaDescuentos.get (recycler.getChildAdapterPosition (v)).descripcion);
                            intent.putExtra("stock", listaPublicaciones.get (recycler.getChildAdapterPosition (v)).stock);
                            intent.putExtra("valor_unidad", listaPublicaciones.get(recycler.getChildAdapterPosition(v)).valor_unidad);

                            startActivity(intent);
                        }
                    });
                    recycler_descuentos.setAdapter (adapter_des);
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