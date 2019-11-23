package net.server.ftp;

import net.server.Configuration;
import net.server.FtpReader;

import java.io.IOException;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.nio.ByteBuffer;
import java.nio.channels.*;
import java.nio.charset.StandardCharsets;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardOpenOption;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class FtpServer {
    Configuration configuration;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private Logger logger = Logger.getLogger(FtpServer.class.getName());
    private FtpReader reader;

    public FtpServer() {
        configuration = new Configuration();
    }

    public void start() throws IOException {
        configureServer();
        acceptConnections();
    }

    public void setActiveAddress(String host,int port){
        configuration.setAddress(host, port);
    }

    public InetAddress getActiveAddress(){
        return serverChannel.socket().getInetAddress();
    }

    private void configureServer() throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(configuration.getActiveAddress());
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
    }

    private void acceptConnections() throws IOException {
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> i = selectedKeys.iterator();

            while (i.hasNext()) {
                SelectionKey key = i.next();

                if (key.isAcceptable()) {
                    handleAccept();
                } else if (key.isReadable()) {
                    handleRead(key);
                }
                else if(key.isWritable()){
                    handleWrite(key);
                }
                i.remove();
            }
        }
    }

    private void handleWrite(SelectionKey key) throws IOException {
        System.out.println("write");
        ///SocketChannel client = (SocketChannel) key.channel();
        //ByteBuffer b = ByteBuffer.allocate(1024);
        //client.read(b);

    }

    private void handleAccept() throws IOException {
        SocketChannel client = serverChannel.accept();
        logger.info("Connection from " + client.toString() + " accepted.");
        client.configureBlocking(false);
        SelectionKey sk = client.register(selector, SelectionKey.OP_READ,SelectionKey.OP_WRITE);
        String response1 = "125 \r\n";
        String response2 = "230 \r\n";
        System.out.println(response1);
        //sk.attach(response1);
        ByteBuffer b = ByteBuffer.allocate(100);
        b.put(response1.getBytes());
        b.flip();
        client.write(b);
    }

    private void handleRead(SelectionKey key) throws IOException {
        System.out.println("read");
        SocketChannel client = (SocketChannel) key.channel();
        Path path = Paths.get(configuration.getOutputPath());
        FileChannel fileChannel = FileChannel.open(path,
                EnumSet.of(StandardOpenOption.CREATE,
                        StandardOpenOption.TRUNCATE_EXISTING,
                        StandardOpenOption.WRITE));
        ByteBuffer buffer = ByteBuffer.allocate(configuration.getBufferSize());
        FtpReader reader = new FtpReader();
        System.out.println(1);
        int numOfReadedBytes = reader.readFile(client,fileChannel,configuration.getBufferSize());
        System.out.println(2);
        ByteBuffer b = ByteBuffer.allocate(1024);
        //int numOfReadedBytes = client.read(b);
        String answer = new String(b.array(), StandardCharsets.UTF_8).trim();
        logger.info(numOfReadedBytes + " bytes were readed");
        logger.info(answer);
        String response1 = "230 \r\n";
        ByteBuffer b1 = ByteBuffer.allocate(100);
        b1.put(response1.getBytes());
        b1.flip();
        client.write(b1);
        fileChannel.close();

        client.close();
    }
}
