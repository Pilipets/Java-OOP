package OOP.lab2.testing;

import OOP.lab2.parsing.XMLValidator;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

public class XMLValidatorTest {

  @Test
  public void validateValidXML() {
    boolean result = XMLValidator.validateXMLSchema(
            "src/test/resources/market_pattern.xsd",
            "src/test/resources/market_data.xml");
    assertTrue(result);
  }

  @Test
  public void validateInvalidXML() {
    boolean result = XMLValidator.validateXMLSchema(
            "src/test/resources/market_pattern.xsd",
            "src/test/resources/invalid_data.xml");
    assertFalse(result);
  }
}
