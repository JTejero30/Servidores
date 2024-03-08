package Telnet;

import org.apache.commons.net.telnet.TelnetClient;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class MyTelnet {
    private String host;
    private TelnetClient cliente;
    private InputStream inputStream;
    private OutputStream outputStream;

    public MyTelnet(String host) throws IOException {
        cliente= new TelnetClient();
        cliente.connect(host);
        System.out.println("Conectado "+host);
        this.inputStream= cliente.getInputStream();
        this.outputStream= cliente.getOutputStream();

        leerSalida("login: ");
        escribirComando("javi");
        leerSalida("Password: ");
        escribirComando("123456");
        leerSalida("javi@javi-VirtualBox:~$");
    }
    void escribirComando(String comando) throws IOException {
        outputStream.write((comando+"\n").getBytes());
        outputStream.flush();
    }
    void leerSalida(String fin) throws IOException {
        String cadena= "";
        int c;
        while (!cadena.contains(fin)){
            c=inputStream.read();
            cadena= cadena+(char)c;
            System.out.print((char) c);
        }
    }
}
