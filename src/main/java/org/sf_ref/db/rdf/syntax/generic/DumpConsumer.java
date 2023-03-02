package org.sf_ref.db.rdf.syntax.generic;

import java.util.*;
import java.io.*;

import org.sf_ref.rdf.implementation.model.NodeFactoryImpl;
import org.sf_ref.rdf.model.Model;
import org.sf_ref.rdf.model.ModelException;
import org.sf_ref.rdf.model.NodeFactory;
import org.sf_ref.rdf.model.Statement;
import org.sf_ref.rdf.syntax.RDFConsumer;
import org.sf_ref.rdf.syntax.RDFSerializer;
import org.sf_ref.rdf.syntax.SerializationException;
import org.sf_ref.rdf.model.*;
import org.sf_ref.rdf.syntax.*;

public class DumpConsumer implements RDFConsumer, RDFSerializer {
      
  int num = 0;
  NodeFactory f = new NodeFactoryImpl();

  public void startModel () {}
  public void endModel () {
    System.out.println("Total statements: " + num);
  }

  public NodeFactory getNodeFactory() {
    return f;
  }

  public void addStatement (Statement s) {
    System.out.println(s.toString());
    num++;
  }

  public void serialize(Model model, Writer w) throws SerializationException, IOException, ModelException {

    for(Enumeration en = model.elements(); en.hasMoreElements();) {

      Statement st = (Statement)en.nextElement();
      w.write(st.toString());
      w.write('\n');
    }
    w.flush();
  }

}

