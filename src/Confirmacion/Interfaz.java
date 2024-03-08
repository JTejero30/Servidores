package Confirmacion;

import javax.swing.*;
import java.awt.*;
import java.io.IOException;
import java.net.ServerSocket;

public class Interfaz {
    private JPanel panel1;
    private JTextArea holaTextArea;

    public static void main(String[] args) {
        EventQueue.invokeLater(new Runnable() {
            @Override
            public void run() {
                try{

                }catch (Exception e){
                    Interfaz interfaz= new Interfaz();

                }
            }
        });
        /*private void initialize() {
            JFrame frame = new JFrame("ChatView");
            frame.setContentPane(new Interfaz().panel1);
            frame.setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
            frame.setSize(200, 300);
            frame.setVisible(true);

        }*/


    }
    private void startServer() throws IOException {
        ServerSocket server= new ServerSocket(123);
        while (true){

        }
    }
}
