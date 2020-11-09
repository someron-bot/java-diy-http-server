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

    /**
     * Handle the request
     */
    @Override
    public void run() {
        try (clientSocket) {
            HttpRequest req = new HttpRequest(new String(in.readNBytes(in.available()), StandardCharsets.UTF_8));
            HttpResponse res = new HttpResponse();
            Path path = Paths.get((String) Main.config.get("webroot"), req.path);
            File file = path.toFile();
            // Checks if file is inside webroot
            if(!file.getCanonicalPath().startsWith(Main.webroot.getCanonicalPath())) {
                res.setStatus(404, "Not Found");
            } else {
                try {
                    if(checkRequest(req, res)) {
                        // Functions for HTTP methods
                        switch(req.method) {
                            case GET, HEAD -> getFile(req, res, file);
                            case DELETE -> deleteFile(req, res, file);
                            case OPTIONS -> optionsFile(req, res, file);
                            case POST -> postFile(req, res, file);
                            case PUT -> putFile(req, res, file);
                        }
                    }
                } catch(Exception e) {
                    // Sends a "500 Internal Server Error" Status if a Error occurs
                    res.setStatus(500, "Internal Server Error");
                    e.printStackTrace();
                }
            }
            // Send the actual data
            out.write(res.toString().getBytes());
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * Checks if the requests are valid and allowed
     * @param req The Request
     * @param res The Response
     * @return {@code true} if the Request is valid and allowed. {@code false} otherwise
     */
    public boolean checkRequest(HttpRequest req, HttpResponse res) {
        // Checks for invalid/unimplemented methods
        if(req.method == HttpRequest.Method.UNKNOWN) {
            res.setStatus(501, "Not Implemented");
            return false;
            // Checks for forbidden methods
        } else if(Boolean.parseBoolean((String) Main.config.get("readOnly")) && (req.method != HttpRequest.Method.GET && req.method != HttpRequest.Method.HEAD)) {
            res.setStatus(405, "Method Not Allowed");
            return false;
        }
        return true;
    }

    /**
     * For {@code GET} requests
     * @param req The Request Object
     * @param res The Response Object
     * @param file The file the Request is routed to
     * @throws Exception If something goes wrong
     */
    private void getFile(HttpRequest req, HttpResponse res, File file) throws Exception {
        if(file.exists()) {
            // Read file
            res.attachBody(new String(new FileInputStream(file).readAllBytes(), StandardCharsets.UTF_8));
            // Handle nonexistent files
        } else {
            res.setStatus(404, "Not Found");
        }
        if(req.method == HttpRequest.Method.HEAD) res.body = null;
    }

    /**
     * For {@code OPTIONS} requests
     * @param req The Request Object
     * @param res The Response Object
     * @param file The file the Request is routed to
     * @throws Exception If something goes wrong
     */
    private void optionsFile(HttpRequest req, HttpResponse res, File file) throws Exception {
        if(file.exists()) {
            res.setStatus(204, "No Content");
            if(Boolean.parseBoolean((String) Main.config.get("readOnly"))) res.headers.put("Allow", "GET, HEAD, OPTIONS");
            else res.headers.put("Allow", "GET, HEAD, POST, PUT, DELETE, OPTIONS");
            // Handle nonexistent files
        } else {
            res.setStatus(404, "Not Found");
        }
    }

    /**
     * For {@code DELETE} requests
     * @param req The Request Object
     * @param res The Response Object
     * @param file The file the Request is routed to
     * @throws Exception If something goes wrong
     */
    private void deleteFile(HttpRequest req, HttpResponse res, File file) throws Exception {
        if(file.exists()) {
            if(!file.delete()) {
                res.setStatus(500, "Internal Server Error");
                return;
            }
            res.setStatus(204, "No Content");
            // Handle nonexistent files
        } else {
            res.setStatus(404, "Not Found");
        }
    }

    /**
     * For {@code POST} requests
     * @param req The Request Object
     * @param res The Response Object
     * @param file The file the Request is routed to
     * @throws Exception If something goes wrong
     */
    private void postFile(HttpRequest req, HttpResponse res, File file) throws Exception {
        if(req.body == null) { res.setStatus(400, "Bad Request"); return; }
        // Handle existing files
        if(file.exists()) {
            res.setStatus(409, "Conflict");
        } else {
            // Write to file
            file.createNewFile();
            FileWriter writer = new FileWriter(file);
            writer.write(req.body);
            writer.close();
            res.setStatus(201, "Created");
        }
    }

    /**
     * For {@code PUT} requests
     * @param req The Request Object
     * @param res The Response Object
     * @param file The file the Request is routed to
     * @throws Exception If something goes wrong
     */
    private void putFile(HttpRequest req, HttpResponse res, File file) throws Exception {
        if(req.body == null) { res.setStatus(400, "Bad Request"); return; }
        if(!file.exists()) {
            // Handle existing files
            file.createNewFile();
            res.setStatus(201, "Created");
        } else {
            res.setStatus(204, "No Content");
        }
        // Write to file
        FileWriter writer = new FileWriter(file);
        writer.write(req.body);
        writer.close();
    }
}
