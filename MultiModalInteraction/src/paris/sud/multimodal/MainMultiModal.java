package paris.sud.multimodal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.hp.hpl.jena.query.Query;
import com.hp.hpl.jena.query.QueryExecution;
import com.hp.hpl.jena.query.QueryExecutionFactory;
import com.hp.hpl.jena.query.QueryFactory;
import com.hp.hpl.jena.query.QuerySolution;
import com.hp.hpl.jena.query.ResultSet;
import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;
import com.hp.hpl.jena.rdf.model.Property;
import com.hp.hpl.jena.rdf.model.RDFNode;
import com.hp.hpl.jena.rdf.model.Resource;
import com.hp.hpl.jena.rdf.model.Statement;
import com.hp.hpl.jena.rdf.model.StmtIterator;

public class MainMultiModal {
	public static void main(String[] args){
		//dirty log init
		org.apache.log4j.BasicConfigurator.configure();
		
		Model model = ModelFactory.createDefaultModel();
		File f = new File("rdf-model.xml");
		FileReader fr;
		try {
			fr = new FileReader(f);
			model.read(fr, "http://imi.org/");
		} catch (FileNotFoundException e) {
			System.out.println("IO ERROR: "+f.getAbsolutePath());
			System.exit(1);
		}
	    
	 // list the statements in the Model
	    StmtIterator iter = model.listStatements();

	    // print out the predicate, subject and object of each statement
	    while (iter.hasNext()) {
	        Statement stmt      = iter.nextStatement();  // get next statement
	        Resource  subject   = stmt.getSubject();     // get the subject
	        Property  predicate = stmt.getPredicate();   // get the predicate
	        RDFNode   object    = stmt.getObject();      // get the object

	        System.out.print(subject.toString());
	        System.out.print(" " + predicate.toString() + " ");
	        if (object instanceof Resource) {
	           System.out.print(object.toString());
	        } else {
	            // object is a literal
	            System.out.print(" \"" + object.toString() + "\"");
	        }

	        System.out.println(" .");
	    } 
	    
		Query query = QueryFactory.create("PREFIX rdf: <http://www.w3.org/1999/02/22-rdf-syntax-ns#> " +
				"PREFIX mm: <http://imi.org/> " +
				"SELECT * WHERE { ?room rdf:type mm:Room }");
		QueryExecution qe = QueryExecutionFactory.create(query, model);
	    ResultSet rs = qe.execSelect();

	    System.out.println("solutions:");
	    while(rs.hasNext())
	    {
	        QuerySolution sol = rs.nextSolution();
	        System.out.println(sol);
	    }

	    qe.close();
	    	
	    
	}
}
