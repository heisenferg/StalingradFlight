package com.example.stalingradflight;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static int BANDO;
    ImageView naziButton, sovietButton, cab;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        cab = findViewById(R.id.bando);
        naziButton = findViewById(R.id.naziButton);
        sovietButton = findViewById(R.id.sovietButton);
animarBandos();
        naziButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BANDO = 1;
                Intent i = new Intent(MainActivity.this, LanzadorJuego.class);
                startActivity(i);
            }
        });

        sovietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BANDO = 2;
                Intent i = new Intent(MainActivity.this, LanzadorJuego.class);
                startActivity(i);
                  }
                }
        );



    }

    public void animarBandos(){
        AnimatorSet animador = new AnimatorSet();
        //Botón nazi
        ObjectAnimator rotacion = ObjectAnimator.ofFloat(naziButton, "rotation",0f, 360f);
        rotacion.setDuration(2000);
        //Botón comunista
        ObjectAnimator rotacion2 = ObjectAnimator.ofFloat(sovietButton, "rotation",360f, 0f);
        rotacion2.setDuration(2000);
        //Cabecera
        ObjectAnimator fadeIn = ObjectAnimator.ofFloat(cab, "alpha",0f, 1f);
        fadeIn.setDuration(3000);
        //Play y start animación
        animador.play(rotacion).with(rotacion2).with(fadeIn);
        animador.start();
    }
}