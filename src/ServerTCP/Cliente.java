package ServerTCP;

import java.io.IOException;
import java.io.OutputStream;
import java.net.Socket;

public class Cliente {
    public static void main(String[] args) throws IOException {
        Socket socket = new Socket("localhost",12345);
        OutputStream out= socket.getOutputStream();
        out.write(("Robayo sca un 10 \n").getBytes());
        out.flush();
        socket.close();
    }
}
