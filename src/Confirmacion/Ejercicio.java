package Confirmacion;

import java.awt.image.BufferStrategy;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;

public class Ejercicio {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(123);
        Socket cliente = server.accept();

        BufferedReader buf= new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        System.out.println(buf.readLine());

        OutputStream confirmacion= cliente.getOutputStream();
        confirmacion.write(("Recibido Micke \n").getBytes());
        confirmacion.flush();
    }
}
