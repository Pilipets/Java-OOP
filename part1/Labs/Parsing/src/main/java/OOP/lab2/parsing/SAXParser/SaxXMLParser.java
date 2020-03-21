package OOP.lab2.parsing.SAXParser;

import OOP.lab2.parsing.builders.BasicParsingBuilder;
import OOP.lab2.parsing.MyXMLParser;
import org.xml.sax.SAXException;

import javax.xml.parsers.ParserConfigurationException;
import javax.xml.parsers.SAXParser;
import javax.xml.parsers.SAXParserFactory;
import java.io.File;
import java.io.IOException;

public class SaxXMLParser<T> implements MyXMLParser<T> {
  private final BasicParsingBuilder<T> builder;
  private final SaxHandler saxHandler;
  private SAXParser saxParser;

  public SaxXMLParser(BasicParsingBuilder<T> builder) {
    this.builder = builder;
    saxHandler = new SaxHandler(builder);

    try {
      saxParser = SAXParserFactory.newInstance().newSAXParser();
    } catch (SAXException | ParserConfigurationException e) {
    }
  }
  @Override
  public T parseDocument(String xmlPath) throws IllegalArgumentException {
    T parseResult;
    try {
      saxParser.parse(new File(xmlPath), saxHandler);
      parseResult = builder.getParseResult();
    } catch (SAXException | IOException e) {
      throw new IllegalArgumentException("Error: " + e.getMessage());
    }
    return parseResult;
  }
}
