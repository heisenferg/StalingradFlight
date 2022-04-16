package com.example.stalingradflight;

import static com.example.stalingradflight.Juego.coordenada;

import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.util.Log;

public class MisilEnemigo {

    //Velocidad = velocidad*nivel;
    private float velocidadMisil;
    private int nivel=1;
    private Juego j;
    public float puntero_misil=0;
    public int estadoMisil=0;
    public float coordenadaMisil;
    public float coordenadaYMisil;
    private BucleJuego bucleJuego;

    public float getCoordenadaMisil() {
        return coordenadaMisil;
    }

    public float getCoordenadaYMisil() {
        return coordenadaYMisil;
    }

    //Constructor
    public MisilEnemigo(Juego juego, int Nivel){
        j = juego;
        nivel = Nivel;
        velocidadMisil = velocidadMisil * Nivel;
        coordenadaYMisil=0;
        coordenadaMisil = posicionMisilX();
        Log.d("Posición misil X", " X:" + coordenadaMisil);
    }


    public void pintarMisilEnemigo(Canvas canvas, Paint paint){
       // canvas.drawBitmap(j.misilEnemigo, coordenadaMisil, 0, paint);

        //Recortar misil
        //En coordenadas le pongo entre 1.5 para adecuar
        canvas.drawBitmap(j.misilEnemigo, new Rect((int) puntero_misil, 0, (int) (puntero_misil + j.misilEnemigo.getWidth()/9), j.misilEnemigo.getHeight()),
                    new Rect((int)coordenadaMisil, (int) coordenadaYMisil-j.misilEnemigo.getHeight(), (int)coordenadaMisil+j.misilEnemigo.getWidth()/9, (int) (j.misilEnemigo.getHeight()/1.5+coordenadaYMisil)-j.misilEnemigo.getHeight()),
                null);
        Log.d("MISIL: ", " Y Misil: " + coordenadaYMisil +
                " X misil: " + coordenadaMisil + " velocidad: " + velocidadMisil);
        // Destruyo misil pintado
        if(coordenadaMisil>j.AltoPantalla){
          //  j.misilEnemigo.recycle();
        }


    }

    public float posicionMisilX(){
        coordenadaMisil = coordenada.nextInt(j.AnchoPantalla);
        return coordenadaMisil;
    }

    public void posicionMisilY(){
        velocidadMisil = j.AltoPantalla/5/bucleJuego.MAX_FPS;
        velocidadMisil = velocidadMisil *nivel/5;
        coordenadaYMisil = coordenadaYMisil+velocidadMisil;
    }

    public void actualizarMisilSprite(){
        if (j.contadorFrames%3==0) {
            puntero_misil = j.misilEnemigo.getWidth() / 9 * estadoMisil;
            estadoMisil++;
            if (estadoMisil > 8) {
                estadoMisil = 0;
            }
        }
    }

    public Bitmap bitmap(){
        return j.misilEnemigo;
    }

}
