package Chat;

import java.io.*;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost", 1234);
        OutputStream out = socket.getOutputStream();
        BufferedReader buff = new BufferedReader(new InputStreamReader(System.in));
        BufferedReader buff2 = new BufferedReader(new InputStreamReader(socket.getInputStream()));

        InputStream input= socket.getInputStream();



        System.out.println("¿Quien eres?");
        enviarServer(buff.readLine(), out);
        System.out.println("¿A quien quieres eviar?");
        enviarServer(buff.readLine(), out);

        while (true) {
            System.out.println("Introduce el mensaje");
            enviarServer(buff.readLine(), out);
            Thread recibir= new Thread(()->{
                String cadena;
                try {
                    while((cadena= buff2.readLine())!=null){
                        System.out.println(cadena);
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }

            });
            recibir.start();
        }

    }
    private static void enviarServer(String comando, OutputStream out) throws IOException {
        out.write((comando + "\n").getBytes());
        out.flush();
    }
}
