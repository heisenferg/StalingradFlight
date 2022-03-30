package com.example.stalingradflight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.pm.ActivityInfo;
import android.os.Build;
import android.os.Bundle;
import android.view.View;
import android.widget.TextView;

public class LanzadorJuego extends AppCompatActivity {

    private Juego juego;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        juego = new Juego(this);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);
        hideSystemUI();

        setContentView(juego);

    }

    private void hideSystemUI() {
        if (Build.VERSION.SDK_INT > Build.VERSION_CODES.HONEYCOMB) {
            //A partir de kitkat
            juego.setSystemUiVisibility(
                    View.SYSTEM_UI_FLAG_LAYOUT_STABLE
                            | View.SYSTEM_UI_FLAG_LAYOUT_HIDE_NAVIGATION
                            | View.SYSTEM_UI_FLAG_LAYOUT_FULLSCREEN
                            | View.SYSTEM_UI_FLAG_HIDE_NAVIGATION // hide nav bar
                            | View.SYSTEM_UI_FLAG_FULLSCREEN // hide status bar
                            | View.SYSTEM_UI_FLAG_IMMERSIVE_STICKY);
            //cuando se presiona volumen, por ej, se cambia la visibilidad, hay que volver
            //a ocultar
            juego.setOnSystemUiVisibilityChangeListener(new
                                                            View.OnSystemUiVisibilityChangeListener() {
                                                                @Override
                                                                public void onSystemUiVisibilityChange(int visibility) {
                                                                    hideSystemUI();
                                                                }
                                                            });
        }
    }

}