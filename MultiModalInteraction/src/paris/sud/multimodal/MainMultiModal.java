package paris.sud.multimodal;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;

import com.hp.hpl.jena.rdf.model.Model;
import com.hp.hpl.jena.rdf.model.ModelFactory;

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
		
	}
}
