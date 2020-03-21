package OOP.lab2.data_pattern;

import java.util.Objects;

public class Vehicle {
    public enum  VehicleType {
        SUV, Truck, SportCar, MotorBike, Bicycle, ElectricCar, None
    };
    public int id;
    public String title;
    public VehicleType type;
    public int price;
    private VehicleDetails details;
    public void setVehicleDetails(VehicleDetails details){
        this.details = details;
    }
    @Override
    public String toString() {
        return String.join("",
                String.format("\nVehicle{\n  id=\'%d\', title = %s,",id,title),
                String.format("\n  type=%s, price=\'%d\'",type,price),
                String.format("\n  vehicle_details=%s}", details)
        );
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof Vehicle)) return false;
        Vehicle v = (Vehicle) o;
        return id == v.id
                && Objects.equals(title, v.title)
                && type == v.type
                && price == v.price
                && Objects.equals(details, v.details);
    }

}
