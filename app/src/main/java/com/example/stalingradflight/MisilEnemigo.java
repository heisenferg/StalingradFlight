package com.example.stalingradflight;

import android.graphics.Canvas;
import android.graphics.Paint;

public class MisilEnemigo {

    private float coordenadaX;
    //Velocidad = velocidad*nivel;
    private float velocidad;
    private int nivel;
    private Juego j;

    //Constructor
    public MisilEnemigo(Juego juego, int Nivel, float x){
        j = juego;
        nivel = Nivel;
        coordenadaX=x;
        velocidad = velocidad*nivel/10;
    }


    public void sacarMisilEnemigo(Canvas canvas, Paint paint){
       // canvas.drawBitmap(j.m);
    }
}
