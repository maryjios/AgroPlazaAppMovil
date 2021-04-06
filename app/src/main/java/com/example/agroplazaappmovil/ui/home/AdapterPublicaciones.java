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

public class AdapterPublicaciones extends RecyclerView.Adapter<AdapterPublicaciones.ViewHolderDatos> implements View.OnClickListener {

    ArrayList<Publicaciones> listaDatos;
    private View.OnClickListener listener;

    public AdapterPublicaciones (ArrayList<Publicaciones> listaDatos) {
        this.listaDatos = listaDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_list_publicaciones, null, false);
        view.setOnClickListener (this);
        return new ViewHolderDatos (view);
    }

    @Override
    public void onBindViewHolder (@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos (listaDatos.get (position));
    }

    @Override
    public int getItemCount () {
        return listaDatos.size ();
    }

    public void setOnclickListener (View.OnClickListener listener) {
        this.listener = listener;
    }

    public void filterList(ArrayList<Publicaciones> filteredList) {
        this.listaDatos = filteredList;
        notifyDataSetChanged();
    }

    public void clear(){
        listaDatos.clear();
        listaDatos.addAll (listaDatos);
        notifyDataSetChanged();
    }

    @Override
    public void onClick (View v) {
        if (this.listener != null) {
            this.listener.onClick (v);
        }
    }
    /* clase Holder */
    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView titulo;
        TextView precio;
        TextView envio;
        TextView descuento;
        ImageView imagen;
        RequestQueue request;
        Context contexto;

        public ViewHolderDatos (@NonNull View itemView) {
            super (itemView);

            titulo = itemView.findViewById (R.id.titulo_publicacion);
            precio = itemView.findViewById (R.id.precio_publicacion);
            envio = itemView.findViewById (R.id.envio_publicacion);
            descuento = itemView.findViewById (R.id.descuento_publicacion);
            imagen = itemView.findViewById (R.id.imagen_publicacion);
            request = Volley.newRequestQueue (itemView.getContext ());
            contexto = itemView.getContext ();
        }

        public void asignarDatos (Publicaciones datos) {

            String tiutoLengt = datos.titulo;

            if (tiutoLengt.length () >= 12) {
                titulo.setText(datos.titulo.substring(0, 12) + "...");
            }else{
                titulo.setText(datos.titulo);
            }
            precio.setText (datos.precio);
            envio.setText (datos.envio);
            descuento.setText (datos.descuento);


            String url = "https://agroplaza.solucionsoftware.co/public/dist/img/publicaciones/publicacion" + datos.id + "/" + datos.foto;
            url = url.replace (" ", "%20");

            final String finalUrl = url;
            ImageRequest imageRequest = new ImageRequest (url,
                    new Response.Listener<Bitmap> () {
                        @Override
                        public void onResponse (Bitmap response) {
                            imagen.setImageBitmap (response);
                        }
                    }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener () {
                @Override
                public void onErrorResponse (VolleyError error) {
                    Toast.makeText (contexto, "Error: " + error.getMessage (), Toast.LENGTH_LONG).show ();
                }
            }
            );
            request.add (imageRequest);

        }
    }

}
