package OOP.lab2.parsing;

import OOP.lab2.data_pattern.VehicleMarket;
import OOP.lab2.parsing.DOMParser.DomXMLParser;
import OOP.lab2.parsing.SAXParser.SaxXMLParser;
import OOP.lab2.parsing.StAXParser.StaxXMLParser;
import OOP.lab2.parsing.builders.BasicParsingBuilder;
import OOP.lab2.parsing.builders.MarketParsingBuilder;

public class MarketParser {
  private MyXMLParser<VehicleMarket> parser;

  public MarketParser(MyXMLParser<VehicleMarket> parser){
    this.parser = parser;
  }
  public MarketParser(String type){
    BasicParsingBuilder<VehicleMarket> builder = new MarketParsingBuilder();
    switch (type) {
      case "DOM":
        this.parser = new DomXMLParser<VehicleMarket>(builder);
        break;
      case "SAX":
        this.parser = new SaxXMLParser<VehicleMarket>(builder);
        break;
      case "STAX":
        this.parser = new StaxXMLParser<VehicleMarket>(builder);
        break;
      default:
        this.parser = null;
    }
  }

  public VehicleMarket parseSite(String xmlPath, String xsdPath) {
    if (xmlPath == null || xsdPath == null ||
            !XMLValidator.validateXMLSchema(xsdPath, xmlPath))
      return null;

    VehicleMarket site = null;
    try {
      site = parser.parseDocument(xmlPath);
    } catch (IllegalArgumentException e) {
      site = null;
      System.out.println("Error while parsing: " + e.getMessage());
    }
    return site;
  }
}
