package OOP.lab2.parsing.SAXParser;

import OOP.lab2.parsing.builders.BasicParsingBuilder;
import org.xml.sax.Attributes;
import org.xml.sax.helpers.DefaultHandler;

class SaxHandler<T> extends DefaultHandler {

  private T parseResult;
  private BasicParsingBuilder<T> builder;

  public SaxHandler(BasicParsingBuilder<T> builder) {
    this.builder = builder;
  }

  public T getParseResult() {
    return parseResult;
  }

  @Override
  public void endDocument() {
    parseResult = builder.getParseResult();
  }

  @Override
  public void startElement(String uri, String localName, String qName, Attributes attributes) {
    builder.onTagStart(qName);
    for (int iter = 0; iter < attributes.getLength(); iter++) {
      builder.setAttribute(attributes.getQName(iter), attributes.getValue(iter));
    }
  }

  @Override
  public void endElement(String uri, String localName, String qName) {
    builder.onTagEnd(qName);
  }

  @Override
  public void characters(char[] ch, int start, int length) {
    String data = new String(ch, start, length);
    data = data.replace("\n", "").trim();
    builder.setTag(data);
  }
}
