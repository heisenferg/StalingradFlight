package com.example.stalingradflight;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.media.MediaPlayer;
import android.os.Build;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.Random;

public class Juego extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private Bitmap bmpMapa;
    public Bitmap avion, avionOut;
    public Bitmap misilEnemigo;
    public Bitmap miMisil;
    public Bitmap explosion, explosionDerrota;
    public Bitmap banderaNazi, banderaComunista;
    public boolean avionRoto;


    private SurfaceHolder holder;
    private BucleJuego bucle;
    private Activity activity;
    private int mapaX=0,mapaY=1; //Coordenadas x e y para desplazar
    private int maxX=0;
    private int maxY=0;
    public int contadorFrames=0;
    private boolean hacia_abajo=true;
    private static final String TAG = Juego.class.getSimpleName();
    // private int xMario=0, yMario=0;
    private int mapaH, mapaW;
    private int destMapaY;
    private int estadoAvion =1;
    private int puntero_Avion_sprite =0;
    private int avionW, avionH;
    private int contador_Frames = 0;
    private float yAvion;
    private float xAvion;

    private int tiempoCrucePantalla = 3;
    private float deltaT;
    private boolean salta = false;
    private boolean hayToque=false;
    private ArrayList<Toque> toques = new ArrayList<Toque>();
    private Control[] controles = new Control[4];
    private final int IZQUIERDA =0;
    private final int DERECHA =1;
    private final int DISPARO=2;
    private final int MUSICA=3;
    public float velocidad;
    public int AnchoPantalla,AltoPantalla;
    private boolean derrota=false;
    private boolean victoria=false;
    private MediaPlayer reprductor;
    private MisilEnemigo nuevoMisil;
    private ArrayList<MisilEnemigo> misilesEnemigos = new ArrayList<MisilEnemigo>();
    private ArrayList<MiMisil> miMisilDisparado = new ArrayList<MiMisil>();
    private ArrayList<Choques> choqueDerrotaArray = new ArrayList<Choques>();
    private MediaPlayer musica;
    private int nivel;
    private int misilesDestruidos=0;
    private MiMisil misMisiles;
    public static Random coordenada =  new Random();
    private Musica musicaFondo;
    private Choques explosiones;
    private ArrayList<Choques> choquesArrayList = new ArrayList<Choques>();
    public Iterator<MisilEnemigo> iteradorMisilesMalos;
    public Iterator<MiMisil> iteradorMisilesBuenos;
    int tiempoUltimoDisparo = 0;


    public Juego(Activity context) {
        super(context);
        activity = context;
        holder = getHolder();
        holder.addCallback(this);
        dimesionesPantalla();
        nivel=1;


        pintarEnemigo();
        cargarMiMisil();
        cargarExplosiones();



        Display mdisp = context.getWindowManager().getDefaultDisplay();

        //Sonido
        sonidoAvion();

        bmpMapa = BitmapFactory.decodeResource(getResources(), R.drawable.fondo);
        mapaH = bmpMapa.getHeight();
        mapaW = bmpMapa.getWidth();

        //Mapa, ancho, alto, filtro
        bmpMapa.createScaledBitmap(bmpMapa, mapaW, mapaH, false);

        //Banderas para fin de juego:
        banderaNazi = BitmapFactory.decodeResource(getResources(), R.drawable.banderanazi);
        banderaComunista = BitmapFactory.decodeResource(getResources(), R.drawable.banderacomunista);

        if (MainActivity.BANDO == 1){
            avion = BitmapFactory.decodeResource(getResources(), R.drawable.naziplane2);
            avionOut = BitmapFactory.decodeResource(getResources(), R.drawable.naziplaneout);

        } else {
            avion = BitmapFactory.decodeResource(getResources(), R.drawable.comunismplane);
            avionOut = BitmapFactory.decodeResource(getResources(), R.drawable.comunismplaneout);

        }
        Log.d("BANDO: " , " es " + MainActivity.BANDO);


        deltaT = 1f/BucleJuego.MAX_FPS;


        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);

        //Velocidad:
        velocidad = AnchoPantalla/5/bucle.MAX_FPS;



        // PRUEBAS
        // nuevoMisil();


        //explosiones = new Choques(this,400,500);
        iteradorMisilesMalos = misilesEnemigos.iterator();
        iteradorMisilesBuenos = miMisilDisparado.iterator();


        explotarMisil(AnchoPantalla,AltoPantalla);


    }





    public void pintarEnemigo(){
        misilEnemigo = BitmapFactory.decodeResource(getResources(), R.drawable.misilenemigo);
        misilEnemigo.createScaledBitmap(misilEnemigo, 70, 110, true);
    }

    public void cargarExplosiones(){
        //Cargo la explosión de mi avión en caso de derrota
        explosionDerrota = BitmapFactory.decodeResource(getResources(), R.drawable.explosionmiavion);
        explosionDerrota.createScaledBitmap(explosionDerrota, 70, 110, false);

        if (MainActivity.BANDO==1){
            explosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosionnazi);
            explosion.createScaledBitmap(explosion, 70, 110, false);
        }
        if (MainActivity.BANDO==2) {
            explosion = BitmapFactory.decodeResource(getResources(), R.drawable.explosionsovietica);
            explosion.createScaledBitmap(explosion, 70, 110, false);
        }


    }


    public void dimesionesPantalla(){
        if(Build.VERSION.SDK_INT > 13) {
            Display display = activity.getWindowManager().getDefaultDisplay();
            Point size = new Point();
            display.getSize(size);
            AnchoPantalla = size.x;
            AltoPantalla = size.y;
        }
        else{
            Display display = activity.getWindowManager().getDefaultDisplay();
            AnchoPantalla = display.getWidth();  // deprecated
            AltoPantalla = display.getHeight();  // deprecated
        }
        Log.i(Juego.class.getSimpleName(), "alto:" + AltoPantalla + "," + "ancho:" + AnchoPantalla);
    }


    @Override
    public void surfaceChanged(SurfaceHolder holder, int format, int width, int height) {

    }

    @Override
    public void surfaceCreated(SurfaceHolder holder) {

        // Para interceptar los eventos de la SurfaceView
        getHolder().addCallback(this);
        Canvas c = holder.lockCanvas();
        maxX =c.getWidth();
        maxY = c.getHeight();
        holder.unlockCanvasAndPost(c);

        avionW = avion.getWidth();
        avionH = avion.getHeight();
        // creamos el game loop


        xAvion = maxX/2- avion.getWidth()/2;
        yAvion = (maxY / 5*4)-avion.getHeight()/2;




        destMapaY = (AnchoPantalla-AltoPantalla)/2;


        // se crea la superficie, creamos el game loop
        bucle = new BucleJuego(getHolder(), this);
        setOnTouchListener(this);

        // Hacer la Vista focusable para que pueda capturar eventos
        setFocusable(true);

        CargaControles();



        //comenzar el bucle
        bucle.start();

    }

    /**
     * Este método actualiza el estado del juego. Contiene la lógica del videojuego
     * generando los nuevos estados y dejando listo el sistema para un repintado.
     */
    public void actualizar() {

        // Si no hay derrota ni victoria
        if (derrota == false && victoria==false){
            contadorFrames++;
            estadoAvion++;

            //Posición avion
            puntero_Avion_sprite = avionW/3 * estadoAvion;

            if (contadorFrames%3==0){
                if (estadoAvion >3){
                    estadoAvion =1;
                }
            }


            for (int i=0; i<4; i++){
                if (controles[i].pulsado){
                    Log.d("Control: ", "Se ha pulsado " + controles[i].nombre);
                }
            }

            /**
             * CONTROLES
             */
            if (controles[IZQUIERDA].pulsado){
                //sonidos = new Virajes(this);
                //Controlamos que no se salga por la izquierda.
                if (xAvion >=0) {
                    xAvion = (int) (xAvion - velocidad);
                }

            }
            if (controles[DERECHA].pulsado){
                //Controlamos que no se salga por la derecha.
                if (xAvion <maxX-avion.getWidth())
                    xAvion = (int) (xAvion + velocidad);
            }
            if (controles[DISPARO].pulsado){
                // Controlamos que no podamos disparar más de un disparo cada 90 frames; aprox. 1,5 segundos
                if (contadorFrames-tiempoUltimoDisparo>=90){
                   /* for (MisilEnemigo e: misilesEnemigos){
                        Log.d("PRUEBA DISPARO: ", " xAvion: " + xAvion + " e.coordenadaMisil-misilEnemigo.getWidth(): " +e.coordenadaMisil +" ancho " + misilEnemigo.getWidth());

                        if ((int)xAvion+avion.getWidth()/2 > (int) e.coordenadaMisil && (int)xAvion+avion.getWidth()/2 < (int)e.coordenadaMisil+misilEnemigo.getWidth()){
                            Log.d("PRUEBA DISPARO: ", " xAvion: " + xAvion + " e.coordenadaMisil-misilEnemigo.getWidth(): " +e.coordenadaMisil +" ancho " + misilEnemigo.getWidth());
                            ponerMisilMioNuevo();
                        }
                    }*/
                    ponerMisilMioNuevo();

                    tiempoUltimoDisparo=contadorFrames;

                } else if (contadorFrames-tiempoUltimoDisparo<90) {
                    //Sonido sin munición
                    noMunicion();
                }

            }
            if (controles[MUSICA].pulsado){
               /* if (MainActivity.BANDO==1){
                    musica = MediaPlayer.create(activity, R.raw.marchlow);
                    musica.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mep) {
                            mep.start();
                        }
                    });
                    musica.start();
                }
                if (MainActivity.BANDO==2){
                    musica = MediaPlayer.create(activity, R.raw.katilow);
                    musica.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                        @Override
                        public void onCompletion(MediaPlayer mep) {
                            mep.start();
                        }
                    });
                    musica.start();
                }*/
                // No funciona correctamente, entra la música tarde siempre
                musica.stop();
                musicaFondo = new Musica(this);
            }


            /**
             * NIVELES CAMBIAR
             */
            if (misilesDestruidos>=3){
                victoria = true;

                nivel=2;
            }
            if (misilesDestruidos>=20){
                nivel=3;
            }
            if (misilesDestruidos>=30){
                nivel=4;
            }
            if (misilesDestruidos==40){
                victoria = true;
            }


            //Cada dos frames, muevo el mapa 1 px
            if (contadorFrames%2 == 0){
                mapaX = 0;
                mapaY = mapaY + 1;
            }



            int tiempoAleatorioMisilEnemigo = coordenada.nextInt(1500)+1;
            //Cada X frames aleatorios, pinto un misil
            if (contadorFrames%tiempoAleatorioMisilEnemigo==0){
                ponerEnemigoNuevo();
            }

            //Posición del misil por cada misil enemigo
            for(MisilEnemigo e: misilesEnemigos){
                e.posicionMisilY();
                //Actualización del misil
                e.actualizarMisilSprite();
            }


            //Posición de mi misil por cada misil
            for(MiMisil mi: miMisilDisparado){
                mi.posicionMiMisilY();
                //Actualización del misil
                mi.actualizarMiMisilSprite();


            }


            /*
             */


            //Explosiones
            for(Iterator<MisilEnemigo> it_enemigos= misilesEnemigos.iterator();it_enemigos.hasNext();) {
                MisilEnemigo e = it_enemigos.next();

                for(Iterator<MiMisil> it_disparos=miMisilDisparado.iterator();it_disparos.hasNext();) {
                    MiMisil d=it_disparos.next();

                    if (colision(e,d)) {

                       // explotarMisil(e.coordenadaMisil,e.coordenadaYMisil);

                        choquesArrayList.add(new Choques(this,e.coordenadaMisil-avion.getWidth()/2, d.coordenadaYMisil-misilEnemigo.getHeight()/2));
                        Log.d("Choques en array: ", "e.coordenadaMisil: " + e.coordenadaMisil +
                                " e.coordenadaYMisil: " + e.coordenadaYMisil + " d.coordenadaMisil: " +
                                d.coordenadaMisil + " d.coordenadaYMisil: " + d.coordenadaYMisil);
                        /* eliminamos de las listas tanto el disparo como el enemigo */
                        try {
                            it_enemigos.remove();
                            it_disparos.remove();
                        }
                        catch(Exception ex){}
                        misilesDestruidos++;
                        // explosiones.movimientoSpriteExplosion();


                    }

                }
            }

            explosiones.movimientoSpriteExplosion();


            // Actualizar explosiones
            for(Iterator<Choques> it_explosiones = choquesArrayList.iterator(); it_explosiones.hasNext();){
                Choques exp=it_explosiones.next();
                exp.estadoExplosion++;
                if(exp.estadoExplosion>=9) it_explosiones.remove();
            }

            // Mi explosion derrota
            for(MisilEnemigo misilEnemigo: misilesEnemigos){
                if(misilEnemigo.coordenadaYMisil>=AltoPantalla){

                    derrota=true;
                }
                if(misilEnemigo.coordenadaYMisil>=yAvion+avion.getHeight()/2 && misilEnemigo.coordenadaMisil>=xAvion && misilEnemigo.coordenadaMisil<xAvion+avion.getWidth()){
                    explosiones.movimientoSpriteExplosionMiAvion();
                    choquesArrayList.add(new Choques(this,xAvion-avion.getWidth()/2,yAvion+avion.getHeight()));
                   // if(contadorFrames%60==0){
                        derrota=true;
                    avionRoto=true;

                }
            }

           // comprobarDerrota();


        }

    }



    //Lo mantengo en dos métodos, para pruebas
    public void nuevoMisil(){
        nuevoMisil = new MisilEnemigo(this,nivel);
    }

    private void ponerEnemigoNuevo() {
        nuevoMisil();
        misilesEnemigos.add(nuevoMisil);
    }

    private void ponerMisilMioNuevo(){
        misMisiles = new MiMisil(this,xAvion,yAvion);
        miMisilDisparado.add(misMisiles);
    }

    private void explotarMisil(int coordenadaX, float coordenadaY){
        explosiones = new Choques(this, coordenadaX, coordenadaY);
        choquesArrayList.add(explosiones);
    }


    public boolean colision(MisilEnemigo e, MiMisil d){
        Bitmap enemigo=e.bitmap();
        Bitmap disparo=miMisil;
        Log.d("ColisionesA:", "e.coordenadasMisil: " + e.coordenadaMisil + ", e.coordenadaYMisil: " + e.coordenadaYMisil + "\n" +
                "xAvion: " + xAvion +" d.coordenadaYMisil:" + d.coordenadaYMisil);
        return Colision.hayColision(enemigo,(int) e.coordenadaMisil,(int) e.coordenadaYMisil-misilEnemigo.getHeight()/2,
                disparo,(int)d.coordenadaMisil,(int)d.coordenadaYMisil);
    }

    // Si choca misil enemigo contra mi avion
    public boolean colisionDerrota(MisilEnemigo e){
      Bitmap enemigo=e.bitmap();
        Log.d("Colisiones:", "e.coordenadasMisil: " + Colision.hayColision(enemigo,(int) e.coordenadaMisil,(int)e.coordenadaYMisil-misilEnemigo.getHeight()/2,
                avion,(int)xAvion,(int)yAvion));
        //EL siguiente yAvion hace explotar abajo del avión
        return Colision.hayColision(enemigo,(int) e.coordenadaMisil,(int)e.coordenadaYMisil-misilEnemigo.getHeight()/2,
              avion,(int)xAvion,AltoPantalla);

      /*  if (xAvion > e.coordenadaMisil && xAvion<e.coordenadaMisil+avion.getWidth() && yAvion < e.coordenadaYMisil &&
        yAvion > e.coordenadaYMisil-misilEnemigo.getHeight()){
            Log.d("True:", "entra al primer if");

                Log.d("True: ", "es true");

                return true;

        }

        Log.d("True: ", "es false");

        return false;*/

    }



        public void comprobarDerrota() {
            //Explosión derrota
            for (MisilEnemigo e : misilesEnemigos) {
                Log.d("Aquí:", " entra hasta el iterador");
                if(colisionDerrota(e)){
                        Log.d("Aquí:", " entra colisionderrota");
                    //explotarMisil(xAvion, yAvion);
                    choquesArrayList.add(new Choques(this, xAvion + avion.getWidth() / 2, yAvion + avion.getHeight() / 2));
                    derrota = true;
                }
            }
        }

        public void dibujarDerrota(Canvas canvas){
            canvas.drawBitmap(avionOut, xAvion,yAvion,null);
        }

    /**
     * Este método dibuja el siguiente paso de la animación correspondiente
     */

    public void renderizar(Canvas canvas) {
        if(canvas!=null) {

            Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setColor(Color.WHITE);

            //Toda el canvas en negro
            canvas.drawColor(Color.BLACK);



            //Dibujar mapa
            canvas.drawBitmap(bmpMapa, AnchoPantalla/2-mapaW/2, -mapaH+AltoPantalla+mapaY, null);
            Log.d("Altura y", " es de " + (-mapaH+mapaY));
            Log.d("Altura y", " mapaH " + mapaH);


            //Dibujar avión
            canvas.drawBitmap(avion, xAvion, yAvion, null);


            //Control de frames
            myPaint.setStyle(Paint.Style.FILL);
            myPaint.setTextSize(40);

            myPaint.setColor(Color.GREEN);
            canvas.drawText("Frames ejecutados:"+contadorFrames, 600, maxY/6*1, myPaint);
            canvas.drawText("Nivel:"+ nivel, controles[DERECHA].xCoordenada+controles[DERECHA].Ancho()+50,
                    controles[DERECHA].yCoordenada+controles[DERECHA].Alto()/2, myPaint);
            canvas.drawText("Misiles destruidos:"+ misilesDestruidos, controles[DERECHA].xCoordenada+controles[DERECHA].Ancho()+50,
                    controles[DERECHA].yCoordenada+controles[DERECHA].Alto()/2+50, myPaint);

            //Dibujar controles
            myPaint.setAlpha(400);
            for (int i = 0; i<4; i++){
                controles[i].Dibujar(canvas, myPaint);
            }



            // Dibujamos los enemigos
            for(MisilEnemigo e: misilesEnemigos){
                e.pintarMisilEnemigo(canvas,myPaint);
            }

            //Pintar mis misiles
            for (MiMisil mi : miMisilDisparado){
                mi.pintarMiMisil(canvas, myPaint);
            }


            // Dibujamos la explosión
            for (Choques miexplosion: choquesArrayList){
                miexplosion.dibujarExplosion(canvas,myPaint);
            }

            // Dibujamos la derrota
            for (Choques choqueDerrota: choqueDerrotaArray){

                choqueDerrota.dibujarExplosionDerrota(canvas,myPaint);
            }


            if (contadorFrames <= 500){
                myPaint.setColor(Color.RED);
                canvas.drawText("Acaba con todos los misiles antes de que alcancen a tus tropas", AnchoPantalla/4,AltoPantalla/2,myPaint);
            }


            // Condiciones de drrota y victoria
            if (derrota==true){
                //explosiones.dibujarExplosionDerrota(canvas, myPaint);
                derrotaFindeJuego(myPaint, canvas);
            }

            if (victoria==true){
               victoriaFindeJuego(myPaint, canvas);
            }

        }
    }


    public void victoriaFindeJuego(Paint myPaint, Canvas canvas){
        myPaint.setAlpha(0);

        if (MainActivity.BANDO==1){
            //Bandera Nazi Victoria
            canvas.drawBitmap(banderaNazi, AnchoPantalla/2-banderaNazi.getWidth()/2, AltoPantalla-banderaNazi.getHeight(), null);
            myPaint.setColor(Color.GREEN);
            myPaint.setTextSize(AnchoPantalla/10);
            canvas.drawText("¡Alemania ganó!", AnchoPantalla/4, AltoPantalla/2-100, myPaint);
            myPaint.setTextSize(AnchoPantalla/20);
            canvas.drawText("Cambiaste el curso de la historia", AnchoPantalla/4, AltoPantalla/2+100, myPaint);
            musica.stop();
            musicaFondo = new Musica(this);
        }
        if (MainActivity.BANDO==2){
            // Bandera Comunista Victoria
            canvas.drawBitmap(banderaComunista, AnchoPantalla/2-banderaComunista.getWidth()/2, AltoPantalla-banderaComunista.getHeight()*2, null);
            myPaint.setColor(Color.RED);
            myPaint.setTextSize(AnchoPantalla/10);
            canvas.drawText("Ganó la URSS!!", AnchoPantalla/4, AltoPantalla/2-100, myPaint);
            myPaint.setTextSize(AnchoPantalla/20);
            canvas.drawText("Acabaste con todos los misiles Nazis", AnchoPantalla/4, AltoPantalla/2+100, myPaint);
        }
    }

    public void derrotaFindeJuego(Paint myPaint, Canvas canvas){
        myPaint.setAlpha(0);
        myPaint.setColor(Color.RED);

        if (MainActivity.BANDO==1){
            //Bandera Nazi Victoria
            canvas.drawBitmap(banderaComunista, AnchoPantalla/2-banderaNazi.getWidth()/2, AltoPantalla-banderaNazi.getHeight(), null);

            myPaint.setTextSize(AnchoPantalla/10);
            canvas.drawText("¡Rusia te ganó!", 0, AltoPantalla/2-100, myPaint);
            myPaint.setTextSize(AnchoPantalla/20);
            canvas.drawText("No pudiste cambiar el curso de la historia", 0, AltoPantalla/2+100, myPaint);
            if(avionRoto) {
                dibujarDerrota(canvas);
            }
        }
        if (MainActivity.BANDO==2){
            // Bandera Comunista Victoria
            canvas.drawBitmap(banderaNazi, AnchoPantalla/2-banderaComunista.getWidth()/2, AltoPantalla-banderaComunista.getHeight()*2, null);

            myPaint.setColor(Color.RED);
            myPaint.setTextSize(AnchoPantalla/10);
            canvas.drawText("¡Alemania te ganó!", 0, AltoPantalla/2-100, myPaint);
            myPaint.setTextSize(AnchoPantalla/20);
            canvas.drawText("El curso de la historia tomará otros derroteros", 0, AltoPantalla/2+100, myPaint);
            if(avionRoto) {
                dibujarDerrota(canvas);
            }

        }
    }


    public void cargarMiMisil(){
        if (MainActivity.BANDO ==1){
            //Misil nazi cargardo
            miMisil = BitmapFactory.decodeResource(getResources(), R.drawable.mimisilnazi);

        }
        if (MainActivity.BANDO ==2){
            //Misil comunista cargado
            miMisil = BitmapFactory.decodeResource(getResources(), R.drawable.mimisilsovietico);
        }
        miMisil.createScaledBitmap(miMisil, 70, 110, true);
    }


    public void CargaControles(){
        float aux;

        //Izquierda
        controles[IZQUIERDA]=new Control(getContext(),0,maxY/5*4);
        controles[IZQUIERDA].Cargar(R.drawable.izquierda);
        controles[IZQUIERDA].nombre="Izquieda";

        //Derecha
        controles[DERECHA]=new Control(getContext(),
                controles[0].xCoordenada+controles[0].Ancho(), controles[0].yCoordenada);
        controles[DERECHA].Cargar(R.drawable.derecha);
        controles[DERECHA].nombre="Derecha";

        //disparo
        aux=6.0f/7.0f*maxX; //en los 6/7 del ancho
        controles[DISPARO]=new Control(getContext(),aux,maxY-controles[0].Alto());
        controles[DISPARO].Cargar(R.drawable.disparo);
        controles[DISPARO].nombre="Fuego!";

        //Poner música
        controles[MUSICA] = new Control(getContext(), aux, controles[0].yCoordenada/4*1);
        controles[MUSICA].Cargar(R.drawable.musica);
        controles[MUSICA].nombre="Music on";
    }


    @Override
    public void surfaceDestroyed(SurfaceHolder holder) {
        Log.d(TAG, "Juego destruido!");
        // cerrar el thread y esperar que acabe
        boolean retry = true;
        while (retry) {
            try {
                // bucle.fin();
                bucle.join();
                retry = false;
            } catch (InterruptedException e) {

            }
        }
    }


    @Override
    public boolean onTouch(View v, MotionEvent event) {
        int index;
        int x,y;

        // Obtener el pointer asociado con la acción
        index = event.getActionIndex();


        x = (int) event.getX(index);
        y = (int) event.getY(index);

        switch(event.getActionMasked()){
            case MotionEvent.ACTION_DOWN:
            case MotionEvent.ACTION_POINTER_DOWN:
                hayToque=true;

                synchronized(this) {
                    toques.add(index, new Toque(index, x, y));
                }

                //se comprueba si se ha pulsado
                for(int i=0;i<4;i++)
                    controles[i].comprueba_Pulsado(x,y);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                synchronized(this) {
                    toques.remove(index);
                }

                //se comprueba si se ha soltado el botón
                for(int i=0;i<4;i++)
                    controles[i].compruebaSoltado(toques);
                break;

            case MotionEvent.ACTION_UP:
                synchronized(this) {
                    toques.clear();

                }
                hayToque=false;
                //se comprueba si se ha soltado el botón
                for(int i=0;i<4;i++)
                    controles[i].compruebaSoltado(toques);
                break;
        }

        return true;
    }


    public void sonidoAvion(){
        if (MainActivity.BANDO==1){
            musica = MediaPlayer.create(activity, R.raw.aircraftengine);
            musica.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mep) {
                    mep.start();
                }
            });
            musica.start();
        } else if (MainActivity.BANDO==2) {
            musica = MediaPlayer.create(activity, R.raw.comunistengine);
            musica.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
                @Override
                public void onCompletion(MediaPlayer mep) {
                    mep.start();
                }
            });
            musica.start();
        }
    }


    /**
     * Ejemplo, hay que cambiar
     */
    public void noMunicion(){
        reprductor = MediaPlayer.create(activity, R.raw.noanmo);
        reprductor.setOnCompletionListener(new MediaPlayer.OnCompletionListener() {
            @Override
            public void onCompletion(MediaPlayer mp) {
                mp.release();
            }
        });
        reprductor.start();
    }




}