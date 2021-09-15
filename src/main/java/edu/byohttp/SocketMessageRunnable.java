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

            /* TODO:
                En este punto tenemos un hilo corriendo, con acceso al InputStream que permite leer los bytes del
                request, y un OutputStream que permite escribir los bytes del response.
                A partir de acá podemos procesar el request para producir el response.
             */

            in.close();
            out.flush();
            out.close();
        } catch (IOException e) {
            /* TODO:
                Esta es la frontera para nuestro código, pues el hilo corre de manera desprendida del main.
                Acá deberíamos registrar este error inesperado en la bitácora para que no pase desapercibido.
             */
        }
    }
}
