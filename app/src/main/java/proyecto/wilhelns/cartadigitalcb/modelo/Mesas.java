package proyecto.wilhelns.cartadigitalcb.modelo;

public class Mesas {
    public String mesa;
    public boolean estado;

    public Mesas(String mesa, boolean estado) {
        this.mesa = mesa;
        this.estado = estado;
    }
    public Mesas() {}


    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }

    public boolean isEstado() {
        return estado;
    }

    public void setEstado(boolean estado) {
        this.estado = estado;
    }
}
