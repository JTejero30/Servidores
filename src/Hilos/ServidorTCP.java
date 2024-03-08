package Hilos;

import IVA_TCP.Cliente;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class ServidorTCP {
    public static void main(String[] args) throws IOException {
        ServerSocket server = new ServerSocket(123);

        for (int i = 0; i < 5; i++) {
            Socket socket = new Socket("localhost", 123);
            cliente cliente = new cliente(socket);
            cliente.start();
            System.out.println("Cliente conectado");
        }
    }

    static class cliente extends Thread {
        private Socket socket;

        public cliente(Socket socket) {
            this.socket = socket;
        }
    }
}
