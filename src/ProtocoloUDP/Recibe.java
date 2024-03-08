package ProtocoloUDP;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;

public class Recibe{
    public static void main(String[] args) throws IOException {
        //creamos service
        DatagramSocket service= new DatagramSocket(12);
        //creamos el paquete con el almacenamiento maximo por paquete
        byte[] serverMax = new byte[1024];
        //creamos el paquete
        DatagramPacket paquete= new DatagramPacket(serverMax, serverMax.length);
        //recibimos el paquete
        for (int i = 0; i <2 ; i++) {
            service.receive(paquete);
            //Cogemos el paquete desde el byte 0 al ultimo
            String paqueteString= new String(paquete.getData(),0,paquete.getLength());
            System.out.println(paqueteString);
            //cerramos
        }

        service.close();
    }
}
