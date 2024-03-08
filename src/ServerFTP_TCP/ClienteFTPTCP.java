package ServerFTP_TCP;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class ClienteFTPTCP {
    public static void main(String[] args) throws IOException {
        String rutaArchivo = "C:\\Users\\javit\\Desktop\\2DAM\\Programacion Servicios y Procesos\\Projects\\Servidores\\src\\ServerFTP_TCP\\ClienteFTPTCP.java";
        FileInputStream fileInputStream = new FileInputStream(rutaArchivo);
        Socket cliente= new Socket("localhost",12);
        OutputStream out= cliente.getOutputStream();

        int size = fileInputStream.available();
        out.write((size+"\n").getBytes());
        out.flush();

        int c;
        String cadena="";
        while ((c=fileInputStream.read())!=-1){
            cadena += (char) c;
        }

        out.write((cadena+"\n").getBytes());
        out.flush();

    }
}
