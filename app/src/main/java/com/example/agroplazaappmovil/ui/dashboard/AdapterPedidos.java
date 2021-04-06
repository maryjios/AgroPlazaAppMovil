package com.example.agroplazaappmovil.ui.dashboard;

import android.content.Context;
import android.graphics.Bitmap;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.cardview.widget.CardView;
import androidx.recyclerview.widget.RecyclerView;

import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.R;

import java.util.ArrayList;

public class AdapterPedidos extends RecyclerView.Adapter<AdapterPedidos.ViewHolderDatos> implements View.OnClickListener {

    ArrayList<Pedidos> listaDatos;
    private View.OnClickListener listener;

    public AdapterPedidos (ArrayList<Pedidos> listaDatos) {
        this.listaDatos = listaDatos;
    }

    @NonNull
    @Override
    public ViewHolderDatos onCreateViewHolder (@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from (parent.getContext ()).inflate (R.layout.item_list_pedido, null, false);
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

    @Override
    public void onClick (View v) {
        if (listener != null) {
            listener.onClick (v);
        }
    }


    /* clase Holder */
    public class ViewHolderDatos extends RecyclerView.ViewHolder {

        ImageView imagenPedido;
        TextView numeroPedido;
        TextView tituloPublicacion;
        TextView fechaPedido;
        TextView estadoPedido;
        LinearLayout contentPedidoEntregado;
        CardView contentEstadoPedido;
        RequestQueue request;
        Context contexto;

        public ViewHolderDatos (@NonNull View itemView) {
            super (itemView);

            numeroPedido = itemView.findViewById (R.id.numeroPedido);
            tituloPublicacion = itemView.findViewById (R.id.tituloPublicacion);
            fechaPedido = itemView.findViewById (R.id.fechaPedido);
            estadoPedido = itemView.findViewById (R.id.estadoPedido);
            contentEstadoPedido = itemView.findViewById (R.id.contentEstadoPedido);
            imagenPedido = itemView.findViewById (R.id.imagenPedido);
            request = Volley.newRequestQueue (itemView.getContext ());
            contexto = itemView.getContext ();

        }

        public void asignarDatos (Pedidos datos) {
            numeroPedido.setText ("Numero de Pedido#: 0000000" + datos.numero_pedido);
            tituloPublicacion.setText (datos.titulo_publicacion);
            fechaPedido.setText (datos.fecha_pedido);
            estadoPedido.setText (datos.estado_pedido);

            if (datos.estado_pedido.equalsIgnoreCase ("EN PROCESO")) {
                contentEstadoPedido.setBackgroundResource (R.color.cardColorYellow);
            } else if (datos.estado_pedido.equalsIgnoreCase ("SOLICITADO")) {
                contentEstadoPedido.setBackgroundResource (R.color.blue);
            } else if (datos.estado_pedido.equalsIgnoreCase ("CANCELADO")) {
                contentEstadoPedido.setBackgroundResource (R.color.red_btn_bg_color);
            } else if (datos.estado_pedido.equalsIgnoreCase ("FINALIZADO")) {
                contentEstadoPedido.setBackgroundResource (R.color.gray);
            } else if (datos.estado_pedido.equalsIgnoreCase ("ENTREGADO")) {
                contentEstadoPedido.setBackgroundResource (R.color.main_green_color);
            }

            String url = "https://agroplaza.solucionsoftware.co/public/dist/img/publicaciones/publicacion" + datos.id_publi + "/" + "foto_1.jpg";
            url = url.replace (" ", "%20");

            final String finalUrl = url;
            ImageRequest imageRequest = new ImageRequest (url,
                    new Response.Listener<Bitmap> () {
                        @Override
                        public void onResponse (Bitmap response) {
                            imagenPedido.setImageBitmap (response);
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
