package edu.byohttp;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;

public class EchoMessage {
    public void echo(InputStream in, OutputStream out) throws IOException {
        in.transferTo(out);
    }
}
