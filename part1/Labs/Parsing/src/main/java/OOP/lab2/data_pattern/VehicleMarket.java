package OOP.lab2.data_pattern;

import java.io.FileWriter;
import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

public class VehicleMarket {
    private List<Vehicle> items;
    public VehicleMarket(){
        items = new ArrayList<>(0);
    }
    public void addVehicle(Vehicle v){
        items.add(v);
    }
    @Override
    public String toString() {
        return items.toString();
    }

    public void printToFile(String fileName){
        try(FileWriter writer = new FileWriter(fileName, true)) {
            writer.write(this.toString());
            writer.append('\n');
            writer.flush();
        } catch (IOException e) {
            //log.info(e.getMessage());
        }
    }

    public int getVehiclesAmount(){
        return items.size();
    }
    public Vehicle getVehicle(int i){
        return items.get(i);
    }
}
