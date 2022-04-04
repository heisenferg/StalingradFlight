package com.example.stalingradflight;

import androidx.appcompat.app.AppCompatActivity;

import android.animation.AnimatorSet;
import android.animation.ObjectAnimator;
import android.annotation.SuppressLint;
import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.media.MediaPlayer;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static int BANDO;
    ImageView naziButton, sovietButton, cab, textoNaxi, textoComunista;
    private MediaPlayer reproductor;

    @SuppressLint("ResourceType")
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        cab = findViewById(R.id.bando);
        naziButton = findViewById(R.id.naziButton);
        sovietButton = findViewById(R.id.sovietButton);
        textoComunista = findViewById(R.id.urss);
        textoNaxi = findViewById(R.id.alemania);
        textoNaxi.setVisibility(View.INVISIBLE);
        textoComunista.setVisibility(View.INVISIBLE);
        animarBandos();
        sonidos();
        naziButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BANDO = 1;
                mostarBando();
                balas();
                Intent i = new Intent(MainActivity.this, LanzadorJuego.class);
                startActivity(i);
            }
        });

        sovietButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BANDO = 2;
                mostarBando();
                balas();
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

    public void sonidos(){
            reproductor = MediaPlayer.create(this, R.raw.war);
            reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
            reproductor.start();
    }

    public void balas(){
        reproductor = MediaPlayer.create(this, R.raw.chargeandfire);
        reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.stop();
            }
        });
        reproductor.start();
    }

    public void mostarBando(){
        AnimatorSet animador = new AnimatorSet();
        if(BANDO==1){
            textoNaxi.setVisibility(View.VISIBLE);
           ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textoNaxi, "alpha",0f, 1f);
            fadeIn.setDuration(100);
            //Play y start animación
            animador.play(fadeIn);
        } else if (BANDO==2) {
            textoComunista.setVisibility(View.VISIBLE);
            ObjectAnimator fadeIn = ObjectAnimator.ofFloat(textoComunista, "alpha",0f, 1f);
            fadeIn.setDuration(100);
            //Play y start animación
            animador.play(fadeIn);
        }

        animador.start();
    }

}