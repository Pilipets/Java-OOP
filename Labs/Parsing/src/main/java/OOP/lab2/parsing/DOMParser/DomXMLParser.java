package OOP.lab2.parsing.DOMParser;

import OOP.lab2.parsing.builders.BasicParsingBuilder;
import OOP.lab2.parsing.MyXMLParser;

import org.w3c.dom.Document;
import org.w3c.dom.NamedNodeMap;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;

import javax.xml.parsers.DocumentBuilder;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import java.io.File;
import java.io.IOException;

public class DomXMLParser<T> implements MyXMLParser<T> {

  private BasicParsingBuilder<T> builder;
  private final DocumentBuilder docBuilder;
  public DomXMLParser(BasicParsingBuilder<T> builder) {
    this.builder = builder;
    DocumentBuilderFactory dbFactory = DocumentBuilderFactory.newInstance();
    DocumentBuilder tempBuilder = null;
    try {
      tempBuilder = dbFactory.newDocumentBuilder();
    } catch (ParserConfigurationException e){
    }
    docBuilder = tempBuilder;
  }

  @Override
  public T parseDocument(String xmlPath) throws IllegalArgumentException {
    File xmlFile = new File(xmlPath);

    try {
      Document document = docBuilder.parse(xmlFile);
      getInfoAboutAllNodes(document.getChildNodes());
    } catch (SAXException | IOException e) {
      //e.printStackTrace();
      throw new IllegalArgumentException("Error while reading file: " + e.getMessage());
    }

    return builder.getParseResult();
  }

  private void getInfoAboutAllNodes(NodeList list) {
    for (int i = 0; i < list.getLength(); i++) {
      Node node = list.item(i);

      if (node.getNodeType() == Node.TEXT_NODE) {
        String textInformation = node.getNodeValue().replace("\n", "").trim();

        if (!textInformation.isEmpty()) {
          builder.onTagStart(node.getParentNode().getNodeName());
          builder.setTag(list.item(i).getNodeValue());
        }
      } else { //Node.ELEMENT_NODE
        NamedNodeMap attributes = node.getAttributes();
        builder.onTagStart(node.getNodeName());

        for (int k = 0; k < attributes.getLength(); k++) {
          Node curAttribute = attributes.item(k);
          builder.setAttribute(curAttribute.getNodeName(), curAttribute.getTextContent());
        }

        if (node.hasChildNodes()) getInfoAboutAllNodes(node.getChildNodes());
        builder.onTagEnd(node.getNodeName());
      }
    }
  }
}
