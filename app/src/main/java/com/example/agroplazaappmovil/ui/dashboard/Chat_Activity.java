package com.example.agroplazaappmovil.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.Toast;

import com.android.volley.Request;
import com.android.volley.RequestQueue;
import com.android.volley.Response;
import com.android.volley.VolleyError;
import com.android.volley.toolbox.JsonObjectRequest;
import com.android.volley.toolbox.Volley;
import com.example.agroplazaappmovil.R;
import com.example.agroplazaappmovil.ui.home.AdapterDescuentos;
import com.example.agroplazaappmovil.ui.home.AdapterPublicaciones;
import com.example.agroplazaappmovil.ui.home.Descuentos;
import com.example.agroplazaappmovil.ui.home.DetallePublicacion;
import com.example.agroplazaappmovil.ui.home.Publicaciones;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class Chat_Activity extends AppCompatActivity {

    ArrayList<Mensajes> listaMensajes;

    RecyclerView recycler;
    AdapterMensajes adapter;

    private static final String SERVER_URI = "ws://18.221.49.32:8083/mqtt";
    private static final String TOPIC = "prueba_chat";
    private static final String TAG = "PruebaMqtt";

    private MqttAndroidClient mqttAndroidClient;

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_chat);

        ListarMensajes ();
        Button regresar = findViewById (R.id.btn_atras_chat);
        regresar.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                Chat_Activity.super.onBackPressed ();
            }
        });

        SharedPreferences persistencia = getSharedPreferences ("datos_login", Context.MODE_PRIVATE);
        String client_id = "user_id_" + persistencia.getString ("id", "NaN");

        conexionMqtt (client_id);

        EditText txt_mensage = findViewById (R.id.campo_mensaje);

        ImageView enviar_mensaje = findViewById (R.id.btn_enviar_mensaje);
        enviar_mensaje.setOnClickListener (new View.OnClickListener () {
            @Override
            public void onClick (View v) {
                String[] nom_usuario = persistencia.getString ("nombres", "NaN").split (" ");
                String[] ape_usuario = persistencia.getString ("apellidos", "NaN").split (" ");
                String usuario = nom_usuario[0] + " " + ape_usuario[0];

                String mensaje = txt_mensage.getText ().toString ();
                txt_mensage.setText ("");

                SimpleDateFormat dateFormat = new SimpleDateFormat ("yyyy-MM-dd", Locale.getDefault ());
                Date date = new Date ();
                String fecha = dateFormat.format (date) + " " + date.getHours () + ":" + date.getMinutes () + ":" + date.getSeconds ();

                String avatar = persistencia.getString ("avatar", "NaN");

                JSONObject datos_mensaje = new JSONObject ();
                try {
                    datos_mensaje.put ("id", client_id);
                    datos_mensaje.put ("usuario", usuario);
                    datos_mensaje.put ("mensaje", mensaje);
                    datos_mensaje.put ("fecha", fecha);
                    datos_mensaje.put ("avatar", avatar);

                    Log.i (TAG, datos_mensaje.toString ());
                    publishMessage (datos_mensaje);
                    mostrarMensaje (datos_mensaje);
                } catch (JSONException e) {
                    e.printStackTrace ();
                }

            }
        });
    }

    public void ListarMensajes () {

        recycler = findViewById (R.id.mi_recycler_chat);
        recycler.setLayoutManager (new LinearLayoutManager (getApplicationContext (), LinearLayoutManager.VERTICAL, false));

        listaMensajes = new ArrayList<> ();


        String nombre_destinario = "Mary Jimenez";
        String mensaje_destinario = "Hola, me gustaria saber cuando llegan los bananos";
        String fecha_destinario = "Ayer";
        String mensaje_remitente = "No sea marika que ayer ke llegaron sus bananos";
        String fecha_remitente = "Justo Ahora";


        Mensajes mensajito = new Mensajes (nombre_destinario, mensaje_destinario, fecha_destinario, mensaje_remitente, fecha_remitente);
        listaMensajes.add (mensajito);

        adapter = new AdapterMensajes (listaMensajes);
        recycler.setAdapter (adapter);
    }

    public void conexionMqtt (String client_id) {
        Log.i (TAG, "onCreate: client_Id: " + client_id);

        mqttAndroidClient = new MqttAndroidClient (getApplicationContext (), SERVER_URI, client_id);
        mqttAndroidClient.setCallback (new MqttCallbackExtended () {
            @Override
            public void connectComplete (boolean reconnect, String serverURI) {
                if (reconnect) {
                    Log.i (TAG, "Reconnected to : " + serverURI);
                    // Because Clean Session is true, we need to re-subscribe
                    subscribeToTopic ();
                } else {
                    Log.i (TAG, "Connected to: " + serverURI);
                }
            }

            @Override
            public void connectionLost (Throwable cause) {
                Log.e (TAG, "The Connection was lost.");
            }

            @Override
            public void messageArrived (String topic, MqttMessage message) throws Exception {
                Log.i (TAG, "Incoming message: " + new String (message.getPayload ()));
            }

            @Override
            public void deliveryComplete (IMqttDeliveryToken token) {

            }
        });

        MqttConnectOptions mqttConnectOptions = new MqttConnectOptions ();
        mqttConnectOptions.setAutomaticReconnect (true);
        mqttConnectOptions.setCleanSession (false);

        try {
            //addToHistory("Connecting to " + serverUri);
            mqttAndroidClient.connect (mqttConnectOptions, null, new IMqttActionListener () {
                @Override
                public void onSuccess (IMqttToken asyncActionToken) {
                    DisconnectedBufferOptions disconnectedBufferOptions = new DisconnectedBufferOptions ();
                    disconnectedBufferOptions.setBufferEnabled (true);
                    disconnectedBufferOptions.setBufferSize (100);
                    disconnectedBufferOptions.setPersistBuffer (false);
                    disconnectedBufferOptions.setDeleteOldestMessages (false);
                    mqttAndroidClient.setBufferOpts (disconnectedBufferOptions);
                    subscribeToTopic ();
                }

                @Override
                public void onFailure (IMqttToken asyncActionToken, Throwable exception) {
                    Log.e (TAG, "Failed to connect to: " + SERVER_URI);
                }
            });


        } catch (MqttException ex) {
            ex.printStackTrace ();
        }
    }

    public void subscribeToTopic () {
        try {
            mqttAndroidClient.subscribe (TOPIC, 0, null, new IMqttActionListener () {
                @Override
                public void onSuccess (IMqttToken asyncActionToken) {
                    Log.i (TAG, "Subscribed!");
                }

                @Override
                public void onFailure (IMqttToken asyncActionToken, Throwable exception) {
                    Log.e (TAG, "Failed to subscribe");
                }
            });

        } catch (MqttException ex) {
            Log.e (TAG, "Exception whilst subscribing");
            ex.printStackTrace ();
        }
    }

    public void publishMessage (JSONObject mensaje) {

        try {
            MqttMessage message = new MqttMessage ();
            message.setPayload (mensaje.toString ().getBytes ());
            mqttAndroidClient.publish (TOPIC, message);
            Log.i (TAG, "Message Published");
            if (!mqttAndroidClient.isConnected ()) {
                Log.i (TAG, mqttAndroidClient.getBufferedMessageCount () + " messages in buffer.");
            }
        } catch (MqttException e) {
            Log.e (TAG, "Error Publishing: " + e.getMessage ());
            e.printStackTrace ();
        }
    }

    public void mostrarMensaje (JSONObject datos) {
        RelativeLayout mensage = new RelativeLayout (this);

    }
}