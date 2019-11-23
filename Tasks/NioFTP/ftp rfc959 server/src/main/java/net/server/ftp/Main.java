package net.server.ftp;

import java.io.IOException;

public class Main {
    public static void main(String[]args) throws IOException {
        FtpServer server = new FtpServer();
        server.start();
    }
}
