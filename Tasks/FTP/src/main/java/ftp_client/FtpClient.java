package ftp_client;

import java.io.IOException;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.FileChannel;
import java.nio.channels.SelectionKey;
import java.nio.channels.Selector;
import java.nio.channels.SocketChannel;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.Iterator;

public class FtpClient {
    private Selector selector;

    public FtpClient() {
    }

    public void connect(String hostName, int port) throws IOException {
        selector = Selector.open();
        SocketChannel clientChannel = SocketChannel.open();
        clientChannel.configureBlocking(false);
        clientChannel.connect(new InetSocketAddress(hostName, port));
        clientChannel.register(selector, SelectionKey.OP_CONNECT);
        while (true) {
            selector.select();
            Iterator<SelectionKey> iterator = selector.selectedKeys().iterator();
            while (iterator.hasNext()) {
                SelectionKey key = iterator.next();
                iterator.remove();
                SocketChannel concreteClient = (SocketChannel) key.channel();

                if (key.isConnectable()) {
                    if (concreteClient.isConnectionPending()) {
                        concreteClient.finishConnect();
                    }
                    concreteClient.register(selector, SelectionKey.OP_WRITE);
                    continue;
                }
                if (key.isWritable()) {
                    sendFile(concreteClient);
                    concreteClient.close();
                    return;
                }
            }
        }
    }

    private void sendFile(SocketChannel concreteClient) throws IOException {
        String fileName = "C:\\Users\\STAS\\Desktop\\Marks.pdf";
        Path path = Paths.get(fileName);
        FileChannel fileChannel = FileChannel.open(path);
        ByteBuffer buffer = ByteBuffer.allocate(10240);
        int bytesRead = 0;
        int counter = 0;
        do {
            bytesRead = fileChannel.read(buffer);
            if (bytesRead <= 0)
                break;
            counter += bytesRead;
            buffer.flip();
            do {
                bytesRead -= concreteClient.write(buffer);
            } while (bytesRead > 0);
            buffer.clear();
        } while (true);
        fileChannel.close();
        System.out.println("finished");
    }
}
