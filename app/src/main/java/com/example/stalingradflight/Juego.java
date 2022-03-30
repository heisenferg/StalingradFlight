package com.example.stalingradflight;

import android.app.Activity;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.Log;
import android.view.Display;
import android.view.MotionEvent;
import android.view.SurfaceHolder;
import android.view.SurfaceView;
import android.view.View;

import java.util.ArrayList;

public class Juego extends SurfaceView implements SurfaceHolder.Callback, View.OnTouchListener {
    private Bitmap bmpMapa;
    private Bitmap avion;
    private SurfaceHolder holder;
    private BucleJuego bucle;
    private int x=0,y=1; //Coordenadas x e y para desplazar
    private int maxX=0;
    private int maxY=0;
    private int contadorFrames=0;
    private boolean hacia_abajo=true;
    private static final String TAG = Juego.class.getSimpleName();
    // private int xMario=0, yMario=0;
    private int mapaH, mapaW;
    private int destMapaY;
    private int estadoAvion =1;
    private int puntero_Avion_sprite =0;
    private int avionW, avionH;
    private int contador_Frames = 0;
    private int yAvion;
    private int xAvion;
    private float posicionAvion[] = new float[2];
    private float velocidadAvion[] = new float[2];
    private float gravedad [] =new float[2];
    private float posicionInicialAvion[]= new float[2];
    private int tiempoCrucePantalla = 3;
    private float deltaT;
    private boolean salta = false;
    private boolean hayToque=false;
    private ArrayList<Toque> toques = new ArrayList<Toque>();
    private Control[] controles = new Control[3];
    private final int IZQUIERDA =0;
    private final int DERECHA =1;
    private final int DISPARO=2;
    private float velocidad;
    //Fondo
    private Bitmap fondos[] = new Bitmap[2];
    private int image_fondo[] = {R.drawable.fondonube, R.drawable.fondonube2};

    public Juego(Activity context) {
        super(context);
        holder = getHolder();
        holder.addCallback(this);
      //  cargarFondo();
        Display mdisp = context.getWindowManager().getDefaultDisplay();
        bmpMapa = BitmapFactory.decodeResource(getResources(), R.drawable.fondonube);

        //Cargamos mapa
       // cargarFondo();

        if (MainActivity.BANDO == 1){
            avion = BitmapFactory.decodeResource(getResources(), R.drawable.naziplane);
        } else {
            avion = BitmapFactory.decodeResource(getResources(), R.drawable.comunismplane);
        }
        Log.d("BANDO: " , " es " + MainActivity.BANDO);
        mapaH = bmpMapa.getHeight();
        mapaW = bmpMapa.getWidth();

        deltaT = 1f/BucleJuego.MAX_FPS;


        Point mdispSize = new Point();
        mdisp.getSize(mdispSize);

        //Velocidad:
        velocidad = mapaW/5/bucle.MAX_FPS;


        velocidadAvion[x] = maxX/tiempoCrucePantalla;
        velocidadAvion[y] = 0;



    }

/*
    public void cargarFondo(){
        for(int i=0;i<2;i++) {
            bmpMapa = BitmapFactory.decodeResource(getResources(), image_fondo[i]);
            if(fondos[i]==null)
                fondos[i] = bmpMapa.createScaledBitmap(bmpMapa, maxX, maxY, true);
            bmpMapa.recycle();
        }
    }


*/

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


         posicionInicialAvion[x] = maxY*1/5 ;


        destMapaY = (maxY-mapaH)/2;


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

        //Vector de velocidad
        // xMario = xMario+mapaW/(bucle.MAX_FPS*3);


        contadorFrames++;
        //Posición avion
        puntero_Avion_sprite = avionW/3 * estadoAvion;

        //Velocidad
        posicionAvion[x] = posicionAvion[x] + deltaT * velocidadAvion[x];
        posicionAvion[y] = posicionAvion[y] + deltaT * velocidadAvion[y];

//Gravedad
        velocidadAvion[x] = 0;
        velocidadAvion[y] = velocidadAvion[y] + deltaT;


        estadoAvion++;

        if (contadorFrames%3==0){

            if (estadoAvion >3){
                estadoAvion =1;
            }
        }

        if(salta){
            // bucle.ejecutandose=false;
            velocidadAvion[y]=-velocidadAvion[x]*2;
            gravedad[y]=-velocidadAvion[y]*2;
            salta = false;
        }

        for (int i=0; i<3; i++){
            if (controles[i].pulsado){
                Log.d("Control: ", "Se ha pulsado " + controles[i].nombre);
            }
        }

        if (controles[IZQUIERDA].pulsado){
            //Controlamos que no se salga por la izquierda.
            if (xAvion >=0)
                xAvion = (int) (xAvion - velocidad);
        }
        if (controles[DERECHA].pulsado){
            //Controlamos que no se salga por la derecha.
            if (xAvion <maxX-avion.getWidth())
                xAvion = (int) (xAvion + velocidad);
        }
        if (controles[DISPARO].pulsado){

        }


    }

    /**
     * Este método dibuja el siguiente paso de la animación correspondiente
     */

    public void renderizar(Canvas canvas) {
        if(canvas!=null) {

            Paint myPaint = new Paint();
            myPaint.setStyle(Paint.Style.STROKE);
            myPaint.setColor(Color.WHITE);

            //Toda el canvas en rojo
            canvas.drawColor(Color.BLACK);

            //Dibujar mapa
            canvas.drawBitmap(bmpMapa, 0, destMapaY, null);

            //Dibujar avión
            canvas.drawBitmap(avion, xAvion, yAvion, null);

            //Recortar muñeco
         /*   canvas.drawBitmap(avion, new Rect(puntero_Avion_sprite,0, puntero_Avion_sprite + (avionW /6), avionH *1/5),
                    new Rect( (int) posicionAvion[x], yAvion, (int) (avionW /6 ), destMapaY+mapaH*1/2), null);
*/
            //dibujar un texto
            myPaint.setStyle(Paint.Style.FILL);
            myPaint.setTextSize(40);
            canvas.drawText("Frames ejecutados:"+contadorFrames, 600, 1000, myPaint);


            //Dibujar controles
            myPaint.setAlpha(400);
            for (int i = 0; i<3; i++){
                controles[i].Dibujar(canvas, myPaint);
            }
        }
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
                for(int i=0;i<3;i++)
                    controles[i].comprueba_Pulsado(x,y);
                break;

            case MotionEvent.ACTION_POINTER_UP:
                synchronized(this) {
                    toques.remove(index);
                }

                //se comprueba si se ha soltado el botón
                for(int i=0;i<3;i++)
                    controles[i].compruebaSoltado(toques);
                break;

            case MotionEvent.ACTION_UP:
                synchronized(this) {
                    toques.clear();
                }
                hayToque=false;
                //se comprueba si se ha soltado el botón
                for(int i=0;i<3;i++)
                    controles[i].compruebaSoltado(toques);
                break;
        }

        return true;
    }
}
