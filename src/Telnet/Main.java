package Telnet;

import java.io.IOException;

public class Main {
    public static void main(String[] args) throws IOException {
        String virgulilla="javi@javi-VirtualBox:~$";
        MyTelnet telnesito= new MyTelnet("10.199.241.91");
        telnesito.escribirComando("ls -l");
        telnesito.leerSalida(virgulilla);
    }
}
