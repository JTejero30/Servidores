package IVA_TCP;

import java.io.*;
import java.net.Socket;
import java.util.ArrayList;


public class Cliente  {
    public static void main(String[] args) throws IOException {
        ArrayList<Producto> lista = new ArrayList<Producto>();
        lista.add(new Producto(1.50,"Manzana"));
        lista.add(new Producto(2.60,"Ananas"));
        lista.add(new Producto(3.10,"Platano"));
        lista.add(new Producto(2.40,"Mandarina"));
        lista.add(new Producto(6.10,"Aguacate"));
        //es lo que va a enviar el cliente 1
        //creo la salida
        Socket cliente = new Socket("localhost",1234);
        //creamos una salida especial ObjectOutput para enviar un arraylist(objeto)
        OutputStream out = cliente.getOutputStream();
        ObjectOutputStream objectOut=new ObjectOutputStream(out);
        //escribimos el array
        objectOut.writeObject(lista);

        //creamos la entrada para recibir el resultado
        InputStream resultado= cliente.getInputStream();
        int c;
        while((c = resultado.read()) != -1) {
            System.out.print((char)c);
        }
    }
}
