package proyecto.wilhelns.cartadigitalcb.modelo;

public class Items {
    private String detalle;
    private String imagen;
    private String info;
    private String nombre;
    private int precio;
    private boolean disponible;

    public Items(String detalle, String imagen, String info, String nombre, int precio, boolean disponible) {
        this.detalle = detalle;
        this.imagen = imagen;
        this.info = info;
        this.nombre = nombre;
        this.precio = precio;
        this.disponible = disponible;
    }

    public Items() {}

    public boolean isDisponible() {
        return disponible;
    }

    public void setDisponible(boolean disponible) {
        this.disponible = disponible;
    }

    public String getDetalle() {
        return detalle;
    }

    public void setDetalle(String detalle) {
        this.detalle = detalle;
    }

    public String getImagen() {
        return imagen;
    }

    public void setImagen(String imagen) {
        this.imagen = imagen;
    }

    public String getInfo() {
        return info;
    }

    public void setInfo(String info) {
        this.info = info;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getPrecio() {
        return precio;
    }

    public void setPrecio(int precio) {
        this.precio = precio;
    }
}
