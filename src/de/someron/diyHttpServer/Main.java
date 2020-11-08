package de.someron.diyHttpServer;

public class Main {
    public static int PORT = 80;

    public static void main(String[] args) {
        if(args.length == 1) PORT = Integer.parseInt(args[0]);
    }
}
