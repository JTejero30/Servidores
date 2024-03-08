package ChatUDPEncrypt;

import javax.crypto.BadPaddingException;
import javax.crypto.Cipher;
import javax.crypto.IllegalBlockSizeException;
import javax.crypto.NoSuchPaddingException;
import java.io.*;
import java.net.*;
import java.security.*;
import java.util.Base64;

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
                    if(!mensaje.equals("archivo")){
                        byte[] bytes = mensaje.getBytes();
                        DatagramPacket paquete = new DatagramPacket(bytes, bytes.length, InetAddress.getLocalHost(), 999);
                        socket.send(paquete);
                    }else{

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
                    System.out.println(paqueteRecibidoString);
                    String desencriptado = desencriptar(paqueteRecibidoString);
                    System.out.println(desencriptado);
                } catch (NoSuchPaddingException | NoSuchAlgorithmException | InvalidKeyException |
                         IllegalBlockSizeException | BadPaddingException | IOException e) {
                    throw new RuntimeException(e);
                }
            }
        });
        recibir.start();
        enviar.start();
    }
    private static String desencriptar(String paqueteRecibidoString) throws NoSuchPaddingException, NoSuchAlgorithmException, InvalidKeyException, IllegalBlockSizeException, BadPaddingException {
        Cipher cifrado = Cipher.getInstance("RSA");
        cifrado.init(Cipher.DECRYPT_MODE, privada);
        byte[] desencriptado = cifrado.doFinal(Base64.getDecoder().decode(paqueteRecibidoString));

        return new String(desencriptado);
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
