package Confirmacion;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",123);
        OutputStream out = socket.getOutputStream();
        out.write(("Hola \n").getBytes());
        out.flush();

        BufferedReader buf= new BufferedReader(new InputStreamReader(socket.getInputStream()));
        System.out.println(buf.readLine());

    }
}
