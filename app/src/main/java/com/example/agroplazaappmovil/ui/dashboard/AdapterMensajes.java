package com.example.agroplazaappmovil.ui.dashboard;

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

public class AdapterMensajes extends RecyclerView.Adapter<AdapterMensajes.ViewHolderDatos>{

    ArrayList<Mensajes> listaMensajes;
    public AdapterMensajes (ArrayList<Mensajes> listaMensajes) {
        this.listaMensajes= listaMensajes;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_mensaje,null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarMensajes(listaMensajes.get(position));
    }

    @Override
    public int getItemCount() {
        return listaMensajes.size();
    }


    /* clase Holder */
    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        // ImageView avatar_destinario;
        TextView nombre_destinario;
        TextView mensaje_destinario;
        TextView fecha_destinario;
        TextView mensaje_remitente;
        TextView fecha_remitente;
        RequestQueue request;
        Context contexto;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            mensaje_remitente = itemView.findViewById(R.id.mensaje_remitente);
            nombre_destinario = itemView.findViewById(R.id.nombre_destinario);
            mensaje_destinario = itemView.findViewById(R.id.mensaje_destinario);
            fecha_destinario = itemView.findViewById(R.id.fecha_destinario);
            //  avatar_destinario = itemView.findViewById(R.id.avatar_destinario);
            fecha_remitente = itemView.findViewById(R.id.fecha_remitente);
            request = Volley.newRequestQueue(itemView.getContext());
            contexto = itemView.getContext();
        }

        public void asignarMensajes(Mensajes datos) {

            mensaje_remitente.setText(datos.mensaje_remitente);
            nombre_destinario.setText(datos.nombre_destinario);
            mensaje_destinario.setText(datos.mensaje_destinario);
            fecha_destinario.setText(datos.fecha_destinario);
            fecha_destinario.setText(datos.fecha_destinario);
            fecha_destinario.setText(datos.fecha_destinario);


        }
    }
}
