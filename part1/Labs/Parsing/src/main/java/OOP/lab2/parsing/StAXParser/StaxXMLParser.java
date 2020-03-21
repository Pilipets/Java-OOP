package OOP.lab2.parsing.StAXParser;

import OOP.lab2.parsing.builders.BasicParsingBuilder;
import OOP.lab2.parsing.MyXMLParser;

import javax.xml.stream.XMLInputFactory;
import javax.xml.stream.XMLStreamException;
import javax.xml.stream.XMLStreamReader;
import java.io.FileInputStream;
import java.io.FileNotFoundException;

import static javax.xml.stream.XMLStreamConstants.*;

public class StaxXMLParser<T> implements MyXMLParser<T> {
  private final BasicParsingBuilder<T> builder;
  public StaxXMLParser(BasicParsingBuilder<T> builder) {
    this.builder = builder;
  }

  @Override
  public T parseDocument(String xmlPath) throws IllegalArgumentException {
    XMLInputFactory xmlInputFactory = XMLInputFactory.newInstance();
    XMLStreamReader xmlStreamReader;

    try {
      xmlStreamReader = xmlInputFactory.createXMLStreamReader(new FileInputStream(xmlPath));
    } catch (FileNotFoundException | XMLStreamException e) {
      //log.info(e.getStackTrace().toString());
      throw new IllegalArgumentException("Error " + e.getMessage());
    }

    try {
      while (xmlStreamReader.hasNext()) {
        int event = xmlStreamReader.next();

        if (event == END_DOCUMENT) {
          break;
        } else if (event == START_ELEMENT) {
          builder.onTagStart(xmlStreamReader.getLocalName());

          for (int i = 0; i < xmlStreamReader.getAttributeCount(); i++) {
            String value = xmlStreamReader.getAttributeValue(i);
            String field = xmlStreamReader.getAttributeName(i).toString();
            builder.setAttribute(field, value);
          }
        } else if (event == CHARACTERS) {
          String information = xmlStreamReader.getText();
          information = information.replace("\n", "").trim();
          builder.setTag(information);
        } else if (event == END_ELEMENT) {
          builder.onTagEnd(xmlStreamReader.getLocalName());
        }
      }
    } catch (XMLStreamException e) {
      //log.info(e.getStackTrace().toString());
      throw new IllegalArgumentException("Error " + e.getMessage());
    }
    return builder.getParseResult();
  }
}
