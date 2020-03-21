package server_serialization;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SServer implements AutoCloseable {
    private final int port;
    private final static Logger logger = Logger.getLogger(SServer.class.getName());
    private Socket clientSocket;
    private ObjectInputStream in;
    private ServerSocket server;

    public SServer(int port) {
        in = null;
        this.port = port;

        try {
            server = new ServerSocket(port);
            logger.info(String.format("Serialization Server was created on %d port",port));
        } catch (IOException e){
            server = null;
            logger.log(Level.SEVERE, String.format("Failed to create ServerSocket: %s",e));
        }
    }
    @Override
    public void close(){
        boolean thrownException = false;
        try {
            clientSocket.close();
        }catch (IOException e){
            thrownException = true;
            logger.log(Level.SEVERE, String.format("Error while closing the client socket: %s",e));
        }
        try {
            server.close();
        } catch (IOException e){
            thrownException = true;
            logger.log(Level.SEVERE, String.format("Error while closing the client socket: %s",e));
        }

        if(!thrownException)
            logger.info("Server was successfully closed");
    }

    public void checkConnection(){
        try {
            clientSocket = server.accept();
            logger.info(String.format("Connection is accepted: %s", clientSocket));
        } catch (IOException e){
            logger.log(Level.SEVERE, String.format("Error in listening socket %s",e));
        }
    }

    public Object readData() {
        Object obj = null;
        try {
            in = new ObjectInputStream(clientSocket.getInputStream());
            obj = in.readObject();
        } catch (IOException | ClassNotFoundException e) {
            logger.log(Level.SEVERE, String.format("Error in reading data from client: %s",e));
        }
        finally {
            try{
                in.close();
            }
            catch (IOException e){
                logger.log(Level.SEVERE, String.format("Error in reading data from client: %s",e));
            }
        }
        return obj;
    }

    public int getRunningPort(){
        return server.getLocalPort();
    }
}
