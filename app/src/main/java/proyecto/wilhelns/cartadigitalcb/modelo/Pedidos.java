package proyecto.wilhelns.cartadigitalcb.modelo;

public class Pedidos {
    private String nombre;
    private int cantidad;
    private int sub_precio;
    private String fecha;
    private String mesa;
    private boolean confirm;

    public Pedidos(String nombre, int cantidad, int sub_precio, String fecha, String mesa,boolean confirm) {
        this.nombre = nombre;
        this.cantidad = cantidad;
        this.sub_precio = sub_precio;
        this.fecha = fecha;
        this.mesa = mesa;
        this.confirm = confirm;
    }

    public Pedidos() {}

    public boolean isConfirm() {
        return confirm;
    }

    public void setConfirm(boolean confirm) {
        this.confirm = confirm;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    public int getCantidad() {
        return cantidad;
    }

    public void setCantidad(int cantidad) {
        this.cantidad = cantidad;
    }

    public int getSub_precio() {
        return sub_precio;
    }

    public void setSub_precio(int sub_precio) {
        this.sub_precio = sub_precio;
    }

    public String getFecha() {
        return fecha;
    }

    public void setFecha(String fecha) {
        this.fecha = fecha;
    }

    public String getMesa() {
        return mesa;
    }

    public void setMesa(String mesa) {
        this.mesa = mesa;
    }


}
