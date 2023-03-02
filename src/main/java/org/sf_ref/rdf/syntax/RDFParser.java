package org.sf_ref.rdf.syntax;

import org.xml.sax.InputSource;
import org.xml.sax.ErrorHandler;
import org.xml.sax.SAXException;

/**
 * RDF parser interface
 */

public interface RDFParser {

  /**
   * Parse from the given SAX/XML input source.
   */
  public void parse(InputSource source, RDFConsumer consumer) throws SAXException;

  public void setErrorHandler (ErrorHandler handler);

}

