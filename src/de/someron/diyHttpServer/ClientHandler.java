package de.someron.diyHttpServer;

import de.someron.diyHttpServer.protocol.HttpRequest;
import de.someron.diyHttpServer.protocol.HttpResponse;

import java.io.*;
import java.net.Socket;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;

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
            HttpRequest req = new HttpRequest(new String(in.readNBytes(in.available()), StandardCharsets.UTF_8));
            HttpResponse res = new HttpResponse();
            try {
                readFile(req, res);
            } catch(Exception e) {
                res.setStatus(500, "Internal Server Error");
                e.printStackTrace();
            }
            out.write(res.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void readFile(HttpRequest req, HttpResponse res) throws Exception {
        Path path = Paths.get((String) Main.config.get("webroot"), req.path);
        File file = path.toFile();
        if(file.exists()) {
            res.attachBody(new String(new FileInputStream(file).readAllBytes(), StandardCharsets.UTF_8));
        } else {
            res.setStatus(404, "Not Found");
        }
    }
}
