package ServerUDP;

import java.io.IOException;
import java.net.*;

public class ClienteUDP {
    public static void main(String[] args) throws IOException {
        DatagramSocket socket= new DatagramSocket();
        String mensaje= "Hola mundo";
        byte[] mensajeEnviar= mensaje.getBytes();

        DatagramPacket paquete= new DatagramPacket(mensajeEnviar,mensajeEnviar.length,InetAddress.getByName("localhost"),1235);
        socket.send(paquete);

    }
}
