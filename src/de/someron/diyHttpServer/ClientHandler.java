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
            Path path = Paths.get((String) Main.config.get("webroot"), req.path);
            File file = path.toFile();
            if(!file.getCanonicalPath().startsWith(Main.webroot.getCanonicalPath())) {
                res.setStatus(404, "Not Found");
            } else {
                try {
                    if(checkRequest(req, res)) {
                        switch(req.method) {
                            case GET, HEAD -> getFile(req, res, file);
                            case DELETE -> deleteFile(req, res, file);
                            case OPTIONS -> optionsFile(req, res, file);
                            case POST -> postFile(req, res, file);
                            case PUT -> putFile(req, res, file);
                        }
                    }
                } catch(Exception e) {
                    res.setStatus(500, "Internal Server Error");
                    e.printStackTrace();
                }
            }
            out.write(res.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public boolean checkRequest(HttpRequest req, HttpResponse res) {
        if(req.method == HttpRequest.Method.UNKNOWN) {
            res.setStatus(501, "Not Implemented");
            return false;
        } else if(Boolean.parseBoolean((String) Main.config.get("readOnly")) && (req.method != HttpRequest.Method.GET && req.method != HttpRequest.Method.HEAD)) {
            res.setStatus(405, "Method Not Allowed");
            return false;
        }
        return true;
    }

    private void getFile(HttpRequest req, HttpResponse res, File file) throws Exception {
        System.out.println("GET");
        if(file.exists()) {
            res.attachBody(new String(new FileInputStream(file).readAllBytes(), StandardCharsets.UTF_8));
        } else {
            res.setStatus(404, "Not Found");
        }
        if(req.method == HttpRequest.Method.HEAD) res.attachBody("");
    }

    private void optionsFile(HttpRequest req, HttpResponse res, File file) {
        if(file.exists()) {
            res.setStatus(204, "No Content");
            if(Boolean.parseBoolean((String) Main.config.get("readOnly"))) res.headers.put("Allow", "GET, HEAD, OPTIONS");
            else res.headers.put("Allow", "GET, HEAD, POST, PUT, DELETE, OPTIONS");
        } else {
            res.setStatus(404, "Not Found");
        }
    }

    private void deleteFile(HttpRequest req, HttpResponse res, File file) {
        if(file.exists()) {
            file.delete();
            res.setStatus(204, "No Content");
        } else {
            res.setStatus(404, "Not Found");
        }
    }

    private void postFile(HttpRequest req, HttpResponse res, File file) throws IOException {
        if(req.body == null) { res.setStatus(400, "Bad Request"); return; }
        if(file.exists()) {
            res.setStatus(409, "Conflict");
        } else {
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(req.body);
            writer.close();
            res.setStatus(201, "Created");
        }
    }

    private void putFile(HttpRequest req, HttpResponse res, File file) throws IOException {
        if(req.body == null) { res.setStatus(400, "Bad Request"); return; }
        if(!file.exists()) {
            file.createNewFile();
            res.setStatus(201, "Created");
        } else {
            res.setStatus(204, "No Content");
        }
        FileWriter writer = new FileWriter(file);
        writer.write(req.body);
        writer.close();
    }
}
