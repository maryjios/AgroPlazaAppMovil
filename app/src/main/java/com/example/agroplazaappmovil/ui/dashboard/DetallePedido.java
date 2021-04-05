package com.example.agroplazaappmovil.ui.dashboard;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.Toast;

import androidx.appcompat.app.AppCompatActivity;

import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.ui.perfil.EditarDatos;

public class DetallePedido extends AppCompatActivity {
    String id_pedido, titulo_pedido, fecha_pedido, estado_pedido;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_detalle_pedido);

        Intent intent = getIntent ();
        id_pedido = intent.getStringExtra ("id");
        titulo_pedido = intent.getStringExtra ("titulo");
        fecha_pedido = intent.getStringExtra ("fecha");
        estado_pedido = intent.getStringExtra ("estado");

        LinearLayout cPedidoEntregado = findViewById (R.id.contentPedidoEntregado);
        if (estado_pedido.equalsIgnoreCase ("ENTREGADO")){
            cPedidoEntregado.setVisibility (View.VISIBLE);
        }else{
            cPedidoEntregado.setVisibility (View.GONE);
        }

        Button btnChatPedido = findViewById (R.id.btnChatPedido);
        btnChatPedido.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Intent intent = new Intent (getApplicationContext (), Chat_Activity.class);
                intent.putExtra ("numero_pedido", id_pedido);
                intent.putExtra ("titulo_publicacion", titulo_pedido);
                startActivity (intent);
            }
        });

        ImageButton regresar = findViewById(R.id.btn_Atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DetallePedido.super.onBackPressed();
            }
        });
    }
}