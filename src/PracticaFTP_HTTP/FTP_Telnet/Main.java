package PracticaFTP_HTTP.FTP_Telnet;

import java.io.IOException;
import java.util.Scanner;

public class Main {

    private static String comando;

    public static void main(String[] args) throws IOException {
        ClienteTelnet cliente= new ClienteTelnet("10.199.241.91");
        cliente.escribirComando("ftp 127.0.0.1");
        cliente.leerSalida(":javi): ");
        cliente.escribirComando("javi");
        cliente.leerSalida("Password:");
        cliente.escribirComando("123456");
        cliente.leerSalida("Using binary mode to transfer files.");
        Scanner lector = new Scanner(System.in);

        while (comando!="quit"){
            System.out.println();
            cliente.leerSalida("ftp>");
            comando = lector.nextLine();
            cliente.escribirComando(comando);

        }
        System.out.println("Conexion cerrada");
    }
}
