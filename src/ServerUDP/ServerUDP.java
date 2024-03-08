package ServerUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class ServerUDP {
    //user DATAGRAM PROTOCOL
    public static void main(String[] args) throws IOException {
        DatagramSocket server = new DatagramSocket(1235);
        //creamos un paquete para enviar
        byte[] bytes= new byte[1024];
        //construimos un paquete datagram con esos bytes
        DatagramPacket paquete = new DatagramPacket(bytes,bytes.length);

        server.receive(paquete);
        String paqueteRecibido= new String(paquete.getData(),0,paquete.getLength());
        System.out.println("Mensaje recibido "+paqueteRecibido);
        server.close();
    }
}
