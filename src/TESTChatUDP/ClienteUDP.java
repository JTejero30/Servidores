package TESTChatUDP;

import TESTChatUDP.Model.Cliente;

import java.util.ArrayList;
import java.util.Scanner;

public class ClienteUDP {
    public static void main(String[] args) {
        Scanner lector= new Scanner(System.in);
        ServerUDP server= new ServerUDP();

        Thread cliente= new Thread(()->{
            String  user;
            int puerto;
            Cliente myClient= new Cliente();

            System.out.println("Usuario:");
            user=lector.nextLine();
            if(buscarCliente(user, server.getClientes())!=null){
                System.out.println("existe");
            }else{
                System.out.println("Introduce un puerto");
                puerto=lector.nextInt();
                lector.nextLine();
                //creo un usuario, lo conecto y lo añado
                myClient.setName(user);
                myClient.setPort(puerto);
                server.clientes.add(myClient);
            }

//            try {
//                DatagramSocket socket= new DatagramSocket();
//                String mensaje= "holamundito";
//                byte[] mensajeAByte= mensaje.getBytes();
//
//                DatagramPacket paquete= new DatagramPacket(mensajeAByte, mensajeAByte.length, InetAddress.getByName("localhost"),1234);
//                socket.send(paquete);
//            } catch (SocketException e) {
//                throw new RuntimeException(e);
//            } catch (UnknownHostException e) {
//                throw new RuntimeException(e);
//            } catch (IOException e) {
//                throw new RuntimeException(e);
//            }
        });
        cliente.start();
    }
    private static Cliente buscarCliente(String user, ArrayList<Cliente> clientes) {
        boolean find= false;
        int contador=0;
        Cliente myClient=null;

        if(!clientes.isEmpty()){
            while (!find){
                if(clientes.get(contador).getName().equals(user)){
                    myClient=clientes.get(contador);
                    find=true;
                }
            }
        }
        return myClient;
    }
}
