package client_serialization;

import data.Vehicle;

import java.io.IOException;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.util.logging.Logger;

public class SClient implements AutoCloseable {
    private Socket serverSocket = null;
    private ObjectOutputStream out = null;
    private final String ip;
    private final int port;
    private final Logger log;


    public SClient(String ipAddress, int port){
        this.ip = ipAddress;
        this.port = port;
        log = Logger.getLogger(SClient.class.getName());
    }

    @Override
    public void close(){
        log.info("Closing the client");
        try {
            serverSocket.close();
        }catch (IOException e){
            log.info(String.format("Error while closing the client:%s",e));
        }
    }
    public void connect() {
        try {
            serverSocket = new Socket(ip, port);
            out = new ObjectOutputStream(serverSocket.getOutputStream());
            log.info(String.format("Connected to the %s",serverSocket.getInetAddress().getHostAddress()));
        } catch (IOException e){
            log.info(String.format("Error while connecting to the server: %s",e));
        }
    }

    public void sendData(Vehicle v) {
        try {
            out.writeObject(v);
            out.flush();
            out.close();
        } catch (IOException e){
            log.info(String.format("Error while sending data to the server: %s",e));
        }
    }


    public int getRunningPort() {
        return port;
    }

    public String getHostAddress() {
        return ip;
    }
}
