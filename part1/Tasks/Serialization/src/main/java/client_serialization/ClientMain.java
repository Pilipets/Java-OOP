package client_serialization;

import data.Vehicle;


import java.io.IOException;

public class ClientMain {
    public static void main(String[]args) {
        SClient client = new SClient("127.0.0.1",1111);
        client.connect();
        Vehicle v = new Vehicle(54643,"Pagani Zonda",Vehicle.VehicleType.SportCar,2.55e6);
        client.sendData(v);
    }
}
