package Chat;

import javax.management.monitor.StringMonitor;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.locks.ReentrantLock;

public class Server {
    public static void main(String[] args) throws IOException {
        ServerSocket serverSocket = new ServerSocket(1234);
        Map<String, Socket> users = new HashMap<String, Socket>();
        ReentrantLock reentrant = new ReentrantLock();

        while (true){
            Thread hilo= new Thread(()->{
                try {
                    Socket cliente= serverSocket.accept();
                    BufferedReader buff= new BufferedReader(new InputStreamReader(cliente.getInputStream()));
                    String nombreUser= buff.readLine();
                    //si no esta registrado lo registra
                    reentrant.lock();
                    if(users.get(nombreUser)==null){
                        System.out.println(nombreUser);
                        users.put(nombreUser,cliente);
                    }
                    reentrant.unlock();
                    //lo mandamos al remitente
                    String remitente= buff.readLine();
                    Socket remitenteSocket= users.get(remitente);
                    if(remitenteSocket!=null){
                        //enviamos el mensaje
                        String mensaje=buff.readLine();
                        //creamos la salida para ese cliente
                        OutputStream salida= remitenteSocket.getOutputStream();
                        salida.write((mensaje+"\n").getBytes());
                        salida.flush();
                    }
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            });
            hilo.start();
        }
    }
}
