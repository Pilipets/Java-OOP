package ftp_server.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FtpFileManager {
    private final Configuration config;
    private final FileChannel fileChannel;
    public FtpFileManager(Configuration config) throws IOException {
        this.config = config;
        fileChannel = FileChannel.open(Paths.get(config.getOutputPath()),
                EnumSet.of(StandardOpenOption.CREATE_NEW, StandardOpenOption.WRITE));
    }
    public void close() throws IOException {
        fileChannel.close();
    }
    public void saveDataFrom(SocketChannel client) throws IOException {
        ByteBuffer buffer = ByteBuffer.allocate(config.getBufferSize());
        int res = 0;
        do {
            buffer.clear();
            res = client.read(buffer);
            buffer.flip();
            if (res > 0) {
                fileChannel.write(buffer);
            }
        } while (res > 0);
    }
}
