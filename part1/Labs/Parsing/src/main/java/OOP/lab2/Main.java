package OOP.lab2;

import OOP.lab2.data_pattern.VehicleMarket;
import OOP.lab2.parsing.MarketParser;

public class Main {

  public static void main(String[] args) {
    String filePath = "src/main/resources/market_data.xml";
    String xsdPath = "src/main/resources/market_pattern.xsd";

    MarketParser myParser = new MarketParser("DOM");
    VehicleMarket market = myParser.parseSite(filePath, xsdPath);
    market.printToFile("Output/DOM_result.txt");

    myParser = new MarketParser("STAX");
    market = myParser.parseSite(filePath, xsdPath);
    market.printToFile("Output/STAX_result.txt");

    myParser = new MarketParser("SAX");
    market = myParser.parseSite(filePath, xsdPath);
    market.printToFile("Output/SAX_result.txt");
  }
}
