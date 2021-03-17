package com.example.agroplazaappmovil.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import com.example.agroplazaappmovil.R;

public class HomeFragment extends Fragment {
    View actividad;

    public View onCreateView(@NonNull LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actividad = inflater.inflate(R.layout.fragment_home, container, false);

        SharedPreferences persistencia = actividad.getContext().getSharedPreferences("datos_login", Context.MODE_PRIVATE);

        TextView nombreUser = actividad.findViewById(R.id.textBienvenidaPincipal);
        nombreUser.setText("Hola, "+""+persistencia.getString ("nombres", "").toString ()+","+" Bienvenido");
        /*
        TextView ubicacion = actividad.findViewById(R.id.textlUbicacion);
        ubicacion.setText("Ahora mismo estas en - Pereira,Risaralda"); */
        return actividad;
    }
}