package ChatUDPEncrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.io.ObjectInputStream;
import java.net.*;
import java.security.InvalidKeyException;
import java.security.NoSuchAlgorithmException;
import java.security.PublicKey;
import java.util.ArrayList;
import java.util.Base64;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.concurrent.locks.ReentrantLock;

public class ServerUDPChat {
    private static Map<String, SocketAddress> clientesOnline;
    private static Map<SocketAddress, PublicKey> claves;
    private static int clientNumber;
    private static DatagramSocket server;
    private static ReentrantLock lock;

    private static ArrayList<String> nombres = new ArrayList<>();
    private static int contador = 0;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        claves = new HashMap<>();

        nombres.add("Juan");
        nombres.add("María");
        nombres.add("Pedro");
        nombres.add("Ana");
        nombres.add("Luis");
        nombres.add("Laura");
        nombres.add("Carlos");
        nombres.add("Sofía");
        nombres.add("Diego");
        nombres.add("Valeria");
        clientesOnline = new HashMap<>();

        clientNumber = 1;
        //se crea en el puerto 999
        server = new DatagramSocket(999);
        //while true recibe clientes
        while (true) {
            String userName;
            //creamos un paquete para el mensaje que va a llegar
            byte[] mensaje = new byte[1024];
            DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length);
            //recibimos el paquete
            server.receive(paquete);
            //bloqueamos
            //lock.lock();
            String paqueteString = new String(paquete.getData(), 0, paquete.getLength());

            SocketAddress direccionCliente = paquete.getSocketAddress();
            if (clienteExiste(direccionCliente)) {
                //si el cliente ya existia
                userName = userName(direccionCliente);
                clientesOnline.forEach((cliente, socket) -> {
                    //Encriptamos el mensaje por cada destinatario conectado
                    String encriptado = encryptMensaje(paqueteString, socket);
                    //reenviamos los mensajes a todos los de la lista
                    try {
                        if (userName != cliente) {
                            reenviarMensajes(encriptado, userName, socket);
                        } else {
                            reenviarMensajes("Mensaje enviado", userName, socket);
                        }
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                });
            } else {
                //registramos su clave publica en un map
                ByteArrayInputStream inputStream = new ByteArrayInputStream(paquete.getData());
                ObjectInputStream objectInputStream = new ObjectInputStream(inputStream);
                PublicKey clavePublica = (PublicKey) objectInputStream.readObject();
                claves.put(direccionCliente, clavePublica);
                userName = pickName();
                //añadimos el cliente a clientes conectados
                clientesOnline.put(userName, direccionCliente);
            }

            //mostramos mensajes de informacion
            //System.out.println("Mensaje: " + paqueteString);
            System.out.println("Recibido de: " + userName);


            //desbloqueamos
            //lock.unlock();
        }
    }

    private static String encryptMensaje(String paqueteString, SocketAddress socket) {

        byte[] encriptado;
        //por cada cliente conectado
        //cogemos su clave publica
        PublicKey publicKey = claves.get(socket);
        //encriptamos el mensaje con la clave de cada cliente
        try {
            Cipher cifrado = Cipher.getInstance("RSA");
            cifrado.init(Cipher.ENCRYPT_MODE, publicKey);
            encriptado = cifrado.doFinal(paqueteString.getBytes());
        } catch (NoSuchAlgorithmException | NoSuchPaddingException | IllegalBlockSizeException | BadPaddingException |
                 InvalidKeyException e) {
            throw new RuntimeException(e);
        }

        return Base64.getEncoder().encodeToString(encriptado);
    }

    /**
     * Metodo que devuelve el nombre de una conexion ya existente
     *
     * @param direccionCliente el socketAddress
     * @return el nombre
     */
    private static String userName(SocketAddress direccionCliente) {
        AtomicReference<String> userName = new AtomicReference<>("user");
        clientesOnline.forEach((key, value) -> {
            if (direccionCliente.equals(value)) {
                userName.set(key);
            }
        });
        return userName.get();
    }

    /**
     * Metodo que escoge un nombre de la lista
     *
     * @return el nombre
     */
    private static String pickName() {
        String name = "Usuario";
        if (!nombres.isEmpty()) {
            name = nombres.get(contador);
            nombres.remove(contador);
        }
        contador++;
        return name;
    }

    private static boolean clienteExiste(SocketAddress direccionCliente) {
        AtomicBoolean existe = new AtomicBoolean(false);
        int contador = 0;

        clientesOnline.forEach((key, value) -> {
            if (value.equals(direccionCliente)) {
                existe.set(true);
            }
        });
        System.out.println(existe.get());
        return existe.get();
    }

    private static void reenviarMensajes(String paqueteString, String nombreCliente, SocketAddress socket) throws IOException {
        String mensaje = "Nuevo mensaje de " + nombreCliente + " \n" +
                paqueteString;

        byte[] mensajeBytes = mensaje.getBytes();
        DatagramPacket paquete = new DatagramPacket(mensajeBytes, mensajeBytes.length, socket);
        server.send(paquete);
    }
}
