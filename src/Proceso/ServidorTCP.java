package Proceso;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {
    //Transmission Control Protocol
    public static void main(String[] args) throws IOException {
        //servidor:
        ServerSocket serverSocket = new ServerSocket(1234);
        //cliente
        Socket cliente = serverSocket.accept();
        System.out.println("Usuario conectado");
        //creo un canal de entrada con ese cliente
        BufferedReader buffer = new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        String mensaje = buffer.readLine();

        System.out.println("El cliente ha mandado " + mensaje);
        //cierro cliente SIEMPRE!!!!
        serverSocket.close();
    }
}
