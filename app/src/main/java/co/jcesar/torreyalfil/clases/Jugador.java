package co.jcesar.torreyalfil.clases;

public class Jugador {
    Ficha ficha;
    boolean blanco;
    boolean turno;

    public Jugador() {
    }

    public Jugador(Ficha ficha) {
        this.ficha = ficha;
        this.turno = false;
        this.blanco = (ficha.getColor() == Ficha.Color.BLANCO ? true : false);
    }

    public Ficha getFicha() {
        return ficha;
    }

    public void setFicha(Ficha ficha) {
        this.ficha = ficha;
    }

    public boolean isTurno() {
        return turno;
    }

    public void setTurno(boolean turno) {
        this.turno = turno;
    }

    public boolean isBlanco() {
        return blanco;
    }
}
