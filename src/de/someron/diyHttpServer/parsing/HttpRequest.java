package de.someron.diyHttpServer.parsing;

import java.util.Arrays;
import java.util.HashMap;

public class HttpRequest {
    public Method method;
    public String path;
    public float version;
    public HashMap<String, String> headers = new HashMap<String, String>();

    public enum Method {
        GET, HEAD, POST, PUT, DELETE, UNKNOWN;

        public static Method getMethod(String name) {
            switch(name) {
                case "GET" -> { return GET; }
                case "HEAD" -> { return HEAD; }
                case "POST" -> { return POST; }
                case "PUT" -> { return PUT; }
                case "DELETE" -> { return DELETE; }
                default -> { return UNKNOWN; }
            }
        }
    }

    public HttpRequest(String raw) {
        String[] lines = raw.split("\r\n");
        try {
            parseRequestLine(lines[0]);
            parseHeaders(Arrays.copyOfRange(lines, 1, ((lines.length - 1) <= 0) ? 1 : lines.length - 1));
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseRequestLine(String raw) {
        String[] args = raw.split(" ");
        if(args.length != 3) return;
        method = Method.getMethod(args[0].toUpperCase());
        path = args[1];
        version = Float.parseFloat(args[2].replaceAll("HTTP/", ""));
    }

    private void parseHeaders(String[] raw) {
        for(String line : raw) {
            String[] parts = line.split(": ");
            headers.put(parts[0], parts[1]);
        }
    }
}
