package ServerUDP.RepasoExamen;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.SocketException;

public class ServerUDP {
    public static void main(String[] args) throws IOException {
        DatagramSocket server = new DatagramSocket(1234);

        byte[] bytes= new byte[1024];

        DatagramPacket paquete= new DatagramPacket(bytes,bytes.length);

        server.receive(paquete);
        String paqueteRecibido= new String(paquete.getData(),0,paquete.getLength());
        System.out.println(paqueteRecibido);
        server.close();
    }
}
