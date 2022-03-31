package com.example.stalingradflight;

import android.media.MediaPlayer;

public class Virajes {

    public MediaPlayer reproductor;
    public Juego juego;

    public Virajes(Juego j){
        juego = j;

    }


    public void musicaFondo(MediaPlayer reproductor){
        if (MainActivity.BANDO==1){
            reproductor = MediaPlayer.create(juego.getContext(), R.raw.katilow);
            reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
            reproductor.start();
        } else if (MainActivity.BANDO==2) {
            reproductor = MediaPlayer.create(juego.getContext(), R.raw.katilow);
            reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
            reproductor.start();
        }
    }

}
