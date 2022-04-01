package com.example.stalingradflight;

import static com.example.stalingradflight.Juego.coordenada;

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
    public int coordenadaMisil;

    //Constructor
    public MisilEnemigo(Juego juego, int Nivel){
        j = juego;
        nivel = Nivel;
        velocidad = velocidad*nivel/10;
        posicionMisil();
    }


    public void pintarMisilEnemigo(Canvas canvas, Paint paint){
        canvas.drawBitmap(j.misilEnemigo, coordenadaMisil, 0, paint);
    }

    public int posicionMisil(){
        coordenadaMisil = coordenada.nextInt(j.AnchoPantalla);
        return coordenadaMisil;
    }
}
