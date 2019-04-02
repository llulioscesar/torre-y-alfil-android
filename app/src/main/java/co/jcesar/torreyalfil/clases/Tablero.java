package co.jcesar.torreyalfil.clases;

import android.app.Activity;
import android.content.Context;
import android.graphics.drawable.ColorDrawable;
import android.graphics.drawable.Drawable;
import android.nfc.Tag;
import android.os.Handler;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.DragEvent;
import android.view.Gravity;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import androidx.constraintlayout.widget.ConstraintLayout;
import androidx.gridlayout.widget.GridLayout;
import co.jcesar.torreyalfil.R;

/**
 * Esta clase define el tablero de juego
 * @author Julio Cesar Caicedo Santos
 * @version 30/03/2019
 */
public class Tablero {

    private static final String TAG  = "TABLERO";
    private static final int BLANCO = -1;
    private static final int GRIS = -3355444;
    private static final int SELECT = -3559712;
    private static final int ANIMACION_SEG = 300;

    GridLayout tablero;
    Activity app;
    Context context;

    int filas, columnas;

    ImageView[][] cuadros;

    Casilla seleccion = null;

    Jugador jugador1, jugador2;

    ImageView alfilBlanco, alfilNegro, torreBlanca, torreNegra, actual;

    /**
     * Constructor para el tablero de ajedrez 8x8
     * @param context el parametro context define el contexto de la aplicacion donde se creara el tablero
     */
    public Tablero(Context context, int filas, int columnas){
        this.filas = filas + 1;
        this.columnas = columnas +1;
        this.cuadros = new ImageView[filas][columnas];
        this.app = (Activity) context;
        this.context = context;
        this.tablero = this.app.findViewById(R.id.tablero);
        this.crearTablero(filas, columnas);
        this.colocarFicha();
    }

    /**
     * Crea el tablero de juego
     * @param filas numero de filas que debe tener el tablero
     * @param columnas numero de columnas que debe tener el tablero
     */
    private void crearTablero(int filas, int columnas){
        boolean ok = false;

        int sum = 0;

        this.tablero.setColumnCount(this.columnas);
        this.tablero.setRowCount(this.filas);
        GridLayout.LayoutParams layoutParams;

        DisplayMetrics metrics = new DisplayMetrics();
        app.getWindowManager().getDefaultDisplay().getMetrics(metrics);
        int width = metrics.widthPixels;
        int size = width / this.columnas;

        for (int i = 0; i < this.filas; i++){
            for (int j = 0; j < this.columnas; j++){
                layoutParams = new GridLayout.LayoutParams();
                layoutParams.rowSpec = GridLayout.spec(i);
                layoutParams.columnSpec = GridLayout.spec(j);
                layoutParams.width = size;
                layoutParams.height = layoutParams.width;
                if(j>0&&i>0){
                    layoutParams.rightMargin = (int)(1 * metrics.density);
                    layoutParams.bottomMargin = layoutParams.rightMargin;
                    ImageView cuadro = new ImageView(app);
                    cuadro.setId(sum);
                    cuadro.setOnClickListener(onClickListener);
                    cuadro.setTag(new Casilla(ok ? Casilla.Color.NEGRO : Casilla.Color.BLANCO, i,j, null));
                    cuadro.setLayoutParams(layoutParams);
                    cuadro.setBackgroundColor(this.app.getResources().getColor(ok ? R.color.gris : R.color.blanco));
                    cuadro.setOnDragListener(onDragListener);
                    if (j-1 != columnas - 1){
                        ok = !ok;
                    }else {
                        layoutParams.rightMargin = 0;
                    }
                    cuadro.setLayoutParams(layoutParams);
                    this.tablero.addView(cuadro);
                    this.cuadros[i-1][j-1] = cuadro;
                    sum++;
                }else if(i == 0 && j > 0){
                    TextView textView = new TextView(context);
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(layoutParams);
                    textView.setTextSize(app.getResources().getDimension(R.dimen.numeros_tablero));
                    textView.setTextColor(app.getResources().getColor(R.color.negro));
                    textView.setText(j+"");
                    this.tablero.addView(textView);
                } else if (i> 0) {
                    TextView textView = new TextView(context);
                    textView.setGravity(Gravity.CENTER);
                    textView.setLayoutParams(layoutParams);
                    textView.setTextColor(app.getResources().getColor(R.color.negro));
                    textView.setTextSize(app.getResources().getDimension(R.dimen.numeros_tablero));
                    textView.setText(i+"");
                    this.tablero.addView(textView);
                }

            }
        }
        this.tablero.invalidate();
    }

    /**
     * Incializa las fichas para colocarlas en el tablero
     */
    void colocarFicha(){
        Casilla casilla = new Casilla();
        Ficha ficha = new Ficha(null, Ficha.Tipo.ALFIL, false, Ficha.Color.BLANCO);

        alfilBlanco = app.findViewById(R.id.alfil_blanco);
        alfilBlanco.setVisibility(View.VISIBLE);
        casilla.setFicha(ficha);
        alfilBlanco.setTag(casilla);
        alfilBlanco.setOnTouchListener(onTouchListener);

        casilla = new Casilla();
        alfilNegro = app.findViewById(R.id.alfil_negro);
        alfilNegro.setVisibility(View.VISIBLE);
        ficha = new Ficha(null, Ficha.Tipo.ALFIL, false, Ficha.Color.NEGRO);
        casilla.setFicha(ficha);
        alfilNegro.setTag(casilla);
        alfilNegro.setOnTouchListener(onTouchListener);

        casilla = new Casilla();
        torreBlanca = app.findViewById(R.id.torre_blanca);
        torreBlanca.setVisibility(View.VISIBLE);
        ficha = new Ficha(null, Ficha.Tipo.TORRE,false, Ficha.Color.BLANCO);
        casilla.setFicha(ficha);
        torreBlanca.setTag(casilla);
        torreBlanca.setOnTouchListener(onTouchListener);

        casilla = new Casilla();
        torreNegra = app.findViewById(R.id.torre_negra);
        torreNegra.setVisibility(View.VISIBLE);
        ficha = new Ficha(null, Ficha.Tipo.TORRE, false, Ficha.Color.NEGRO);
        casilla.setFicha(ficha);
        torreNegra.setTag(casilla);
        torreNegra.setOnTouchListener(onTouchListener);
    }

    /**
     * Este metodo coloca las fichas en el tablero en la posicion inicial de cada una
     */
    public void reiniciar(){
        this.tablero.removeAllViews();
        this.crearTablero(this.filas - 1, this.columnas - 1);
        jugador2 = null;
        jugador1 = null;
        colocarFicha();
    }

    /**
     * Muestra hacia donde puede mover la ficha
     */
    void indicarDiagonal(){
        int fila = seleccion.getFila();
        int columna = seleccion.getColumna();
        //parte izquierda abajo
        int valorFila = fila + 1;
        int valorColumna = columna - 1;
        while (valorFila < this.filas && valorColumna >= 1){
            cuadros[valorFila-1][valorColumna-1].setBackgroundColor(app.getResources().getColor(R.color.select));
            if(isFicha(cuadros[valorFila-1][valorColumna-1])) break;
            valorFila++;
            valorColumna--;
        }
        //parte derecha arriba
        valorFila = fila - 1;
        valorColumna = columna + 1;
        while (valorFila >= 1 && valorColumna < this.columnas) {
            cuadros[valorFila-1][valorColumna-1].setBackgroundColor(app.getResources().getColor(R.color.select));
            if(isFicha(cuadros[valorFila-1][valorColumna-1])) break;
            valorFila--;
            valorColumna++;
        }
        //parte izquierda arriba
        valorFila = fila - 1;
        valorColumna = columna - 1;
        while (valorFila >= 1 && valorColumna >= 1) {
            cuadros[valorFila-1][valorColumna-1].setBackgroundColor(app.getResources().getColor(R.color.select));
            if(isFicha(cuadros[valorFila-1][valorColumna-1])) break;
            valorFila--;
            valorColumna--;
        }
        //parte derecha abajo
        valorFila = fila + 1;
        valorColumna = columna + 1;
        while (valorFila < (this.filas) && valorColumna < (this.columnas)) {
            cuadros[valorFila-1][valorColumna-1].setBackgroundColor(app.getResources().getColor(R.color.select));
            if(isFicha(cuadros[valorFila-1][valorColumna-1])) break;
            valorFila++;
            valorColumna++;
        }
    }

    void indicarVerticalHorinzontal(){
        int fila = seleccion.getFila();
        int columna = seleccion.getColumna();
        int valorColumna = columna - 1;
        int valorFila = fila -1;
        // Parte izquierda de la casilla
        while (valorColumna > 0){
            cuadros[fila-1][valorColumna-1].setBackgroundColor(app.getResources().getColor(R.color.select));
            if(isFicha(cuadros[fila-1][valorColumna-1])) break;
            valorColumna--;
        }
        //Parte derecha de la casilla
        valorColumna = columna + 1;
        while (valorColumna < this.columnas){
            cuadros[fila-1][valorColumna-1].setBackgroundColor(app.getResources().getColor(R.color.select));
            if(isFicha(cuadros[fila-1][valorColumna-1])) break;
            valorColumna++;
        }
        // Parte arriba de la casilla
        while (valorFila > 0){
            cuadros[valorFila-1][columna-1].setBackgroundColor(app.getResources().getColor(R.color.select));
            if(isFicha(cuadros[valorFila-1][columna-1])) break;
            valorFila--;
        }
        //Parte abajo de la casilla
        valorFila = fila +1;
        while (valorFila < this.filas){
            cuadros[valorFila-1][columna-1].setBackgroundColor(app.getResources().getColor(R.color.select));
            if(isFicha(cuadros[valorFila-1][columna-1])) break;
            valorFila++;
        }

    }

    boolean isFicha(ImageView cuadro){
        Casilla casilla = (Casilla) cuadro.getTag();
        return casilla.getFicha() != null;
    }

    /**
     * Reestablece los colores del tablero de ajedrez despues de seleccionar una ficha
     */
    void reestablecerColor(){
        for (ImageView[] fila: cuadros) {
            for (ImageView cuadro: fila){
                Casilla casilla = (Casilla) cuadro.getTag();
                cuadro.setBackgroundColor(app.getResources().getColor(casilla.getColor() == Casilla.Color.NEGRO ? R.color.gris : R.color.blanco));
            }
        }
    }

    /**
     * ===================================================================================
     * EVENTOS DEL TABLERO
     * ===================================================================================
     */

    private OnChangeFicha mOnChangeFichaListener;
    private OnFinishGame mOnFinishGameListener;

    /**
     * Evento al soltar la ficha en el tablero
     */
    View.OnDragListener onDragListener = new View.OnDragListener() {
        @Override
        public boolean onDrag(View v, DragEvent event) {
            switch (event.getAction()){
                case DragEvent.ACTION_DROP:
                    ImageView view = (ImageView) event.getLocalState();
                    Casilla casilla = (Casilla) v.getTag();
                    Ficha ficha = ((Casilla)view.getTag()).getFicha();
                    if(ficha.getTipo() == Ficha.Tipo.ALFIL){
                        ficha.setBanda(casilla.getColor() == Casilla.Color.NEGRO ? Ficha.Banda.NEGRA:Ficha.Banda.BLANCA);
                        if (ficha.getColor() == Ficha.Color.BLANCO){
                            alfilNegro.setVisibility(View.GONE);
                            torreBlanca.setVisibility(View.GONE);
                        } else {
                            alfilBlanco.setVisibility(View.GONE);
                            torreNegra.setVisibility(View.GONE);
                        }
                    } else {
                        if (ficha.getColor() == Ficha.Color.BLANCO){
                            torreNegra.setVisibility(View.GONE);
                            alfilBlanco.setVisibility(View.GONE);
                        } else {
                            torreBlanca.setVisibility(View.GONE);
                            alfilNegro.setVisibility(View.GONE);
                        }
                    }
                    casilla.setFicha(ficha);
                    cuadros[casilla.getFila()-1][casilla.getColumna()-1].setImageDrawable(view.getDrawable());
                    seleccionar(ficha.getTipo().toString() + " => fila:" + casilla.getFila() + " columna: " + casilla.getColumna());
                    view.setVisibility(View.GONE);

                    if(jugador1 == null){
                        jugador1 = new Jugador(ficha);
                    }else {
                        jugador2 = new Jugador(ficha);
                    }

                    if(jugador1 != null && jugador2 != null){
                        if(jugador1.isBlanco()){
                            jugador1.setTurno(true);
                            Toast.makeText(context, "Inicia el jugador con " + jugador1.getFicha().getTipo().toString(), Toast.LENGTH_LONG).show();
                        } else {
                            jugador2.setTurno(true);
                            Toast.makeText(context, "Inicia el jugador con " + jugador2.getFicha().getTipo().toString(), Toast.LENGTH_LONG).show();
                        }

                    }

                    return true;
                case DragEvent.ACTION_DRAG_STARTED:
                    return true;
                case DragEvent.ACTION_DRAG_EXITED:
                    return false;
                case DragEvent.ACTION_DRAG_ENDED:
                    tablero.invalidate();
                    return true;
                default:
                    break;
            }
            return true;
        }
    };

    /**
     * Evento de arrastras las ficha al tablero
     */
    View.OnTouchListener onTouchListener = new View.OnTouchListener() {
        @Override
        public boolean onTouch(View v, MotionEvent event) {
            if(event.getAction() == MotionEvent.ACTION_DOWN){
                View.DragShadowBuilder shadowBuilder = new View.DragShadowBuilder(v);
                v.startDrag(null, shadowBuilder, v,0);
                return true;
            }
            return false;
        }
    };

    /**
     * Interfaz para el evento de la posicion de la ficha seleccionada
     */
    public interface OnChangeFicha{
        void getFichaSeleccionada(String ficha);
    }

    /**
     * Interfaz para el evento cuando el juego termina
     */
    public interface  OnFinishGame{
        void gameEnd();
    }

    /**
     * Evento para escuchar el cambio de posicion de la fichas del tablero
     * @param eventListener
     */
    public void OnChangeFichaListener(OnChangeFicha eventListener){
        mOnChangeFichaListener = eventListener;
    }

    /**
     * Evento para escuchar cuando el juego termina
     * @param eventListener
     */
    public void OnFinishGameListener(OnFinishGame eventListener){
        mOnFinishGameListener = eventListener;
    }

    /**
     * metodo para indicar que el juego se termino
     */
    void terminarJuego(){
        if(mOnFinishGameListener != null){
            mOnFinishGameListener.gameEnd();
        }
    }

    /**
     * Metodo para enviar la posicion de la ficha seleccionada
     */
    void seleccionar(String text){
        if (mOnChangeFichaListener != null){
            mOnChangeFichaListener.getFichaSeleccionada(text);
        }
    }

    /**
     * Evento al tocar una casilla del tablero
     */
    View.OnClickListener onClickListener = new View.OnClickListener() {
        @Override
        public void onClick(View v) {
            if(jugador1 == null || jugador2 == null){
                Toast.makeText(context, "Posicione la ficha", Toast.LENGTH_LONG).show();
                return;
            }

            ImageView cuadro = (ImageView) v;
            Casilla casilla = (Casilla) cuadro.getTag();

            ColorDrawable cuadroColor = (ColorDrawable) cuadro.getBackground();
            int colorId = cuadroColor.getColor();

            if (colorId == BLANCO || colorId == GRIS){
                reestablecerColor();
                actual = null;
                seleccion = null;
            }else if( colorId == SELECT && actual.getId() != cuadro.getId()){ // hacer el movimiento de la ficha
                boolean captura = false;
                seleccionar("Mover " + seleccion.getFicha().getTipo().toString() + " hacia la fila: " + casilla.getFila() + " columna: " + casilla.getColumna());
                jugador1.setTurno(!jugador1.isTurno());
                jugador2.setTurno(!jugador2.isTurno());
                if (casilla.getFicha() != null){ // ficha capturada
                    captura = true;
                    seleccionar(seleccion.getFicha().getTipo().toString() + " captura a " + casilla.getFicha().getTipo().toString());
                    jugador1 = null;
                    jugador2 = null;
                }
                if(seleccion.getFicha().getTipo() == Ficha.Tipo.ALFIL){
                    moverAlfil(casilla.getFila() -1, casilla.getColumna() -1, captura);
                } else {
                    moverTorre(casilla.getFila() - 1, casilla.getColumna() - 1, captura);
                }
                reestablecerColor();
                actual = null;
                seleccion = null;
            }

            if (casilla.getFicha() != null && colorId != SELECT){
                Ficha ficha = casilla.getFicha();

                Jugador jugador = validarTurno();
                if(jugador.getFicha().getTipo() != ficha.getTipo()){
                    Toast.makeText(context, "Es el turno de " + jugador.getFicha().getTipo().toString(), Toast.LENGTH_LONG).show();
                    return;
                }

                reestablecerColor();
                seleccion = casilla;
                cuadro.setBackgroundColor(app.getResources().getColor(R.color.select));
                if (ficha.getTipo() == Ficha.Tipo.ALFIL){
                    indicarDiagonal();
                } else {
                    indicarVerticalHorinzontal();
                }
                seleccionar(casilla.getFicha().getTipo().toString() + " => fila: " + casilla.getFila() + " columna: " + seleccion.getColumna());
                actual = cuadro;
            }
        }
    };

    /**
     * Devuelve el jugador con el turno actual
     * @return
     */
    Jugador validarTurno(){
        return jugador1.isTurno() ? jugador1 : jugador2;
    }

    /**
     * Animacion al mover la ficha torre
     * @param fila la nueva fila donde debe llegar
     * @param columna la nueva columna donde debe llegar
     */
    private void moverTorre(final int fila, final int columna, boolean captura) {
        final int sFila = seleccion.getFila() - 1;
        final int sColumna = seleccion.getColumna() - 1;
        final boolean isBlanca = (seleccion.getFicha().getColor() == Ficha.Color.BLANCO ? true : false);
        final Handler handler = new Handler();
        Runnable runnable;
        // mover hacia la izquierda
        if (fila == sFila && columna < sColumna){
            runnable = new Runnable(){
                @Override
                public void run() {
                    // i > columna porque i retrocede
                    for (int i = sColumna; i > columna; i--){
                        final ImageView old = cuadros[sFila][i];
                        final ImageView act = cuadros[sFila][i-1];
                        final int value = i;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (columna < value) {
                                    old.setImageResource(android.R.color.transparent);
                                }
                                act.setImageDrawable(app.getResources().getDrawable(isBlanca ? R.drawable.ic_torre_blanca : R.drawable.ic_torre_negra));
                            }
                        });
                        try {
                            Thread.sleep(ANIMACION_SEG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }else if (fila == sFila && columna > sColumna){ // Mover hacia la derecha
            runnable = new Runnable() {
                @Override
                public void run() {
                    for (int i = sColumna; i < columna; i++){
                        final ImageView old = cuadros[sFila][i];
                        final ImageView act = cuadros[sFila][i+1];
                        final int value = i;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (columna > value) {
                                    old.setImageResource(android.R.color.transparent);
                                }
                                act.setImageDrawable(app.getResources().getDrawable(isBlanca ? R.drawable.ic_torre_blanca : R.drawable.ic_torre_negra));
                            }
                        });
                        try {
                            Thread.sleep(ANIMACION_SEG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }else if (fila < sFila && columna == sColumna){//mover hacia arriba
            runnable = new Runnable() {
                @Override
                public void run() {
                    for (int i = sFila; i > fila; i--){
                        final ImageView old = cuadros[i][sColumna];
                        final ImageView act = cuadros[i-1][sColumna];
                        final int value = i;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (fila < value) {
                                    old.setImageResource(android.R.color.transparent);
                                    old.invalidate();
                                }
                                act.setImageDrawable(app.getResources().getDrawable(isBlanca ? R.drawable.ic_torre_blanca : R.drawable.ic_torre_negra));
                            }
                        });
                        try {
                            Thread.sleep(ANIMACION_SEG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        } else{
            runnable = new Runnable() {
                @Override
                public void run() {
                    for (int i = sFila; i < fila; i++){
                        final ImageView old = cuadros[i][sColumna];
                        final ImageView act = cuadros[i+1][sColumna];
                        final int value = i;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if (fila > value) {
                                    old.setImageResource(android.R.color.transparent);
                                }
                                act.setImageDrawable(app.getResources().getDrawable(isBlanca ? R.drawable.ic_torre_blanca : R.drawable.ic_torre_negra));
                            }
                        });
                        try {
                            Thread.sleep(ANIMACION_SEG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    }
                }
            };
        }
        new Thread(runnable).start();
        ImageView act = cuadros[fila][columna];
        Casilla casilla = (Casilla)act.getTag();
        casilla.setFicha(seleccion.getFicha());
        seleccion.setFicha(null);
        act.setTag(casilla);
        if (captura){
            terminarJuego();
        }
    }

    /**
     * Animacion de la ficha alfil
     * @param fila fila donde debe llegar
     * @param columna columna donde debe llegar
     */
    private void moverAlfil(final int fila, final int columna, boolean captura) {
        final int sFila = seleccion.getFila() - 1;
        final int sColumna = seleccion.getColumna() - 1;
        final int filas = this.filas - 1;
        final int columnas = this.columnas -1;
        final boolean isBlanca = (seleccion.getFicha().getColor() == Ficha.Color.BLANCO ? true : false);
        final Handler handler = new Handler();
        Thread thread;
        Runnable runnable = null;
        final Drawable drawable = app.getResources().getDrawable(isBlanca ? R.drawable.ic_alfil_blanco : R.drawable.ic_alfil_negro);
        // izquierda arriba
        if (columna < sColumna && fila < sFila){
            runnable = new Runnable() {
                @Override
                public void run(){
                    int valorFila = sFila - 1;
                    int valorColumna = sColumna - 1;
                    while (valorFila >= fila && valorColumna >= columna){
                        final int i = valorFila;
                        final int j = valorColumna;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                if(fila <= i && columna <= j){
                                    cuadros[i+1][j+1].setImageDrawable(null);
                                }
                                cuadros[i][j].setImageDrawable(drawable);
                            }
                        });
                        try {
                            Thread.sleep(ANIMACION_SEG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        valorFila--;
                        valorColumna--;
                    }
                }
            };
        } else if(columna > sColumna && fila < sFila){ // ariba derecha
            runnable = new Runnable() {
                @Override
                public void run() {
                    int valorFila = sFila;
                    int valorColumna = sColumna;
                    while (valorFila >= fila && valorColumna < columna) {
                        final int i = valorFila - 1;
                        final int j = valorColumna + 1;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                cuadros[i+1][j-1].setImageDrawable(null);
                                cuadros[i][j].setImageDrawable(drawable);
                            }
                        });
                        try {
                            Thread.sleep(ANIMACION_SEG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        valorFila--;
                        valorColumna++;
                    }
                }
            };

        } else if(columna < sColumna && fila > sFila) { //Izquierda abajo
            runnable = new Runnable() {
                @Override
                public void run() {
                    int valorFila = sFila + 1;
                    int valorColumna = sColumna - 1;
                    while (valorFila <= fila && valorColumna >= columna){
                        final int i = valorFila;
                        final int j = valorColumna;
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                cuadros[i][j].setImageDrawable(drawable);
                                cuadros[i-1][j+1].setImageResource(android.R.color.transparent);

                            }
                        });
                        try {
                            Thread.sleep(ANIMACION_SEG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        valorFila++;
                        valorColumna--;
                    }
                }
            };
        } else { // Abajo derecha
            runnable = new Runnable() {
                @Override
                public void run() {
                    int valorFila = sFila + 1;
                    int valorColumna = sColumna + 1;
                    while (valorFila <= fila && valorColumna <= columna) {
                        final int i = valorFila;
                        final int j = valorColumna;
                        try {
                            Thread.sleep(ANIMACION_SEG);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        handler.post(new Runnable() {
                            @Override
                            public void run() {
                                cuadros[i-1][j-1].setImageDrawable(null);
                                cuadros[i][j].setImageDrawable(drawable);
                            }
                        });
                        valorFila++;
                        valorColumna++;
                    }
                }
            };
        }
        thread = new Thread(runnable);
        thread.start();
        ImageView act = cuadros[fila][columna];
        Casilla casilla = (Casilla)act.getTag();
        casilla.setFicha(seleccion.getFicha());
        seleccion.setFicha(null);
        act.setTag(casilla);
        if (captura){
            terminarJuego();
        }
    }

}
