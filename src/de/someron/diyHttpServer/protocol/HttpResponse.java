package de.someron.diyHttpServer.protocol;

import java.util.HashMap;

public class HttpResponse {
    private static final float VERSION = 1.0f;

    private int status = 200;
    private String statusMessage = "OK";
    public HashMap<String, String> headers = new HashMap<String, String>();
    private String body = "";

    public void setStatus(int status, String message) {
        this.status = status;
        this.statusMessage = message;
    }

    public void attachBody(String contentType, String body) {
        headers.put("Content-Type", contentType);
        headers.put("Content-Length", Integer.toUnsignedString(body.length()));
        this.body = body;
    }

    public void attachBody(String data) {
        this.attachBody("text/plain", data);
    }

    @Override
    public String toString() {
        headers.put("Server", "somerons-nice-server");
        StringBuilder res = new StringBuilder();
        res.append("HTTP/").append(VERSION).append(" ").append(status).append(" ").append(statusMessage).append("\r\n");
        for(String key : headers.keySet()) {
            res.append(key).append(": ").append(headers.get(key)).append("\r\n");
        }
        res.append("\r\n").append(body);
        return res.toString();
    }
}
