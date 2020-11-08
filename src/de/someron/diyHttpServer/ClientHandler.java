package de.someron.diyHttpServer;

import de.someron.diyHttpServer.protocol.HttpRequest;
import de.someron.diyHttpServer.protocol.HttpResponse;

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
        try (clientSocket) {
            new HttpRequest(new String(in.readNBytes(in.available()), StandardCharsets.UTF_8));
            HttpResponse res = new HttpResponse();
            res.setStatus(200, "OK");
            res.attachBody("text/html", "<h0>Hello World</h0>");
            out.write(res.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
