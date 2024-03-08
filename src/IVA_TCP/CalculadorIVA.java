package IVA_TCP;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.ArrayList;

public class CalculadorIVA {
    public static void main(String[] args) throws IOException, ClassNotFoundException {

        //servidor:
        ServerSocket serverSocket= new ServerSocket(1234);
        while(true){
            //cliente
            Socket cliente= serverSocket.accept();
            System.out.println("Cliente conectado");
            //creamos la entrada para el cliente (especial para objetos)
            InputStream input= cliente.getInputStream();
            ObjectInputStream objectInput= new ObjectInputStream(input);
            ArrayList<Producto> lista= (ArrayList<Producto>) objectInput.readObject();
            //creamos la salida para mandarselo al cliente
            OutputStream resultado= cliente.getOutputStream();
            String resultadoString= calcularIVA(lista).toString();
            resultado.write(("El total del iva es "+resultadoString+"\n").getBytes());

        }
    }

    private static Double calcularIVA(ArrayList<Producto> lista) {
        Double total=0.0;
        for (Producto producto:lista) {
            String nombre= producto.getNombre();
            Double precio= producto.getPrecio();
            if (!nombre.startsWith("A")){
                precio=precio*1.10;
            }else{
                precio= precio*1.21;
            }
            total= total+precio;
        }
        return total;
    }
}
