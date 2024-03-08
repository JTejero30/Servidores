package ServerTCP;

import java.net.InetAddress;
import java.net.UnknownHostException;

public class TraducirDNS {
    public static void main(String[] args) throws UnknownHostException {
        String host="www.imf.com";

        InetAddress direccion= InetAddress.getByName(host);

        System.out.println(direccion);
        System.out.println(direccion.getHostAddress());
    }
}