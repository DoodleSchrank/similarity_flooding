package org.sf_ref.rdf.model;

import org.sf_ref.rdf.util.SetOperations;

/**
 * An RDF model that natively supports set operations `union', `difference' and `intersection'.
 *
 * @see SetOperations
 */
public interface SetModel extends Model {

  /**
   * Set union with another model.
   * @return  <code>this</code>, i.e. the model itself
   */
  public SetModel unite(Model m) throws ModelException;

  /**
   * Set difference with another model.
   * @return  <code>this</code>, i.e. the model itself
   */
  public SetModel subtract(Model m) throws ModelException;

  /**
   * Set intersection with another model.
   * @return  <code>this</code>, i.e. the model itself
   */
  public SetModel intersect(Model m) throws ModelException;

}

