package proyecto.wilhelns.cartadigitalcb.modelo;

public class User {

    String tipo, email;

    public User(String tipo, String emailAddress) {
        this.tipo = tipo;
        this.email = emailAddress;
    }
    public User() {}


    public String getTipo() {
        return tipo;
    }

    public String getEmail() {
        return email;
    }
}
