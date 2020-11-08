package de.someron.diyHttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.HashMap;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static ExecutorService pool = Executors.newCachedThreadPool();
    public static HashMap<String, String> data = new HashMap();
    public static Properties config = new Properties();

    public static void main(String[] args) {
        try {
            File configuration = new File("./config.properties");
            config.load(new FileInputStream(configuration));
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public static void listen() throws IOException {
        ServerSocket server = new ServerSocket(Integer.parseInt((String) config.get("port")));
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
