package com.example.agroplazaappmovil.ui.dashboard;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DefaultItemAnimator;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RelativeLayout;

import com.example.agroplazaappmovil.R;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.DisconnectedBufferOptions;
import org.eclipse.paho.client.mqttv3.MqttCallbackExtended;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Locale;

public class Chat_Activity extends AppCompatActivity {

    RecyclerView recycler;
    RecyclerView.Adapter mensajesAdaptar;
    RecyclerView.LayoutManager mensajesLayoutManager;
    List<Mensajes> mensajeList = new ArrayList<Mensajes> ();

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

        recycler = (RecyclerView) findViewById(R.id.mi_recycler_chat);
        recycler.setHasFixedSize(true);

        mensajesLayoutManager = new LinearLayoutManager (this);
        recycler.setLayoutManager(mensajesLayoutManager);

        mensajesAdaptar = new MensajesAdapter (getBaseContext(), mensajeList);
        recycler.setAdapter(mensajesAdaptar);

        mensajeList.add(new Mensajes(1,"Wenas", "Ayer","Mary"));
        mensajeList.add(new Mensajes(2,"Gus mornig", "Ayer","Usuario X"));
        mensajeList.add(new Mensajes(3,"Cuando me envia mi pedido", "Ayer","Mary"));
        mensajeList.add(new Mensajes(4,"Mañana", "Ayer","Usuario X"));
        mensajeList.add(new Mensajes(5,"Ah weno, ta bien ps.", "Ayer","Mary"));
        mensajeList.add(new Mensajes(6,"A las 7 AM, sdihahfahdahkhkskhkhfkdakfdakjczcjkzkjcadjkcdjcjd<c", "Ayer","Usuario X"));
        mensajeList.add(new Mensajes(7,"Ta muy temprano esa hora we", "Ayer","Mary"));
        mensajeList.add(new Mensajes(8,"Ps me vale mergas", "Ayer","Usuario X"));
        mensajeList.add(new Mensajes(9,"Devolucion de dinero porfa", "Ayer","Mary"));
        mensajeList.add(new Mensajes(10,"No se puede", "Ayer","Usuario X"));
        mensajeList.add(new Mensajes(11,"Si se puede viejo lesbiano", "Ayer","Mary"));
        mensajeList.add(new Mensajes(12,"Te reprendo satanas", "Ayer","Usuario X"));
        mensajeList.add(new Mensajes(13,"Bueno", "Ayer","Mary"));
        mensajeList.add(new Mensajes(14,"Ok", "Ayer","Usuario X"));
        mensajeList.add(new Mensajes(15,"A las 7 entonces estare esperando mi pedido, sdhsdsdsdsd", "Ayer","Mary"));
        mensajeList.add(new Mensajes(16,"Ok", "Ayer","Usuario X"));
        mensajeList.add(new Mensajes(17,"Ajm", "Ayer","Mary"));
        mensajeList.add(new Mensajes(18,"Weno", "Ayer","Mary"));
        mensajeList.add(new Mensajes(19,"Ok, hasta luego", "Ayer","Usuario X"));
        mensajeList.add(new Mensajes(120,"Gracias por tu compra!", "Ayer","Usuario X"));
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