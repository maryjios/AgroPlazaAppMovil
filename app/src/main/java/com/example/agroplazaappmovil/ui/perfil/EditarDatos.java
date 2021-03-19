package com.example.agroplazaappmovil.ui.perfil;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.example.agroplazaappmovil.R;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;

public class EditarDatos extends AppCompatActivity {

    EditText edit_documento, edit_nombres, edit_apellidos, edit_direccion, edit_telefono;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_datos);

        edit_documento = findViewById(R.id.editar_documento);
        edit_nombres = findViewById(R.id.editar_nombres);
        edit_apellidos = findViewById(R.id.editar_apellidos);
        edit_direccion = findViewById(R.id.editar_direccion);
        edit_telefono = findViewById(R.id.editar_telefono);

        Button regresar = findViewById(R.id.btn_atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarDatos.super.onBackPressed();
            }
        });

        SharedPreferences persistencia = getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String documento = persistencia.getString("documento", "");
        String nombres = persistencia.getString("nombres", "");
        String apellidos = persistencia.getString("apellidos", "");
        String direccion = persistencia.getString("direccion", "");
        String telefono = persistencia.getString("telefono", "");

        edit_documento.setText(documento);
        edit_nombres.setText(nombres);
        edit_apellidos.setText(apellidos);
        edit_direccion.setText(direccion);
        edit_telefono.setText(telefono);
    }
}