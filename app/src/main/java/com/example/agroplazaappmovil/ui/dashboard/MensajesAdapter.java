package com.example.agroplazaappmovil.ui.dashboard;

import android.content.Context;
import android.content.SharedPreferences;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;
import androidx.recyclerview.widget.RecyclerView;

import com.example.agroplazaappmovil.R;
import java.util.List;

public class MensajesAdapter extends RecyclerView.Adapter<MensajesAdapter.ViewHolder> {

    List<Mensajes> mensajesList;

    public static final int REMITENTE = 0;
    public static final int DESTINARIO = 1;

    int cliente_id;

    public MensajesAdapter (Context context, List<Mensajes> mensajes, int id_usuario) {
        mensajesList = mensajes;
        cliente_id = id_usuario;
    }

    public static class ViewHolder extends RecyclerView.ViewHolder {
        TextView mensajeTextView;
        TextView fechaTextView;

        public ViewHolder(LinearLayout v) {
            super(v);
            mensajeTextView = (TextView) v.findViewById(R.id.mensaje);
            fechaTextView = (TextView) v.findViewById(R.id.fecha_mensaje);

        }
    }

    @Override
    public MensajesAdapter.ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        if (viewType == 1) {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_destinario, parent, false);
            ViewHolder vh = new ViewHolder((LinearLayout) v);
            return vh;
        } else {
            View v = LayoutInflater.from(parent.getContext()).inflate(R.layout.chat_item_remitente, parent, false);
            ViewHolder vh = new ViewHolder((LinearLayout) v);
            return vh;
        }
    }

    public void remove(int pos) {
        int position = pos;
        mensajesList.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position, mensajesList.size());

    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
        holder.mensajeTextView.setText(mensajesList.get(position).getMensaje ());
        holder.fechaTextView.setText(mensajesList.get(position).getFecha ());
    }

    @Override
    public int getItemCount() {
        return mensajesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensajes mensaje = mensajesList.get(position);

        if (mensaje.getId_usuario () == cliente_id) {
            return REMITENTE;
        } else {
            return DESTINARIO;
        }

    }

}