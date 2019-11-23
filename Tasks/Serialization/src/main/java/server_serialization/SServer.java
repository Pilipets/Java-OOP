package server_serialization;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Logger;

public class SServer implements AutoCloseable {
    private final int port;
    private final Logger log;
    private Socket clientSocket;
    private ObjectInputStream in;
    private ServerSocket server;

    public SServer() {
        in = null;
        port = 1111;
        log = Logger.getLogger(SServer.class.getName());

        try {
            server = new ServerSocket(port);
            log.info(String.format("Serialization Server was created on %d port",port));
        } catch (IOException e){
            server = null;
            log.info(String.format("Failed to create ServerSocket: %s",e));
        }
    }
    @Override
    public void close(){
        try {
            clientSocket.close();
            server.close();
            log.info("Server was successfully closed");
        }catch (IOException e){
            log.info(String.format("Error while closing the server:%s",e));
        }
    }

    public void checkConnection(){
        try {
            clientSocket = server.accept();
            log.info(String.format("Connection is accepted: %s", clientSocket));
        } catch (IOException e){
            log.info(String.format("Error in listening socket %s",e));
        }
    }

    public Object readData() {
        Object obj = null;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            obj = in.readObject();
            in.close();
        } catch (IOException | ClassNotFoundException e) {
            log.info(String.format("Error in reading data from client: %s",e));
        }
        return obj;
    }

    public int getRunningPort(){
        return server.getLocalPort();
    }
}
