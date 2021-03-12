package com.example.agroplazaappmovil.ui.perfil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import androidx.fragment.app.Fragment;

import com.example.agroplazaappmovil.Login;
import com.example.agroplazaappmovil.R;

public class PerfilFragment extends Fragment {

    View actividad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actividad = inflater.inflate(R.layout.fragment_perfil, container, false);

        Button cerrar_sesion = actividad.findViewById(R.id.btn_cerrar_sesion);
        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        return actividad;
    }

    public void cerrarSesion () {
        SharedPreferences persistencia = actividad.getContext().getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        SharedPreferences.Editor editor = persistencia.edit();

        editor.clear();
        editor.commit();

        Intent intent = new Intent(getActivity().getApplicationContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }
}