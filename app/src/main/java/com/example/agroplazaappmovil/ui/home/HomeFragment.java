package com.example.agroplazaappmovil.ui.home;

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

        TextView etiqueta = actividad.findViewById(R.id.text_home);
        etiqueta.setText("Modificado desde el fragmento ^w^");

        return actividad;
    }
}