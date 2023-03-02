package org.sf_ref.rdf.syntax;

import org.sf_ref.rdf.model.Model;
import org.sf_ref.rdf.model.ModelException;
import org.sf_ref.rdf.model.*;
import java.io.Writer;
import java.io.IOException;

/**
 * RDF serializer interface.
 */

public interface RDFSerializer {

  /**
   * Serialize a given model into an XML character output stream.
   */
  public void serialize(Model model, Writer w) throws SerializationException, IOException, ModelException;

}

