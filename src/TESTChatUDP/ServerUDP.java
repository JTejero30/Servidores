package TESTChatUDP;

import TESTChatUDP.Model.Cliente;

import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.util.ArrayList;
public class ServerUDP {
    ArrayList<Cliente> clientes= new ArrayList<>();
    public static void main(String[] args) throws IOException {
        DatagramSocket server = new DatagramSocket(1234);
        byte[] bytes= new byte[1024];
        while(true){

            DatagramPacket paquete= new DatagramPacket(bytes, bytes.length);

            server.receive(paquete);
            String paquetString= new String(paquete.getData(),0,paquete.getLength());
            System.out.println(paquetString);
            server.close();
        }
    }
    public ArrayList<Cliente> getClientes() {
        return clientes;
    }
    public void setClientes(ArrayList<Cliente> clientes) {
        this.clientes = clientes;
    }
}
