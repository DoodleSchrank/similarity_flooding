/**
 * Copyright � World Wide Web Consortium, (Massachusetts Institute of
 * Technology, Institut National de Recherche en Informatique et en
 * Automatique, Keio University).
 *
 * All Rights Reserved.
 *
 * Please see the full Copyright clause at
 * <http://www.w3.org/Consortium/Legal/copyright-software.html>
 *
 */
package org.sf_ref.rdf.syntax;

import org.sf_ref.rdf.model.ModelException;
import org.sf_ref.rdf.model.NodeFactory;
import org.sf_ref.rdf.model.Statement;
import org.sf_ref.rdf.model.*;


/**
 * RDFParser passes triples to RDFConsumer
 *
 * @see RDFParser
 */

public interface RDFConsumer {

  /**
   * start is called when parsing of data is started
   */
  public void startModel () throws ModelException;
  
  /**
   * end is called when parsing of data is ended
   */
  public void endModel () throws ModelException;

  /**
   * node factory to be used by the parser for creating resources and literals passed to addStatement
   */
  public NodeFactory getNodeFactory() throws ModelException;
  
  /**
   * assert is called every time a new statement within
   * RDF data model is added
   */
  public void addStatement (Statement s) throws ModelException;
 
}

