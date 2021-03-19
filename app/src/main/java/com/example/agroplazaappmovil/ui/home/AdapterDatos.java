package com.example.agroplazaappmovil.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.R;

import java.util.ArrayList;

public class AdapterDatos extends RecyclerView.Adapter<AdapterDatos.ViewHolderDatos> {

    ArrayList<Productos> listaDatos;

    public AdapterDatos (ArrayList<Productos> listaDatos) {
        this.listaDatos= listaDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_recycler,null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaDatos.get(position));
    }

    @Override
    public int getItemCount() {
        return listaDatos.size();
    }

    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView descripcion;
        ImageView imagen;
        RequestQueue request;
        Context contexto;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            titulo = itemView.findViewById(R.id.titulo_publicacion);
            descripcion = itemView.findViewById(R.id.descripcion_publicacion);
            imagen = itemView.findViewById(R.id.imagen_publicacion);
            request = Volley.newRequestQueue(itemView.getContext());
            contexto = itemView.getContext();
        }

        public void asignarDatos(Productos datos) {
            titulo.setText(datos.titulo);
            descripcion.setText(datos.descripcion);

            String url = "https://agroplaza.solucionsoftware.co/public/dist/img/publicaciones/publicacion"+datos.id+"/"+datos.foto;
            url = url.replace(" ","%20");

            final String finalUrl = url;
            ImageRequest imageRequest = new ImageRequest (url,
                    new Response.Listener<Bitmap>() {
                        @Override
                        public void onResponse(Bitmap response) {
                            imagen.setImageBitmap(response);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
                @Override
                public void onErrorResponse(VolleyError error) {
                    Toast.makeText(contexto, "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                }
            }
            );
            request.add(imageRequest);

        }
    }
}
