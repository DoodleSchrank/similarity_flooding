/**
 * Copyright � World Wide Web Consortium, (Massachusetts Institute of
 * Technology, Institut National de Recherche en Informatique et en
 * Automatique, Keio University).
 *
 * All Rights Reserved.
 *
 * @author	Sergey Melnik <melnik@db.stanford.edu>
 */
package org.sf_ref.rdf.implementation.model;

import org.sf_ref.rdf.io.SerializableLiteral;
import org.sf_ref.rdf.model.Literal;
import org.sf_ref.rdf.model.ModelException;

import java.io.*;

public class LiteralImpl extends RDFNodeImpl implements Literal, Serializable {

  protected String content;

  public LiteralImpl(String str) {

    this.content = str;
  }

  public String getLabel() {

    return content;
  }

  public boolean equals (Object that) {
    
    if (this == that) {
      return true;
    }
    if (that == null || !(that instanceof Literal)) {
      return false;
    }
    
    try {
      return content.equals(((Literal)that).getLabel());
    } catch (ModelException any) {
    }

    return false;
  }

  Object writeReplace() throws ObjectStreamException {

    return new SerializableLiteral(this);
  }
}
