package com.example.agroplazaappmovil.ui.perfil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agroplazaappmovil.R;

public class EditarPassword extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_password);

        Button regresar = findViewById(R.id.btn_atras);
        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarPassword.super.onBackPressed();
            }
        });
    }
}