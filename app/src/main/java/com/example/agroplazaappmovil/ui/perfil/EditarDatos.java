package com.example.agroplazaappmovil.ui.perfil;

import androidx.activity.OnBackPressedCallback;
import androidx.appcompat.app.AppCompatActivity;
import com.example.agroplazaappmovil.R;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageView;

public class EditarDatos extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_editar_datos);

        ImageView regresar;
        regresar = findViewById(R.id.perfil_atras);

        regresar.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                EditarDatos.super.onBackPressed();
            }
        });
    }
}