package com.example.agroplazaappmovil;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;

import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    @Override
    protected void onCreate (Bundle savedInstanceState) {
        super.onCreate (savedInstanceState);
        setContentView (R.layout.activity_main);

        TimerTask tarea = new TimerTask() {
            @Override
            public void run() {

                Intent intent = new Intent(MainActivity.this, Login.class);

                startActivity(intent);

                finish();
            }
        };

        Timer tiempo = new Timer();
        tiempo.schedule(tarea, 5000);
    }
}