package de.someron.diyHttpServer.protocol;

import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.HashMap;

public class HttpResponse {
    private static final float VERSION = 1.0f;

    public int status = 200;
    public String statusMessage = "OK";
    public HashMap<String, String> headers = new HashMap<>();
    public String body = "";
    public ContentType contentType = ContentType.DEFAULT;

    /**
     * Sets the status of the response
     * @param status The Status
     * @param message The message
     */
    public void setStatus(int status, String message) {
        this.status = status;
        this.statusMessage = message;
    }

    /**
     * Attaches a body to the request
     * @param contentType The Content-Type of the data
     * @param body The data
     */
    public void attachBody(ContentType contentType, String body) {
        this.contentType = contentType;
        this.body = body;
    }

    public void attachBody(File file) throws IOException {
        String content = new String(new FileInputStream(file).readAllBytes(), StandardCharsets.UTF_8);
        String[] nonce = file.getName().split("\\.");
        ContentType contentType = ContentType.getContentType(null, content, nonce[1]);
        this.attachBody(contentType, content);
    }

    @Override
    public String toString() {
        headers.put("Server", "someron's-nice-server");
        headers.put("Content-Length", Integer.toUnsignedString(body.length()));
        headers.put("Content-Type", contentType.rawValue);
        StringBuilder res = new StringBuilder();
        res.append("HTTP/").append(VERSION).append(" ").append(status).append(" ").append(statusMessage).append("\r\n");
        for(String key : headers.keySet()) {
            res.append(key).append(": ").append(headers.get(key)).append("\r\n");
        }
        res.append("\r\n").append(body);
        return res.toString();
    }
}
