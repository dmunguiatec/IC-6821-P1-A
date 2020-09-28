package edu.byohttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.net.Socket;

public class SocketMessageRunnable implements Runnable {

    private final InputStream in;
    private final OutputStream out;

    public SocketMessageRunnable(Socket socket) throws IOException {
        this.in = socket.getInputStream();
        this.out = socket.getOutputStream();
    }

    @Override
    public void run() {
        try {
            //TODO: cambiar este echo por una llamada para procesar el mensaje de request
            new EchoMessage().echo(in, out);

            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            // TODO: esta es una mala práctica de manejo de errores, mejorar
            e.printStackTrace();
        }
    }
}
