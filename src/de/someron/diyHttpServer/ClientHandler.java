package de.someron.diyHttpServer;

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
            out.write("Welcome!".getBytes());
            while(true) {
                String line = in.readLine().trim();
                out.write("processing\n".getBytes());
                String[] args = line.split(" ");
                switch (args[0].toUpperCase()) {
                    case "GET" -> out.write((Main.data.get(args[1]) + "\n").getBytes());
                    case "SET" -> {
                        Main.data.put(args[1], args[2]);
                        out.write("done!\n".getBytes());
                    }
                    case "CLOSE" -> {
                        out.write("bye!\n".getBytes());
                        clientSocket.close();
                        return;
                    }
                    default -> {
                        out.write("wdym?\n".getBytes());
                        out.flush();
                    }
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
