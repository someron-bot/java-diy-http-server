package de.someron.diyHttpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;

public class Main {
    public static int PORT = 80;

    public static void main(String[] args) {
        if(args.length == 1) PORT = Integer.parseInt(args[0]);
        try {
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listen() throws IOException {
        ServerSocket server = new ServerSocket(PORT);
        while(true) {
            try {
                Socket client = server.accept();
                System.out.println("Connection from " + client.getInetAddress().getHostAddress());
                client.getOutputStream().write("Hello World".getBytes());
                client.close();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
