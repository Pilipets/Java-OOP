package server_serialization;

import data.Vehicle;

import java.io.IOException;

public class ServerMain {
    public static void main(String[]args) {
        SServer s = new SServer(1111);
        s.checkConnection();
        Vehicle v = (Vehicle)s.readData();
        System.out.println(v);
    }
}
