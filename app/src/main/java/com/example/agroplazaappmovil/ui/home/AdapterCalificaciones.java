package com.example.agroplazaappmovil.ui.home;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.RatingBar;
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

public class AdapterCalificaciones extends RecyclerView.Adapter<AdapterCalificaciones.ViewHolderDatos>{

    ArrayList<Calificaciones> listaDatos;

    public AdapterCalificaciones (ArrayList<Calificaciones> listaDatos) {
        this.listaDatos = listaDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_list_calificacion, null, false);
        return new ViewHolderDatos (view);
    }

    @Override
    public void onBindViewHolder(@NonNull AdapterCalificaciones.ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaDatos.get(position));
    }


    @Override
    public int getItemCount () {
        return listaDatos.size ();
    }

    /* clase Holder */
    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombre_usuario;
        RatingBar total_estrellas;
        TextView fecha_calificacion;
        TextView descripcion_calificacion;
        ImageView foto_evidencia;
        RequestQueue request;
        Context contexto;

        public ViewHolderDatos (@NonNull View itemView) {
            super (itemView);

            nombre_usuario = itemView.findViewById (R.id.nombre_usuario);
            total_estrellas = itemView.findViewById (R.id.total_estrellas);
            fecha_calificacion = itemView.findViewById (R.id.fecha_calificacion);
            descripcion_calificacion = itemView.findViewById (R.id.descripcion_calificacion);
            foto_evidencia = itemView.findViewById (R.id.foto_evidencia);
            request = Volley.newRequestQueue (itemView.getContext ());
            contexto = itemView.getContext ();
        }

        public void asignarDatos (Calificaciones datos) {

            nombre_usuario.setText (datos.nombre_usuario);
            total_estrellas.setRating (Integer.parseInt (datos.total_estrellas));
            fecha_calificacion.setText (datos.fecha_calificacion);
            descripcion_calificacion.setText (datos.descripcion_calificacion);

            /* String url = "https://agroplaza.solucionsoftware.co/public/dist/img/publicaciones/publicacion" + datos.id + "/" + datos.foto;
            url = url.replace (" ", "%20");

            final String finalUrl = url;
            ImageRequest imageRequest = new ImageRequest (url,
                    new Response.Listener<Bitmap> () {
                        @Override
                        public void onResponse (Bitmap response) {
                            foto_evidencia.setImageBitmap (response);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener () {
                @Override
                public void onErrorResponse (VolleyError error) {
                    Toast.makeText (contexto, "Error: " + error.getMessage (), Toast.LENGTH_LONG).show ();
                }
            }
            );
            request.add (imageRequest); */

        }
    }

}
