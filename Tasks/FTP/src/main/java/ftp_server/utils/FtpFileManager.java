package ftp_server.utils;

import java.io.IOException;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;

public class FtpFileManager {
    private final FtpConfiguration config;
    private final FileChannel fileChannel;
    public FtpFileManager(FtpConfiguration config) throws IOException {
        this.config = config;
        fileChannel = FileChannel.open(Paths.get(config.getOutputPath()),
                EnumSet.of(StandardOpenOption.CREATE, StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE));
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
    public static void writeTo(String sourcePath, SocketChannel receiver) throws IOException {
        Path path = Paths.get(sourcePath);
        FileChannel readingChannel = FileChannel.open(path);
        ByteBuffer buffer = ByteBuffer.allocate(2048);
        int bytesRead = 0;
        do {
            bytesRead = readingChannel.read(buffer);
            if (bytesRead <= 0)
                break;
            buffer.flip();
            while (bytesRead > 0){
                bytesRead -= receiver.write(buffer);
            }
            buffer.clear();
        } while (true);
        readingChannel.close();
    }
}
