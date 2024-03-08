package IVA_TCP;

import java.io.Serial;
import java.io.Serializable;
import java.net.Socket;

public class Producto implements Serializable{
    //implementa el serialize para poder pasarlo por el socket
    private Double precio;
    private String nombre;

    public Producto(Double precio, String nombre) {
        this.precio = precio;
        this.nombre = nombre;
    }

    public Double getPrecio() {
        return precio;
    }

    public void setPrecio(Double precio) {
        this.precio = precio;
    }

    public String getNombre() {
        return nombre;
    }

    public void setNombre(String nombre) {
        this.nombre = nombre;
    }

    @Override
    public String toString() {
        return "Producto{" +
                "precio=" + precio +
                ", nombre='" + nombre + '\'' +
                '}';
    }
}
