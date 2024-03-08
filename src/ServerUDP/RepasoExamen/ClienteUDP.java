package ServerUDP.RepasoExamen;

import java.io.IOException;
import java.net.*;

public class ClienteUDP {
    private static DatagramSocket socket;
    public static void main(String[] args) throws IOException {
        socket= new DatagramSocket();

        String mensaje= "Hola Mundo";
        byte[] bytes= mensaje.getBytes();

        DatagramPacket paquet= new DatagramPacket(bytes, bytes.length, InetAddress.getByName("localhost"),1234);
        socket.send(paquet);
    }
}
