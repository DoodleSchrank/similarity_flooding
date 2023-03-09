package org.sf_ref.db.rdf.model;

import org.sf_ref.db.rdf.util.LongIDAware;
import org.sf_ref.rdf.model.RDFNode;
import org.sf_ref.rdf.util.RDFUtil;
//import com.lastmileservices.rdf.util.*;


public abstract class RDFNodeImpl implements RDFNode, LongIDAware {

  //  protected int nodeID;
  //  private int _hashCode;

  /*
  protected RDFNodeImpl(int nodeID) {

    this.nodeID = nodeID;
  }
  */

  protected RDFNodeImpl() {
  }

  public String	toString () {
    return getLabel();
  }

  public abstract String getLabel();

  public long getLongID() {

    //    return nodeID;
    return super.hashCode(); // of Object
  }

  public int hashCode() {

    return RDFUtil.incrementalHashCode(getLabel());
  }

  /** That's the beauty of it... */
  public boolean equals(Object that) 
  {
    return this == that;
  }
  
}
