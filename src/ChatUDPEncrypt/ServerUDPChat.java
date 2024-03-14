package ChatUDPEncrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
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
    public static Map<String, SocketAddress> clientesOnline;
    private static Map<SocketAddress, PublicKey> claves;
    private static int clientNumber;
    private static DatagramSocket serverMensajes;
    private static DatagramSocket serverFiles;
    private static ReentrantLock lock;

    private static ArrayList<String> nombres = new ArrayList<>();
    private static int contador = 0;

    public static void main(String[] args) throws IOException, ClassNotFoundException {
        claves = new HashMap<>();
        lock = new ReentrantLock();
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


        Thread mensajes = new Thread(() -> {
            //while true recibe clientes
            try {
                serverMensajes = new DatagramSocket(999);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
            while (true) {
                String userName;
                //creamos un paquete para el mensaje que va a llegar
                byte[] mensaje = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length);
                //recibimos el paquete
                try {
                    serverMensajes.receive(paquete);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //bloqueamos
                lock.lock();
                String paqueteString = new String(paquete.getData(), 0, paquete.getLength());
                if (new String(paquete.getData(), 0, paquete.getLength()).equals("exit")) {
                    desconectarCliente(paquete.getSocketAddress());
                }else{
                    SocketAddress direccionCliente = paquete.getSocketAddress();
                    if (clienteExiste(direccionCliente)) {
                        //si el cliente ya existia
                        userName = userName(direccionCliente);
                        clientesOnline.forEach((cliente, socket) -> {
                            //Encriptamos el mensaje por cada destinatario conectado
                            String encriptado = encryptMensaje(paqueteString, socket);
                            //reenviamos los mensajes a todos los de la lista
                            try {
                                if (!userName.equals(cliente)) {
                                    reenviarMensajes(encriptado, userName, socket);
                                }
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        });
                    } else {
                        //registramos su clave publica en un map
                        ByteArrayInputStream inputStream = new ByteArrayInputStream(paquete.getData());
                        ObjectInputStream objectInputStream = null;
                        try {
                            objectInputStream = new ObjectInputStream(inputStream);
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        }
                        PublicKey clavePublica = null;
                        try {
                            clavePublica = (PublicKey) objectInputStream.readObject();
                        } catch (IOException e) {
                            throw new RuntimeException(e);
                        } catch (ClassNotFoundException e) {
                            throw new RuntimeException(e);
                        }
                        claves.put(direccionCliente, clavePublica);
                        userName = pickName();
                        //añadimos el cliente a clientes conectados
                        clientesOnline.put(userName, direccionCliente);
                }
                    //mostramos mensajes de informacion
                    //System.out.println("Mensaje: " + paqueteString);
                    System.out.println("Recibido de: " + userName);
                    //desbloqueamos
                    lock.unlock();
                }


            }
        });
        mensajes.start();
        Thread files = new Thread(() -> {
            try {
                serverFiles = new DatagramSocket(1000);
            } catch (SocketException e) {
                throw new RuntimeException(e);
            }
            //while true recibe clientes
            while (true) {
                String userName;
                //creamos un paquete para el mensaje que va a llegar
                byte[] mensaje = new byte[1024];
                DatagramPacket paquete = new DatagramPacket(mensaje, mensaje.length);
                //recibimos el paquete
                try {
                    serverFiles.receive(paquete);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //bloqueamos
                lock.lock();
                String paqueteString = new String(paquete.getData(), 0, paquete.getLength());
                SocketAddress direccionCliente = paquete.getSocketAddress();

                ByteArrayInputStream inputStream = new ByteArrayInputStream(paquete.getData());
                ObjectInputStream objectInputStream = null;
                try {
                    objectInputStream = new ObjectInputStream(inputStream);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
                //____________________________________
                try {
                    File archivoRecibido = (File) objectInputStream.readObject();
                    userName = userName(direccionCliente);
                    clientesOnline.forEach((cliente, socket) -> {
                        //reenviamos los mensajes a todos los de la lista
                        if (!userName.equals(cliente)) {
                            try {
                                reenviarFiles(archivoRecibido, userName, socket);
                            } catch (IOException e) {
                                throw new RuntimeException(e);
                            }
                        }
                    });

                } catch (IOException e) {
                    throw new RuntimeException(e);
                } catch (ClassNotFoundException e) {
                    throw new RuntimeException(e);
                }
                //desbloqueamos
                lock.unlock();
            }
        });
        files.start();
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
        byte[] mensajeBytes = paqueteString.getBytes();
        DatagramPacket paquete = new DatagramPacket(mensajeBytes, mensajeBytes.length, socket);
        serverMensajes.send(paquete);
    }

    private static void reenviarFiles(File archivoRecibido, String userName, SocketAddress socket) throws IOException {
        FileInputStream stream = new FileInputStream(archivoRecibido);
        int c = 0;
        String cadena = "Archivo: " + archivoRecibido.getAbsolutePath();
        while ((c = stream.read()) != -1) {
            if (cadena.length() < 100) {
                cadena += (char) c;
            } else {
                byte[] mensajeBytes = encryptMensaje(cadena, socket).getBytes();
                DatagramPacket paquete = new DatagramPacket(mensajeBytes, mensajeBytes.length, socket);
                serverFiles.send(paquete);
                cadena = "";
            }
        }
    }

    public static void desconectarCliente(SocketAddress socketAddress) {
        String nombreRemitente = userName(socketAddress);
        String mensaje = nombreRemitente + " se ha desconectado.";
        //elimino al cliente de clientes online
        clientesOnline.remove(nombreRemitente);
        /**/
        clientesOnline.forEach((key, value) -> {
            String encriptado = encryptMensaje(mensaje, value);
            //reenviamos los mensajes a todos los de la lista
            try {
                reenviarMensajes(encriptado, key, value);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        });

    }
}
