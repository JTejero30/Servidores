package ServerFTP_TCP;
import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class Server {
    public static void main(String[] args) throws IOException {
        String ruta= System.getProperty("user.home")+"\\Desktop\\prueba.java";
        FileOutputStream file= new FileOutputStream(ruta);
        System.out.println(ruta);

        ServerSocket serverSocket= new ServerSocket(12);
        Socket socket= serverSocket.accept();

        InputStream inputStream= socket.getInputStream();
        BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(inputStream));
        int size=Integer.parseInt( bufferedReader.readLine());
      //  int size= inputStream.read();
        System.out.println(size);

        int c=0;
        int contador = 0;
        while (contador<size){
            c= inputStream.read();
            contador++;
            System.out.print((char) c);
            file.write(c);
        }
    }
}
