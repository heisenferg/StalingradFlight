package com.example.stalingradflight;

import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Rect;
import android.media.MediaPlayer;
import android.util.Log;

public class MiMisil {
    private float velocidadMisil;
    private int nivel;
    private Juego j;
    public float puntero_misil=0;
    public int estadoMisil=0;
    public float coordenadaMisil;
    public float coordenadaX;
    public float coordenadaYMisil=0;
    public MediaPlayer sonidoDisparo;
    private BucleJuego bucleJuego;


    public float getCoordenadaMisil() {
        return coordenadaMisil;
    }

    public void setCoordenadaMisil(float coordenadaMisil) {
        this.coordenadaMisil = coordenadaMisil;
    }

    public float getCoordenadaYMisil() {
        return coordenadaYMisil;
    }

    public void setCoordenadaYMisil(int coordenadaYMisil) {
        this.coordenadaYMisil = coordenadaYMisil;
    }

    public MiMisil(Juego juego, float coordenadaX, float coordenadaY){
        j = juego;
        nivel = 1;
        // Coordenada X se pinta donde esté el avión más dimensión del avión/2 para disparar desde el centro
        coordenadaMisil = (int) (coordenadaX + j.avion.getWidth()/3);
        this.coordenadaX = coordenadaMisil;
        // Coordenada Y en posicion avión menos la altura del avión
      //  coordenadaYMisil = coordenadaY-j.avion.getHeight()/2;
        coordenadaYMisil = (int) (coordenadaY + j.avion.getHeight());
        //Sonido disparo
        sonidoDisparo = MediaPlayer.create(j.getContext(), R.raw.shoot);
        sonidoDisparo.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        sonidoDisparo.start();
    }

    public void pintarMiMisil(Canvas canvas, Paint paint){

        //En coordenadas le pongo entre 1.5 para adecuar
        canvas.drawBitmap(j.miMisil, new Rect((int) puntero_misil, 0, (int) (puntero_misil + j.misilEnemigo.getWidth()/9), j.misilEnemigo.getHeight()),
                new Rect((int)coordenadaMisil, (int) coordenadaYMisil-j.misilEnemigo.getHeight(), (int)coordenadaMisil+j.misilEnemigo.getWidth()/9, (int) (j.misilEnemigo.getHeight()/1.5+coordenadaYMisil)-j.misilEnemigo.getHeight()),
                null);
        Log.d("MISIL: ", " Y Mi Misil: " + coordenadaYMisil +
                " X mi  misil: " + coordenadaMisil + " velocidad mi misil: " + velocidadMisil);



    }


    public void posicionMiMisilY(){
        velocidadMisil = j.AltoPantalla/5/bucleJuego.MAX_FPS;
        velocidadMisil = velocidadMisil *nivel/5;
        //Vamos restando para que suba por la pantalla
        coordenadaYMisil = (int) (coordenadaYMisil-velocidadMisil);
    }


    public void actualizarMiMisilSprite(){
        if (j.contadorFrames%3==0) {
            puntero_misil = j.miMisil.getWidth() / 9 * estadoMisil;
            estadoMisil++;
            if (estadoMisil > 8) {
                estadoMisil = 0;
            }
        }
    }



}
