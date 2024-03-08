package ProtocoloUDP;

import java.io.IOException;
import java.net.*;

public class Envia {
    public static void main(String[] args) throws IOException {
        //creo el servicio para enviar
        DatagramSocket servicio= new DatagramSocket();
        //cojo la direccion IP mediante Inetaddress
        InetAddress host = InetAddress.getByName("localhost");
        //creamos el mensaje
        byte[] mensaje= ("Hola").getBytes();

        DatagramPacket paquete= new DatagramPacket(mensaje,mensaje.length,host,12);
        servicio.send(paquete);
        servicio.close();
    }
}
