package ftp_client;

import java.io.IOException;

public class ClientMain {
    public static void main(String[] args) throws IOException {
        FtpClient client = new FtpClient();
        client.connect("localhost",959);
    }
}
