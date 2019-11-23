package OOP.lab2.data_pattern;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class VehicleDetails {
    public boolean foreign;
    public boolean used;
    public int age;
    public int mileage;
    private List<String> use_traces;
    private List<String> specific_details;
    public VehicleDetails(){
        use_traces = new ArrayList<>(0);
        specific_details = new ArrayList<>(0);
    }
    public void setUseTraces(List<String> use_traces){
        this.use_traces = use_traces;
    }
    public void setSpecificDetails(List<String> specific_details){
        this.specific_details = specific_details;
    }

    @Override
    public String toString() {
        return String.join("",
                String.format("VehicleDetails{\n    foreign=\'%b\', used=\'%b\',", foreign, used),
                String.format("\n    age=\'%d\', mileage=\'%d\',", age, mileage),
                String.format("\n    use_traces=%s,", use_traces.toString()),
                String.format("\n    specific_details=%s}", specific_details.toString()));
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (!(o instanceof VehicleDetails)) return false;
        VehicleDetails other = (VehicleDetails) o;
        return foreign == other.foreign
                && used == other.used
                && age == other.age
                && mileage == other.mileage
                && Objects.equals(use_traces, other.use_traces)
                && Objects.equals(specific_details, other.specific_details);
    }
}
