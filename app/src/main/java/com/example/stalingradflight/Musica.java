package com.example.stalingradflight;

import android.media.MediaPlayer;

public class Musica {

    public MediaPlayer reproductor;
    public Juego juego;

    public Musica(Juego j){
        juego =j;

        if (MainActivity.BANDO==1){
            //Sonido disparo
            reproductor = MediaPlayer.create(j.getContext(), R.raw.marchlow);
            reproductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mp) {
                    mp.start();
                }
            });
            reproductor.start();
        } else if (MainActivity.BANDO==2) {
            reproductor = MediaPlayer.create(j.getContext(), R.raw.katilow);
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
