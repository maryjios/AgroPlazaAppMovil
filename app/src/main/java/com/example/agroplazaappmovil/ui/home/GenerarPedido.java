package com.example.agroplazaappmovil.ui.home;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agroplazaappmovil.R;

public class GenerarPedido extends AppCompatActivity {
     TextView cantidad;
     Button btnaumntar;
     Button btndisminuir;
     Button btn_atras_compra;
     int valor =1;
    @Override

    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_generar_pedido);

        cantidad=findViewById(R.id.cantidad);
        btnaumntar=findViewById(R.id.btnaumentar);
        btndisminuir=findViewById(R.id.btndisminuir);
        btn_atras_compra=findViewById(R.id.btn_atras_compra);

        btn_atras_compra.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                volver_detalle();

            }
        });

        btnaumntar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantidad.setText(String.valueOf(++valor));

            }
        });
        btndisminuir.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cantidad.setText(String.valueOf(--valor));
            }
        });

    }
    public  void volver_detalle(){
        Intent intencion1=new Intent( this , DetallePublicacion.class);
        startActivity(intencion1);
        finish();


    }
}