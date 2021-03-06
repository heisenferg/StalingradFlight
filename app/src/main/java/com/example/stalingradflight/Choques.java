package com.example.stalingradflight;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;

public class Choques {

    private Juego j;
    public float puntero_explosion =0;
    public float coordenadaXExplosionl;
    public float coordenadaYExplosion;
    public MediaPlayer sonidoExplosion;
    private BucleJuego bucleJuego;
    public int estadoExplosion;
    private MisilEnemigo misilEnemigo;


    public Choques(Juego j, float coordenadaXExplosionl, float coordenadaYExplosion) {
        this.j = j;
        this.coordenadaXExplosionl = coordenadaXExplosionl;
        this.coordenadaYExplosion = coordenadaYExplosion - j.misilEnemigo.getHeight();
        puntero_explosion=0;
        estadoExplosion=0;
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
        canvas.drawBitmap(j.explosion, new Rect((int) puntero_explosion, 0, (int) (puntero_explosion+j.explosion.getWidth()/8), j.explosion.getHeight()),
                new Rect((int)coordenadaXExplosionl, (int) coordenadaYExplosion, (int)coordenadaXExplosionl+j.explosion.getWidth()/8, (int) coordenadaYExplosion+j.explosion.getHeight()),
                null);
        Log.d("EXPLOSIÓN: ", " Y Mi explosion: " + coordenadaYExplosion +
                " X mi  explosion: " + coordenadaXExplosionl);



    }



    public boolean hayChoque=false;

    public boolean hayChoque(Juego j, MiMisil miMisil, MisilEnemigo misilEnemigo){
        if(miMisil.coordenadaMisil == misilEnemigo.coordenadaMisil && miMisil.coordenadaYMisil == misilEnemigo.coordenadaYMisil){
            hayChoque=true;
        }
        return hayChoque;
    }




    public boolean finalizado(){
        return estadoExplosion>=9;
    }
    public void movimientoSpriteExplosion(){
        if (j.contadorFrames%6==0) {
            puntero_explosion = j.explosion.getWidth() / 8 * estadoExplosion;
            estadoExplosion++;
        }

    }


    /**
     *
     * @param canvas
     * @param paint
     * Para explosión derrota
     */

    // Dibujar la explosión en caso de derrota
    public void dibujarExplosionDerrota(Canvas canvas, Paint paint){

        //En coordenadas le pongo entre 1.5 para adecuar
        canvas.drawBitmap(j.explosionDerrota, new Rect((int) puntero_explosion, 0, (int) (puntero_explosion+j.explosionDerrota.getWidth()/15), j.explosionDerrota.getHeight()),
                new Rect((int)coordenadaXExplosionl, (int) coordenadaYExplosion, (int)coordenadaXExplosionl+j.explosion.getWidth()/15, (int) coordenadaYExplosion+j.explosionDerrota.getHeight()),
                null);
        Log.d("EXPLOSIÓN Derrota: ", " Y Mi explosion: " + coordenadaYExplosion +
                " X mi  explosion: " + coordenadaXExplosionl);

    }

    public void movimientoSpriteExplosionMiAvion() {
        if (j.contadorFrames % 12 == 0) {
            puntero_explosion = j.explosionDerrota.getWidth() / 15 * estadoExplosion;
            estadoExplosion++;
        }
    }


}
