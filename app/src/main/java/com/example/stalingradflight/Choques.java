package com.example.stalingradflight;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;

public class Choques {

    private Juego j;
    public float puntero_explosion =0;
    public int coordenadaXExplosionl;
    public float coordenadaYExplosion;
    public MediaPlayer sonidoExplosion;
    private BucleJuego bucleJuego;
    public int estadoExplosion;
    private MisilEnemigo misilEnemigo;


    public Choques(Juego j, int coordenadaXExplosionl, float coordenadaYExplosion) {
        this.j = j;
        this.coordenadaXExplosionl = coordenadaXExplosionl;
        this.coordenadaYExplosion = coordenadaYExplosion;
        puntero_explosion=0;
        sonidoExplosion = MediaPlayer.create(j.getContext(), R.raw.explosion);
        sonidoExplosion.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        sonidoExplosion.start();
    }

    public void dibujarExplosion(Canvas canvas, Paint paint){

        //En coordenadas le pongo entre 1.5 para adecuar
        canvas.drawBitmap(j.explosion, new Rect((int) puntero_explosion, 0, (int) (puntero_explosion+j.explosion.getWidth()/9), j.explosion.getHeight()),
                new Rect(coordenadaXExplosionl, (int) coordenadaYExplosion, coordenadaXExplosionl+j.explosion.getWidth()/9, (int) coordenadaYExplosion+j.explosion.getHeight()),
                null);
        Log.d("EXPLOSIÓN: ", " Y Mi explosion: " + coordenadaYExplosion +
                " X mi  explosion: " + coordenadaXExplosionl);



    }

    public boolean hayChoque=false;

    public boolean hayChoque(Juego j, int coordenadaX, int coordenadaY){
        if(coordenadaX == misilEnemigo.posicionMisilX() && coordenadaY == misilEnemigo.getCoordenadaYMisil()){
            hayChoque=true;
        }
        return hayChoque;
    }



    public void movimientoSpriteExplosion(){
        if (j.contadorFrames%6==0) {
            puntero_explosion = j.explosion.getWidth() / 9 * estadoExplosion;
            estadoExplosion++;
           /* if (estadoExplosion > 8) {
                j.explosion.recycle();
            }*/
        }
    }
}
