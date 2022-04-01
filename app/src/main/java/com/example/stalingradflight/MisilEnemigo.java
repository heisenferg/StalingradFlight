package com.example.stalingradflight;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;

public class MisilEnemigo {

    private float coordenadaX;
    //Velocidad = velocidad*nivel;
    private float velocidad;
    private int nivel;
    private Juego j;
    public float puntero_misil;

    //Constructor
    public MisilEnemigo(Juego juego, int Nivel, float x){
        j = juego;
        nivel = Nivel;
        coordenadaX=x;
        velocidad = velocidad*nivel/10;
    }


    public void pintarMisilEnemigo(Canvas canvas, Paint paint){
        canvas.drawBitmap(j.misilEnemigo, coordenadaX, 0, paint);


    }
}
