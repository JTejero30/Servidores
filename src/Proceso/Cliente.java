package Proceso;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

public class Cliente extends Socket {
    public Cliente(String host, int port) throws IOException {
        super(host, port);
    }

    public static void main(String[] args) throws IOException {
//        Socket cliente = new Socket("localhost",1234);
//
//        //creo la salida
//        OutputStream out= cliente.getOutputStream();
//        //escribo en esa salida
//        out.write(("HolaMundo \n").getBytes());
//        out.flush();
//        cliente.close();
//
        Cliente cliente= new Cliente("localhost",1234);
    }
}
