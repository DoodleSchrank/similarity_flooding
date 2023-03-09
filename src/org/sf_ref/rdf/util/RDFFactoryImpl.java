/**
 * Copyright � Sergey Melnik (Stanford University, Database Group)
 *
 * All Rights Reserved.
 */

package org.sf_ref.rdf.util;

import org.sf_ref.rdf.implementation.syntax.sirpac.SiRPAC;
import org.sf_ref.rdf.implementation.syntax.sirpac.SiRS;
import org.sf_ref.rdf.model.Model;
import org.sf_ref.rdf.model.NodeFactory;
import org.sf_ref.rdf.syntax.RDFParser;
import org.sf_ref.rdf.syntax.RDFSerializer;
import org.sf_ref.db.rdf.model.ModelImpl;
//import org.sf_ref.rdf.implementation.model.*;

//import org.sf_ref.rdf.implementation.syntax.strawman.*;


/**
 * A default implementation of the RDFFactory interface.
 *
 * @author Sergey Melnik <melnik@db.stanford.edu>
 */

public class RDFFactoryImpl implements RDFFactory {

    // use strawman parser
  //    boolean strawman = false;

  //  static SchemaRegistry registry;

  /*
  public SchemaRegistry registry() {
    if(registry == null)
      registry = new SchemaRegistryImpl( this );
    return registry;
  }
  */

    public RDFFactoryImpl() {
    }

//     public RDFFactoryImpl(boolean strawman) {
// 	this.strawman = strawman;
//     }

  public RDFParser createParser() {
    
//       if(strawman)
// 	  return new StrawmanParser();
//       else {
	  SiRPAC parser = new SiRPAC();
	  parser.setRobustMode(true);
	  parser.ignoreExternalEntities(true);
	  return parser;
	  //      }
  }

  public RDFSerializer createSerializer() {

//     Map ns = new HashMap();
//     ns.put("http://www.w3.org/1999/02/22-rdf-syntax-ns#", "MYRDF");
//     return new SiRS(ns);
    return new SiRS();
  }

  public Model createModel() {

    return new ModelImpl();
  }

  public NodeFactory getNodeFactory() {

    return new ModelImpl().getNodeFactory();
  }

  /*
  public SchemaModel createSchemaModel(Model m) {

    if(m instanceof SchemaModel)
      return (SchemaModel)m;
    return new SchemaModelImpl( (SchemaRegistryImpl)registry(), m );
  }
  */  
}

