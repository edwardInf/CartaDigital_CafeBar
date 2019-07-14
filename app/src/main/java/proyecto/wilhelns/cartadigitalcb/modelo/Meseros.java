package proyecto.wilhelns.cartadigitalcb.modelo;

public class Meseros {
    private String nombre;
    private String clave;

    public Meseros() {}

    public Meseros(String nombre, String clave) {
        this.nombre = nombre;
        this.clave = clave;
    }

    public String getNombre() {
        return nombre;
    }

    public String getClave() {
        return clave;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public void setClave(String clave) {
        this.clave = clave;
    }
}
