package data;

import java.io.Serializable;

public class Vehicle implements Serializable {
    public enum  VehicleType {
        SUV, Truck, SportCar, MotorBike, Bicycle, ElectricCar, None
    };
    public int id;
    public String title;
    public VehicleType type;
    public double price;

    public Vehicle(int id, String title, VehicleType type, double price){
        this.id = id;
        this.title = title;
        this.type = type;
        this.price = price;
    }
    @Override
    public String toString() {
        return String.join("",
                String.format("\nVehicle{\n  id=\'%d\', title = %s,",id,title),
                String.format("\n  type=%s, price=\'%f\'",type,price)
        );
    }
}