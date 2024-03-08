package ServerTCP;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {

    public static void main(String[] args) throws IOException {
        ServerSocket serrverSocket= new ServerSocket(1234);
        System.out.println("Esperando conexiones...");
        Socket cliente= serrverSocket.accept();
        System.out.println("Usuario conectado");
        BufferedReader bufferedReader= new BufferedReader(new InputStreamReader(cliente.getInputStream()));
        String cadenaCliente=  bufferedReader.readLine();
        System.out.println("Mensaje recibido "+cadenaCliente);
        cliente.close();
        serrverSocket.close();
    }
}
