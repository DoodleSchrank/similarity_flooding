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

import org.sf_ref.rdf.model.ModelException;
import org.sf_ref.rdf.model.RDFNode;
import org.sf_ref.rdf.util.RDFUtil;
import org.sf_ref.rdf.model.*;
import org.sf_ref.rdf.util.*;

public abstract class RDFNodeImpl implements RDFNode {

//   int hash;

  public String	toString () {
    return getLabel();
  }

  public abstract String getLabel ();

  public int hashCode() {

    return RDFUtil.incrementalHashCode(getLabel());

//     if(hash == 0) {
//       hash = getLabel().hashCode();
//     }
//     return hash;
  }

  public boolean equals (Object that) {

    try {
      return getLabel().equals( ((RDFNode)that).getLabel() );
    } catch (ModelException ex) {
      return false;
    }
  }
}
