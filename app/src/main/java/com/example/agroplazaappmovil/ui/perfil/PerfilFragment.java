package com.example.agroplazaappmovil.ui.perfil;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.util.Base64;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.core.graphics.drawable.RoundedBitmapDrawable;
import androidx.core.graphics.drawable.RoundedBitmapDrawableFactory;
import androidx.fragment.app.Fragment;

import com.android.volley.AuthFailureError;
import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.ImageRequest;
import com.android.volley.toolbox.StringRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.Login;
import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.RegistroUsuarios;

import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.HashMap;
import java.util.Hashtable;
import java.util.Map;

import cn.pedant.SweetAlert.SweetAlertDialog;

import static android.app.Activity.RESULT_OK;

public class PerfilFragment extends Fragment {

    View actividad;
    TextView etiqueta_nombre, etiqueta_correo;
    ImageView avatar;
    Bitmap bitmap;
    Button editar_imagen, cancel_edit_imagen, escoger_img;

    private static final int SELECT_FILE = 1;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        actividad = inflater.inflate(R.layout.fragment_perfil, container, false);

        avatar = actividad.findViewById(R.id.imagen_perfil);

        etiqueta_nombre = actividad.findViewById(R.id.nombre_perfil);
        etiqueta_correo = actividad.findViewById(R.id.correo_perfil);

        SharedPreferences persistencia = actividad.getContext().getSharedPreferences("datos_login", Context.MODE_PRIVATE);
        String nombre = persistencia.getString("nombres", "NaN");
        String correo = persistencia.getString("email", "NaN");

        String nom_avatar = persistencia.getString("avatar", "NaN");
        cargarImagenPerfil(nom_avatar);

        etiqueta_nombre.setText(nombre);
        etiqueta_correo.setText(correo);

        Button cerrar_sesion = actividad.findViewById(R.id.btn_cerrar_sesion);
        cerrar_sesion.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                cerrarSesion(persistencia);
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
                desactivar.setConfirmButton("Confirmar", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        desactivarCuenta(persistencia);
                        sDialog.dismiss();
                    }
                });
                desactivar.setCancelButton("Cancelar", new SweetAlertDialog.OnSweetClickListener() {
                    @Override
                    public void onClick(SweetAlertDialog sDialog) {
                        sDialog.dismissWithAnimation();
                    }
                });
                desactivar.show();
            }
        });

        editar_imagen = actividad.findViewById(R.id.btn_edit_imagen);
        cancel_edit_imagen = actividad.findViewById(R.id.cancel_edit_imagen);

        escoger_img = actividad.findViewById(R.id.btn_escoger_imagen);
        escoger_img.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                abrirGaleria();
            }
        });

        editar_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                subirImagen(persistencia);
            }
        });

        cancel_edit_imagen.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                editar_imagen.setVisibility(View.GONE);
                cancel_edit_imagen.setVisibility(View.GONE);
                escoger_img.setVisibility(View.VISIBLE);

                cargarImagenPerfil(nom_avatar);
            }
        });

        return actividad;
    }

    public String obtenerImagenBase64(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }

    private void abrirGaleria() {
        Intent intent = new Intent();
        intent.setType("image/*");
        intent.setAction(Intent.ACTION_GET_CONTENT);
        startActivityForResult(Intent.createChooser(intent, "Select Imagen"), SELECT_FILE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        if (requestCode == SELECT_FILE && resultCode == RESULT_OK && data != null && data.getData() != null) {
            Uri filePath = data.getData();
            try {
                //Cómo obtener el mapa de bits de la Galería
                bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);
                //Configuración del mapa de bits en ImageView
                avatar.setImageBitmap(bitmap);
                ajustarImagenPerfil();

                editar_imagen.setVisibility(View.VISIBLE);
                cancel_edit_imagen.setVisibility(View.VISIBLE);
                escoger_img.setVisibility(View.GONE);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    private void subirImagen(SharedPreferences persistencia) {
        String id_perfil = persistencia.getString("id", "0");

        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Subiendo imagen ...");
        pDialog.setCancelable(false);
        pDialog.show();

        RequestQueue hilo = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = "https://agroplaza.solucionsoftware.co/ModuloUsuarios/ActualizarImagenPerfil";
        StringRequest stringRequest = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String[] mensaje = response.split("\"");
                        Toast.makeText(getActivity(), response.trim(), Toast.LENGTH_LONG).show();
                        if (mensaje[1].equalsIgnoreCase("OK##IMAGE##UPDATE")) {
                            SharedPreferences.Editor editor = persistencia.edit();
                            editor.putString("avatar", "avatar_user_" + id_perfil + ".png");

                            editor.commit();

                            editar_imagen.setVisibility(View.GONE);
                            cancel_edit_imagen.setVisibility(View.GONE);
                            escoger_img.setVisibility(View.VISIBLE);

                            pDialog.dismiss();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.SUCCESS_TYPE)
                                    .setTitleText("Imagen Actualizada!")
                                    .setContentText("Tu imagen de avatar a sido actualizado correctamente.")
                                    .show();
                        } else {
                            pDialog.dismiss();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Ha ocurrido un fallo al guardar la imagen!")
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError volleyError) {
                        Toast.makeText(getContext(), "Error: "+volleyError.getMessage(), Toast.LENGTH_LONG).show();
                        pDialog.dismiss();
                    }
                }){
            @Override
            protected Map<String, String> getParams() throws AuthFailureError {
                //Convertir bits a cadena
                String imagen = obtenerImagenBase64(bitmap);
                Log.i("Base64", imagen);

                //Creación de parámetros
                Map<String,String> params = new Hashtable<String, String>();

                //Agregando de parámetros
                params.put("imagen", imagen);
                params.put("id_perfil", id_perfil);

                //Parámetros de retorno
                return params;
            }
        };

        //Agregar solicitud a la cola
        hilo.add(stringRequest);
    }

    public void cerrarSesion (SharedPreferences persistencia) {
        SharedPreferences.Editor editor = persistencia.edit();

        editor.clear();
        editor.commit();

        Intent intent = new Intent(getActivity().getApplicationContext(), Login.class);
        startActivity(intent);
        getActivity().finish();
    }

    public void cargarImagenPerfil(String archivo) {
        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Cargando ...");
        pDialog.setCancelable(false);
        pDialog.show();

        RequestQueue hilo = Volley.newRequestQueue(getActivity().getApplicationContext());

        String url = "https://agroplaza.solucionsoftware.co/public/dist/img/avatar/" + archivo;
        url = url.replace(" ","%20");

        final String finalUrl = url;
        ImageRequest imageRequest = new ImageRequest (url,
                new Response.Listener<Bitmap>() {
                    @Override
                    public void onResponse(Bitmap response) {
                        avatar.setImageBitmap(response);
                        ajustarImagenPerfil();
                        pDialog.dismiss();
                    }
                }, 0, 0, ImageView.ScaleType.CENTER, null, new Response.ErrorListener() {
            @Override
            public void onErrorResponse(VolleyError error) {
                Toast.makeText(getContext(), "Error: "+error.getMessage(), Toast.LENGTH_LONG).show();
                pDialog.dismiss();
            }
        }
        );

        hilo.add(imageRequest);
    }

    public void ajustarImagenPerfil() {
        Drawable originalDrawable = avatar.getDrawable();
        Bitmap originalBitmap = ((BitmapDrawable) originalDrawable).getBitmap();

        //creamos el drawable redondeado
        RoundedBitmapDrawable roundedDrawable =
                RoundedBitmapDrawableFactory.create(getResources(), originalBitmap);

        //asignamos el CornerRadius
        roundedDrawable.setCornerRadius(originalBitmap.getHeight());

        avatar.setImageDrawable(roundedDrawable);
    }

    public void desactivarCuenta(SharedPreferences persistencia) {
        String id_perfil = persistencia.getString("id", "0");

        SweetAlertDialog pDialog = new SweetAlertDialog(getActivity(), SweetAlertDialog.PROGRESS_TYPE);
        pDialog.getProgressHelper().setBarColor(Color.GREEN);
        pDialog.setTitleText("Espera ...");
        pDialog.setCancelable(false);
        pDialog.show();

        RequestQueue hilo = Volley.newRequestQueue(getContext());
        String url = "https://agroplaza.solucionsoftware.co/ModuloUsuarios/DesactivarCuentaMovil";

        StringRequest solicitud = new StringRequest(Request.Method.POST, url,
                new Response.Listener<String>() {
                    @Override
                    public void onResponse(String response) {

                        String[] mensaje = response.split("\"");

                        if (mensaje[1].equalsIgnoreCase("OK##STATUS##UPDATE")) {
                            SharedPreferences.Editor editor = persistencia.edit();

                            editor.clear();
                            editor.commit();

                            pDialog.dismiss();

                            Intent intent = new Intent(getActivity(), Login.class);
                            intent.putExtra("mensaje", "desactivado");
                            startActivity(intent);
                            getActivity().finish();
                        } else if (mensaje[1].equalsIgnoreCase("ERROR##UPDATE")) {
                            pDialog.dismiss();
                            new SweetAlertDialog(getActivity(), SweetAlertDialog.ERROR_TYPE)
                                    .setTitleText("Oops...")
                                    .setContentText("Hubo un error en el servidor!")
                                    .show();
                        }
                    }
                },
                new Response.ErrorListener() {
                    @Override
                    public void onErrorResponse(VolleyError error) {
                        // Codigo de error del servidor
                        // Se ejecuta cuando no llega el tipo solicitado String.
                        Toast.makeText(getActivity(), "Error Servidor: " + error.getMessage(), Toast.LENGTH_LONG).show();
                        if (error.getMessage() != null) {
                            Log.i("Error Servidor: ", error.getMessage());
                        } else {
                            Log.i("Error Servidor: ", "Error desconocido");
                        }
                    }
                }) {
            protected Map<String, String> getParams() {
                Map<String, String> parametros = new HashMap<String, String>();
                parametros.put("id_perfil", id_perfil);
                return parametros;
            }
        };
        hilo.add(solicitud);
    }
}