package com.example.agroplazaappmovil.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.ui.home.DetallePublicacion;

public class Chat_Activity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_chat);

        Button regresar = findViewById (R.id.btn_atras_chat);
        regresar.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Chat_Activity.super.onBackPressed ();
            }
        });
    }
}