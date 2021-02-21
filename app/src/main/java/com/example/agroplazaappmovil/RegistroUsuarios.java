package com.example.agroplazaappmovil;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.view.View;
import android.widget.Adapter;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Spinner;
import android.widget.Toast;

public class RegistroUsuarios extends AppCompatActivity {

    Spinner spinner;
    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.registro_usuarios);
        spinner = findViewById (R.id.spinner);

        String [] generos = {"Seleccione Genero", "Mujer", "Hombre", "Otro"};


        ArrayAdapter<String> adapter = new ArrayAdapter<> (this, R.layout.spinner_generos, generos);
       spinner.setAdapter (adapter);

    }

}