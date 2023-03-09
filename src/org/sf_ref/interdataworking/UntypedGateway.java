package org.sf_ref.interdataworking;

import org.sf_ref.rdf.model.ModelException;

import java.util.*;

public interface UntypedGateway {

  /**
   * Transforms an ordered list of models into another list of models
   */
  public List execute(List input) throws ModelException;

  public int getMinInputLen();

  public int getMaxInputLen();

  public int getMinOutputLen();

  public int getMaxOutputLen();
}
