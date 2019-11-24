package ftp_server;

import ftp_server.utils.FtpConfiguration;
import ftp_server.utils.FtpFileManager;

import java.io.IOException;
import java.nio.channels.*;
import java.util.Iterator;
import java.util.Set;
import java.util.logging.Logger;

public class FtpServer implements AutoCloseable{
    private final FtpConfiguration config;
    private ServerSocketChannel serverChannel;
    private Selector selector;
    private final Logger log;
    private final FtpFileManager manager;

    public FtpServer() throws IOException {
        log = Logger.getLogger(FtpServer.class.getName());
        config = new FtpConfiguration();
        //setUp config
        manager = new FtpFileManager(config);
        log.info("Server was created");
    }
    @Override
    public void close() throws IOException {
        manager.close();
        serverChannel.close();
        selector.close();
        log.info("Server was closed");
    }
    public void start() throws IOException {
        log.info("Starting the server");
        configureServer();
        acceptConnections();
    }

    private void configureServer() throws IOException {
        selector = Selector.open();
        serverChannel = ServerSocketChannel.open();
        serverChannel.configureBlocking(false);
        serverChannel.socket().bind(config.getInetAddress());
        serverChannel.register(selector, SelectionKey.OP_ACCEPT);
        log.info("Server was configured successfully");
    }

    private void acceptConnections() throws IOException {
        log.info("Listening for the connections");
        while (true) {
            selector.select();
            Set<SelectionKey> selectedKeys = selector.selectedKeys();
            Iterator<SelectionKey> it = selectedKeys.iterator();

            while (it.hasNext()) {
                SelectionKey key = it.next();

                if (key.isAcceptable()) {
                    handleAccept();
                } else if (key.isReadable()) {
                    handleRead(key);
                }
                it.remove();
            }
        }
    }

    private void handleAccept() throws IOException {
        SocketChannel client = serverChannel.accept();
        client.configureBlocking(false);
        client.register(selector, SelectionKey.OP_READ);
        log.info(String.format("New connection from %s accepted.\n",client));
    }

    private void handleRead(SelectionKey key) throws IOException {
        log.info(String.format("Reading data from the client\n",key));
        SocketChannel client = (SocketChannel) key.channel();
        manager.saveDataFrom(client);
        log.info(String.format("Finished reading from client\n",key));
        client.close();
    }
}
