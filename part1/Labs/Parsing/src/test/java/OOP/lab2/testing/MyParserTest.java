package OOP.lab2.testing;

import OOP.lab2.data_pattern.Vehicle;
import OOP.lab2.data_pattern.VehicleDetails;
import OOP.lab2.data_pattern.VehicleMarket;
import OOP.lab2.parsing.DOMParser.DomXMLParser;
import OOP.lab2.parsing.MyXMLParser;
import OOP.lab2.parsing.SAXParser.SaxXMLParser;
import OOP.lab2.parsing.StAXParser.StaxXMLParser;
import OOP.lab2.parsing.builders.MarketParsingBuilder;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Stream;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class MyParserTest {
    private Vehicle v1,v2,v3;
    private static Stream<MyXMLParser> xmlParseProvider() {
        return Stream.of(
                new DomXMLParser(new MarketParsingBuilder()),
                new SaxXMLParser(new MarketParsingBuilder()),
                new StaxXMLParser(new MarketParsingBuilder()));
    }
    public MyParserTest(){
        this.Init();
    }

    @ParameterizedTest
    @MethodSource("xmlParseProvider")
    public void parsePage(MyXMLParser parser) {
        VehicleMarket market = (VehicleMarket) parser.parseDocument("src/test/resources/market_data.xml");
        assertEquals(market.getVehiclesAmount(), 3);
        assertEquals(v1, market.getVehicle(0));
        assertEquals(v2, market.getVehicle(1));
        assertEquals(v3, market.getVehicle(2));
    }

    private void Init(){
        v1 = new Vehicle();
        v2 = new Vehicle();
        v3 = new Vehicle();

        VehicleDetails details;
        List<String> specific_details;
        List<String> use_traces;

        details = new VehicleDetails();
        specific_details = new ArrayList<>(0);

        v1.title = "Vehicle1";
        v1.type = Vehicle.VehicleType.SportCar;
        v1.price = 1200000;
        v1.id = 0;

        details.age = 4;
        details.used = false;
        details.foreign = true;
        details.mileage = 0;

        specific_details.add("max_speed=250km/h");
        specific_details.add("looks awesome");
        specific_details.add("buy in credit");

        details.setSpecificDetails(specific_details);
        v1.setVehicleDetails(details);

        details = new VehicleDetails();
        use_traces = new ArrayList<>(0);
        specific_details = new ArrayList<>(0);

        v2.title = "Vehicle2";
        v2.type = Vehicle.VehicleType.Bicycle;
        v2.price = 100;
        v2.id = 1;

        details.used = true;
        details.foreign = false;
        details.age = 2;

        use_traces.add("wheels are flatten");
        use_traces.add("seat clamp is broken");

        specific_details.add("21 gears");
        specific_details.add("16 inch wheel diameter");
        specific_details.add("special offer");

        details.setSpecificDetails(specific_details);
        details.setUseTraces(use_traces);

        v2.setVehicleDetails(details);

        details = new VehicleDetails();
        use_traces = new ArrayList<>(0);

        v3.title = "Vehicle3";
        v3.type = Vehicle.VehicleType.SUV;
        v3.price = 36000;
        v3.id = 2;

        details.used = true;
        details.foreign = false;
        details.age = 7;
        details.mileage = 90000;

        use_traces.add("average traces of use");
        use_traces.add("scratches near front driver door");
        use_traces.add("rust near back wheels");
        details.setUseTraces(use_traces);

        v3.setVehicleDetails(details);
    }
}
