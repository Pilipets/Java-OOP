package ftp_server.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;

public class FtpReader {
    public int readFile(SocketChannel socketChannel, FileChannel fileChannel, int BufferSize) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(BufferSize);
        int res = 0;
        int counter = 1;
        do {
            buffer.clear();
            res = socketChannel.read(buffer);
            buffer.flip();
            if (res > 0) {
                fileChannel.write(buffer);
                counter += res;
            }
        } while (res > 0);
        return counter;
    }
}
