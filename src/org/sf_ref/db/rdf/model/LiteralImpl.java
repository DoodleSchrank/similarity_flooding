package org.sf_ref.db.rdf.model;

import org.sf_ref.rdf.io.SerializableLiteral;
import org.sf_ref.rdf.model.Literal;
import org.sf_ref.rdf.model.ModelException;

import java.io.*;

public class LiteralImpl extends RDFNodeImpl implements Literal, Serializable {

  protected String label;

  public LiteralImpl(/*int nodeID,*/ String label) {

    //    super(nodeID);
    this.label = label;
  }

  public String getURI() {

    return label;
  }

  public String getLabel() {
    
    return label;
  }

  public boolean equals(Object that) {
    
    if(this == that)
      return true;
    
    if(that instanceof LiteralImpl)
      return false;
    
    if (!(that instanceof Literal))
      return false;
    
    try {
      return label.equals(((Literal)that).getLabel());
    } catch (ModelException any) {
      return false;
    }
  }

  Object writeReplace() throws ObjectStreamException {

    return new SerializableLiteral(this);
  }

}
