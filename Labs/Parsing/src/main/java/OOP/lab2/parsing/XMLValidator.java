package OOP.lab2.parsing;

import org.xml.sax.SAXException;

import javax.security.sasl.SaslException;
import javax.xml.XMLConstants;
import javax.xml.transform.stream.StreamSource;
import javax.xml.validation.Schema;
import javax.xml.validation.SchemaFactory;
import javax.xml.validation.Validator;
import java.io.File;
import java.io.IOException;
import java.util.logging.Logger;

public class XMLValidator {
  private static Logger log = Logger.getLogger(XMLValidator.class.getName());
  private static final SchemaFactory schemaFactory = SchemaFactory.newInstance(XMLConstants.W3C_XML_SCHEMA_NS_URI);
  public static boolean validateXMLSchema(String xsdPath, String xmlPath) {
    try {
      Schema schema = schemaFactory.newSchema(new File(xsdPath));
      Validator validator = schema.newValidator();
      validator.validate(new StreamSource(new File(xmlPath)));
    }
    catch (IOException e) {
      log.info(e.getStackTrace().toString());
      log.info("Error opening file: " + e.getMessage());
      return false;
    } catch (SAXException e) {
      log.info(e.getStackTrace().toString());
      log.info("Can't parse XML file properly: " + e.getMessage());
      return false;
    }
    return true;
  }
}
