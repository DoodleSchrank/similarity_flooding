package org.sf_ref.db.rdf.syntax;

import org.sf_ref.db.rdf.syntax.generic.TripleParser;
import org.sf_ref.db.rdf.syntax.generic.TripleSerializer;
import org.sf_ref.rdf.implementation.syntax.sirpac.SiRPAC;
import org.sf_ref.rdf.implementation.syntax.sirpac.SiRS;
import org.sf_ref.rdf.syntax.RDFParser;
import org.sf_ref.rdf.syntax.RDFSerializer;
import org.sf_ref.rdf.syntax.RDFSyntaxFactory;

public class RDFSyntaxFactoryImpl implements RDFSyntaxFactory {

  public static final String TripleXML_20010107 = "TripleXML_20010107";

  /**
   * Creates a new RDF parser. Returns null if could not create one.
   */
  public RDFParser createParser(String formatID) {

    if(RDF_XML_19990222.equals(formatID))
      return new SiRPAC();
    if(TripleXML_20010107.equals(formatID))
      return new TripleParser();

    return null;
  }

  /**
   * Creates a new RDF serializer
   */
  public RDFSerializer createSerializer(String formatID) {

    if(RDF_XML_19990222.equals(formatID))
      return new SiRS();
    if(TripleXML_20010107.equals(formatID))
      return new TripleSerializer();

    return null;
  }

}
