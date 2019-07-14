package proyecto.wilhelns.cartadigitalcb.modelo;

public class Fechas {
    int imagen;
    String fecha;
    float monto;

    public Fechas(int imagen, String fecha, float monto) {
        this.imagen = imagen;
        this.fecha = fecha;
        this.monto = monto;
    }

    public int getImagen() {
        return imagen;
    }

    public void setImagen(int imagen) {
        this.imagen = imagen;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public void setMonto(float monto) {
        this.monto = monto;
    }

    public String getFecha() {
        return fecha;
    }

    public float getMonto() {
        return monto;
    }
}
