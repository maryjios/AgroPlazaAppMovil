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

public class AdapterPreguntasRespuestas extends RecyclerView.Adapter<AdapterPreguntasRespuestas.ViewHolderDatos>{

    ArrayList<PreguntasRespuestas> listaPreguntasRespuestas;
    private View.OnClickListener listener;
    public AdapterPreguntasRespuestas (ArrayList<PreguntasRespuestas> listaPreguntasRespuestas) {
        this.listaPreguntasRespuestas= listaPreguntasRespuestas;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_list_publicaciones,null, false);
        return new ViewHolderDatos(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ViewHolderDatos holder, int position) {
        holder.asignarDatos(listaPreguntasRespuestas.get(position));
    }

    @Override
    public int getItemCount() {
        return listaPreguntasRespuestas.size();
    }

    
    /* clase Holder */
    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        TextView nombre_cliente;
        TextView pregunta_cliente;
        TextView fecha_pregunta_cliente;
        TextView descuento;
        TextView respuesta_vendedor;
        TextView fecha_respuesta_vendedor;

        public ViewHolderDatos(@NonNull View itemView) {
            super(itemView);
            nombre_cliente = itemView.findViewById(R.id.nombre_cliente);
            pregunta_cliente = itemView.findViewById(R.id.pregunta_cliente);
            fecha_pregunta_cliente = itemView.findViewById(R.id.fecha_pregunta_cliente);
            respuesta_vendedor = itemView.findViewById(R.id.respuesta_vendedor);
            fecha_respuesta_vendedor = itemView.findViewById(R.id.fecha_respuesta_vendedor);
        }

        public void asignarDatos(PreguntasRespuestas datos) {
            nombre_cliente.setText(datos.nombre_cliente);
            pregunta_cliente.setText(datos.pregunta_cliente);
            fecha_pregunta_cliente.setText(datos.fecha_pregunta_cliente);
            respuesta_vendedor.setText(datos.respuesta_vendedor);
            fecha_respuesta_vendedor.setText(datos.fecha_respuesta_vendedor);

        }
    }
}
