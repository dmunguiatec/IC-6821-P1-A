/*
 * This Java source file was generated by the Gradle 'init' task.
 */
package edu.byohttp;

import java.io.IOException;
import java.net.ServerSocket;

public class ByoHttp {

    private final int port;
    private final String resourcesPath;

    ByoHttp(int port, String resourcesPath) {
        this.port = port;
        this.resourcesPath = resourcesPath;
    }

    void run() {
        try (ServerSocket serverSocket = new ServerSocket(this.port)) {
            while (true) {
                new Thread(new SocketMessageRunnable(serverSocket.accept())).start();
            }
        } catch (IOException e) {
            System.err.println("Could not listen on port " + this.port);
            System.exit(-1);
        }
    }

    public static void main(String... args) {
        if (args.length != 2) {
            System.err.println("Usage: java ByoHttp <port number> <resources path>");
            System.exit(1);
        }

        ByoHttp app = new ByoHttp(Integer.parseInt(args[0]), args[1]);
        app.run();
    }
}
