package OOP.lab2.parsing.builders;

public interface BasicParsingBuilder<T> {
  void onTagStart(String tagName);
  void onTagEnd(String tagName);
  void setAttribute(String attributeName, String value);
  void setTag(String information);
  T getParseResult();
}
