package co.jcesar.torreyalfil.clases;

public class Casilla {

    public enum Color {BLANCO, NEGRO};

    private Color color;
    private int fila;
    private int columna;
    private Ficha ficha;

    public Casilla(Color color, int fila, int columna, Ficha ficha) {
        this.color = color;
        this.fila = fila;
        this.columna = columna;
        this.ficha = ficha;
    }

    public Casilla() {
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }

    public int getFila() {
        return fila;
    }

    public void setFila(int fila) {
        this.fila = fila;
    }

    public int getColumna() {
        return columna;
    }

    public void setColumna(int columna) {
        this.columna = columna;
    }
}
