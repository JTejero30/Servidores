package ChatUDPEncrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.*;
import java.security.*;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Base64;
import java.util.Scanner;

public class ClienteUDP {
    private static PrivateKey privada;
    private static PublicKey publica;
    private static DatagramSocket socket;

    public static void main(String[] args) throws IOException, NoSuchAlgorithmException {
        socket = new DatagramSocket();
        KeyPair claves = generarClaves();
        publica = claves.getPublic();
        privada = claves.getPrivate();
        enviarClave(publica);

        Thread enviar = new Thread(() -> {
            while (true) {
                //pedimos el mensaje al cliente
                BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
                String mensaje = null;
                try {
                    mensaje = buff.readLine();
                    if (mensaje.equals("archivo")) {
                        sendFile(elegirArchivo());
                    } else if (mensaje.equals("exit")) {
                        byte[] bytes = mensaje.getBytes();
                        DatagramPacket paquete = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), 999);
                        socket.send(paquete);
                        System.out.println("Desconectado");
                        System.exit(0);
                    } else {
                        byte[] bytes = mensaje.getBytes();
                        DatagramPacket paquete = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), 999);
                        socket.send(paquete);
                        System.out.println("Mensaje enviado");

                    }
                } catch (IOException ex) {
                    throw new RuntimeException(ex);
                }
            }
        });
        Thread recibir = new Thread(() -> {
            while (true) {
                byte[] recibido = new byte[1024];
                DatagramPacket paqueteRecibido = new DatagramPacket(recibido, recibido.length);
                try {
                    socket.receive(paqueteRecibido);
                    String paqueteRecibidoString = new String(paqueteRecibido.getData(), 0, paqueteRecibido.getLength());
                    String desencriptado = desencriptar(paqueteRecibidoString);
                    if (desencriptado.contains("{")) {
                        System.out.print(desencriptado);
                    } else {
                        System.out.println("Cliente -> "+paqueteRecibido.getSocketAddress() + ":");
                        System.out.println("Fecha-> "+LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_TIME));
                        System.out.println(desencriptado);
                    }
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                         IllegalBlockSizeException | BadPaddingException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        recibir.start();
        enviar.start();
    }

    private static File elegirArchivo() throws IOException {
        Scanner lector = new Scanner(System.in);
        int opcion;
        String ruta = "";

        System.out.println("Elige el archivo que quieres enviar:");
        System.out.println("1.- Lista alumnos");
        System.out.println("2.- Lista coches");
        System.out.println("3.- Lista equipos");
        System.out.println("4.- Lista libros");
        opcion = lector.nextInt();
        lector.nextLine();

        switch (opcion) {
            case 1:
                ruta = "src/ChatUDPEncrypt/Files/Alumnos.txt";
                break;
            case 2:
                ruta = "src/ChatUDPEncrypt/Files/Coches.txt";
                break;
            case 3:
                ruta = "src/ChatUDPEncrypt/Files/Equipos.txt";
                break;
            case 4:
                ruta = "src/ChatUDPEncrypt/Files/Libros.txt";
                break;
        }
        File archivo = new File(ruta);
        return archivo;
    }

    private static String desencriptar(String paqueteRecibidoString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cifrado = Cipher.getInstance("RSA");
        cifrado.init(Cipher.DECRYPT_MODE, privada);
        byte[] desencriptado = cifrado.doFinal(Base64.getDecoder().decode(paqueteRecibidoString));
        return new String(desencriptado);
    }

    private static void sendFile(File file) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream oas = new ObjectOutputStream(byteOut);
        oas.writeObject(file);
        byte[] bytesFile = byteOut.toByteArray();
        DatagramPacket filePacket = new DatagramPacket(bytesFile, bytesFile.length, InetAddress.getLocalHost(), 1000);
        socket.send(filePacket);
    }

    private static void enviarClave(PublicKey publica) throws IOException {
        ByteArrayOutputStream byteOut = new ByteArrayOutputStream();
        ObjectOutputStream oas = new ObjectOutputStream(byteOut);
        oas.writeObject(publica);
        byte[] bytesPublica = byteOut.toByteArray();
        DatagramPacket publicData = new DatagramPacket(bytesPublica, bytesPublica.length, InetAddress.getLocalHost(), 999);
        socket.send(publicData);
    }

    private static KeyPair generarClaves() throws NoSuchAlgorithmException {
        KeyPairGenerator generador = KeyPairGenerator.getInstance("RSA");
        generador.initialize(2048);
        return generador.generateKeyPair();
    }
}