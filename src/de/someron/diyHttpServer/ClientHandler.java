package de.someron.diyHttpServer;

import de.someron.diyHttpServer.parsing.HttpRequest;

import java.io.*;
import java.net.Socket;

public class ClientHandler implements Runnable {
    private final Socket clientSocket;
    private final BufferedReader in;
    private final OutputStream out;

    public ClientHandler(Socket clientSocket) throws IOException {
        this.clientSocket = clientSocket;
        in = new BufferedReader(new InputStreamReader(clientSocket.getInputStream()));
        out = clientSocket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            new HttpRequest(in.readLine());
            clientSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
