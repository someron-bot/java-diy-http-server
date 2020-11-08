package de.someron.diyHttpServer;

import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static int PORT = 80;
    public static ExecutorService pool = Executors.newCachedThreadPool();

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
                pool.submit(new ClientHandler(client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
