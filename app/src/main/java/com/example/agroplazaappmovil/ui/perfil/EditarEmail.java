package com.example.agroplazaappmovil.ui.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.example.agroplazaappmovil.R;

public class EditarEmail extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_email);

        SharedPreferences persistencia = getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String correo = persistencia.getString("email", "NaN");

        TextView correo_actual = findViewById(R.id.correo_actual);
        correo_actual.setText(correo);

        Button regresar = findViewById(R.id.btn_atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarEmail.super.onBackPressed();
            }
        });
    }
}