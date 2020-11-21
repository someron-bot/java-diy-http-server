package de.someron.diyHttpServer;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.Properties;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

public class Main {
    public static ExecutorService pool = Executors.newCachedThreadPool();
    public static Properties config = new Properties();
    public static File webroot;
    private static ServerSocket server;

    /**
     * Preparations for listening
     * @param args The program args. Ignored
     */
    public static void main(String[] args) {
        try {
            File configuration = new File("./config.properties");
            config.load(new FileInputStream(configuration));
            webroot = new File(config.getProperty("webroot"));
            listen();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * The actual listening
     * @throws IOException If something goes wrong
     */
    public static void listen() throws IOException {
        server = new ServerSocket(Integer.parseInt((String) config.get("port")));
        Runtime.getRuntime().addShutdownHook(new Thread(() -> {
            try {
                server.close();
                System.out.println("Program shut down");
            } catch (IOException e) {
                e.printStackTrace();
            }
        }));
        while(true) {
            try {
                Socket client = server.accept(); // Get the socket-Connection to the client
                pool.submit(new ClientHandler(client));
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}
