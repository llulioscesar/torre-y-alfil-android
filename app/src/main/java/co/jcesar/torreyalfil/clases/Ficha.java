package co.jcesar.torreyalfil.clases;

public class Ficha {

    public enum Tipo{TORRE,ALFIL}
    public enum Banda{BLANCA, NEGRA}
    public  enum Color{NEGRO,BLANCO}

    private Banda banda;
    private Tipo tipo;
    private boolean maquina;
    private Color color;

    public Ficha(){
        this.maquina = false;
    }

    public Ficha(Banda banda, Tipo tipo, boolean maquina, Color color) {
        this.banda = banda;
        this.tipo = tipo;
        this.maquina = maquina;
        this.color = color;
    }

    public Banda getBanda() {
        return banda;
    }

    public void setBanda(Banda banda) {
        this.banda = banda;
    }

    public Tipo getTipo() {
        return tipo;
    }

    public void setTipo(Tipo tipo) {
        this.tipo = tipo;
    }

    public boolean isMaquina() {
        return maquina;
    }

    public void setMaquina(boolean maquina) {
        this.maquina = maquina;
    }

    public Color getColor() {
        return color;
    }

    public void setColor(Color color) {
        this.color = color;
    }
}
