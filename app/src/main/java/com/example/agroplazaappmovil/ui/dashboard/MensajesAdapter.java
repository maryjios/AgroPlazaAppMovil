package com.example.agroplazaappmovil.ui.dashboard;

import android.content.Context;
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

    public MensajesAdapter (Context context, List<Mensajes> mensajes) {
        mensajesList = mensajes;
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

        holder.mensajeTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });

        holder.fechaTextView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                remove(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return mensajesList.size();
    }

    @Override
    public int getItemViewType(int position) {
        Mensajes mensaje = mensajesList.get(position);

        if (mensaje.getNombreRemitente ().equals("Mary")) {
            return REMITENTE;
        } else {
            return DESTINARIO;
        }

    }

}