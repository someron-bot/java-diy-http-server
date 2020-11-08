package de.someron.diyHttpServer.parsing;

public class HttpRequest {
    public Method method;
    public String path;
    public float version;

    public static enum Method {
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
        try {
            String requestLine = raw.split("\r\n")[0];
            parseRequestLine(requestLine);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    private void parseRequestLine(String raw) {
        String[] args = raw.split(" ");
        method = Method.getMethod(args[0].toUpperCase());
        path = args[1];
        version = Float.parseFloat(args[2].replaceAll("HTTP/", ""));
        System.out.println("Method: " + method.name());
        System.out.println("Path: " + path);
        System.out.println("Version: " + version);
    }
}
