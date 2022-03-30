package com.example.stalingradflight;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.content.pm.ActivityInfo;
import android.os.Bundle;
import android.view.View;
import android.widget.ImageButton;
import android.widget.ImageView;

public class MainActivity extends AppCompatActivity {

    public static int BANDO;
    ImageView naziButton, sovietButton;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        setRequestedOrientation(ActivityInfo.SCREEN_ORIENTATION_LANDSCAPE);

        naziButton = findViewById(R.id.naziButton);
        sovietButton = findViewById(R.id.sovietButton);

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
}