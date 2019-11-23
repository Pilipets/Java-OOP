package OOP.lab2.parsing.builders;

import OOP.lab2.data_pattern.*;
import jdk.nashorn.internal.ir.annotations.Immutable;

import java.util.HashMap;
import java.util.List;
import java.util.ArrayList;

public class MarketParsingBuilder implements BasicParsingBuilder<VehicleMarket> {
  enum FieldTypes {
    title, type, price, //Vehicle
    foreign, used, age, mileage, use_trace, specific_detail, //VehicleDetails
    none
  };
  private VehicleMarket market;
  private Vehicle vehicle;
  private VehicleDetails vehicleDetails;
  private List<String> use_traces;
  private List<String> specific_details;

  private FieldTypes currentState;

  public MarketParsingBuilder() {
    currentState = FieldTypes.none;
  }
  @Override
  public void onTagStart(String tagName) {
    if(tagName.equalsIgnoreCase("vehicle")){
      vehicle = new Vehicle();
    } else if (tagName.equalsIgnoreCase("details")) {
      vehicleDetails = new VehicleDetails();
    } else if (tagName.equalsIgnoreCase("use_traces")) {
      use_traces = new ArrayList<>();
    } else if (tagName.equalsIgnoreCase("specific_details")) {
      specific_details = new ArrayList<>();
    } else if (tagName.equalsIgnoreCase("market")) {
      market = new VehicleMarket();
    } else{
      currentState = FieldTypes.valueOf(tagName);
    }
  }

  @Override
  public void onTagEnd(String tagName) {
    if (tagName.equalsIgnoreCase("vehicle")) {
      market.addVehicle(vehicle);
    } else if (tagName.equalsIgnoreCase("details")) {
      vehicle.setVehicleDetails(vehicleDetails);
    } else if (tagName.equalsIgnoreCase("use_traces")) {
      vehicleDetails.setUseTraces(use_traces);
    } else if (tagName.equalsIgnoreCase("specific_details")) {
      vehicleDetails.setSpecificDetails(specific_details);
    }
  }

  @Override
  public void setAttribute(String attributeName, String value) {
    if (attributeName.equals("id"))
      vehicle.id = Integer.parseInt(value);
  }

  @Override
  public void setTag(String value) {
    switch (currentState) {
      case title:
        vehicle.title = value;
        break;
      case type:
        vehicle.type = Vehicle.VehicleType.valueOf(value);
        break;
      case price:
        vehicle.price = Integer.parseInt(value);
        break;
      case foreign:
        vehicleDetails.foreign = Boolean.parseBoolean(value);
        break;
      case used:
        vehicleDetails.used = Boolean.parseBoolean(value);
        break;
      case age:
        vehicleDetails.age = Integer.parseInt(value);
        break;
      case mileage:
        vehicleDetails.mileage = Integer.parseInt(value);
        break;
      case use_trace:
        use_traces.add(value);
        break;
      case specific_detail:
        specific_details.add(value);
        break;
    }
    currentState = FieldTypes.none;
  }

  @Override
  public VehicleMarket getParseResult() {
    return market;
  }
}
