package OOP.lab2.parsing;

public interface MyXMLParser<T> {
  T parseDocument(String xmlPath) throws IllegalArgumentException;
}
