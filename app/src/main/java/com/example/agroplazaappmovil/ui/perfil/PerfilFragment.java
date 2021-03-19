package com.example.agroplazaappmovil.ui.perfil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import androidx.fragment.app.Fragment;

import com.example.agroplazaappmovil.Login;
import com.example.agroplazaappmovil.R;

import cn.pedant.SweetAlert.SweetAlertDialog;

public class PerfilFragment extends Fragment {

    View actividad;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actividad = inflater.inflate(R.layout.fragment_perfil, container, false);

        TextView etiqueta_nombre, etiqueta_correo;
        etiqueta_nombre = actividad.findViewById(R.id.nombre_perfil);
        etiqueta_correo = actividad.findViewById(R.id.correo_perfil);

        SharedPreferences persistencia = actividad.getContext().getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String nombre = persistencia.getString("nombres", "NaN");
        String correo = persistencia.getString("email", "NaN");

        etiqueta_nombre.setText(nombre);
        etiqueta_correo.setText(correo);

        Button cerrar_sesion = actividad.findViewById(R.id.btn_cerrar_sesion);
        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion();
            }
        });

        Button editar_perfil = actividad.findViewById(R.id.btn_editar_datos);
        editar_perfil.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditarDatos.class);
                startActivity(intent);
            }
        });

        Button cambiar_ciudad = actividad.findViewById(R.id.btn_cambiar_ciudad);
        cambiar_ciudad.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditarCiudad.class);
                startActivity(intent);
            }
        });

        Button cambiar_correo = actividad.findViewById(R.id.btn_cambiar_email);
        cambiar_correo.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditarEmail.class);
                startActivity(intent);
            }
        });

        Button cambiar_password = actividad.findViewById(R.id.btn_cambiar_password);
        cambiar_password.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity().getApplicationContext(), EditarPassword.class);
                startActivity(intent);
            }
        });

        Button desactivar_cuenta = actividad.findViewById(R.id.btn_desactivar);
        desactivar_cuenta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                SweetAlertDialog desactivar = new SweetAlertDialog(getActivity(), SweetAlertDialog.WARNING_TYPE);
                desactivar.setTitleText("¿Estas seguro?");
                desactivar.setContentText("Si desactivas tu cuenta no podras acceder a la aplicacion (Si quieres volver a activarlo debes contactar con administracion.)");
                desactivar.setCancelText("No, gracias");
                desactivar.setConfirmText("Si, quiero hacerlo!");
                desactivar.setConfirmButton("Confirmar", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Toast.makeText(getActivity(), "Se confirmo", Toast.LENGTH_SHORT).show();
                        sDialog.cancel();
                    }
                });
                desactivar.setCancelButton("Cancelar", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        Toast.makeText(getActivity(), "Se cancelo", Toast.LENGTH_SHORT).show();
                        sDialog.cancel();
                    }
                });
                desactivar.show();

                desactivar.getButton(SweetAlertDialog.BUTTON_CANCEL).setBackgroundColor(Color.GRAY);
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