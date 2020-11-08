package de.someron.diyHttpServer;

import de.someron.diyHttpServer.parsing.HttpRequest;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final InputStream in;
    private final OutputStream out;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = clientSocket.getInputStream();
        out = clientSocket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            String req = new String(in.readNBytes(in.available()), StandardCharsets.UTF_8);
            System.out.println(req);
            new HttpRequest(req);
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
