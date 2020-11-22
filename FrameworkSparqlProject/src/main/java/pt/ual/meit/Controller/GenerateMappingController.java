package pt.ual.meit.Controller;

import java.io.BufferedOutputStream;
import java.io.DataOutputStream;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletResponse;

import org.apache.jena.graph.Graph;
import org.apache.jena.query.Dataset;
import org.apache.jena.query.DatasetFactory;
import org.apache.jena.query.Query;
import org.apache.jena.query.QueryExecution;
import org.apache.jena.query.QueryExecutionFactory;
import org.apache.jena.query.QueryFactory;
import org.apache.jena.query.Syntax;
import org.apache.jena.rdf.model.Model;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.riot.Lang;
import org.apache.jena.riot.RDFDataMgr;
import org.apache.jena.sparql.graph.GraphFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.mvc.method.annotation.StreamingResponseBody;

import pt.ual.meit.JPA.ConfigOntology;
import pt.ual.meit.JPA.Mapeamento;
import pt.ual.meit.Mappings.ModuleCMS;
import pt.ual.meit.Mappings.ModuleCRM;
import pt.ual.meit.Model.FinalConfig;
import pt.ual.meit.Service.ConfigService;
import pt.ual.meit.Service.MappingService;
import pt.ual.meit.utils.Constants;

@Controller
@RequestMapping(value = "/generateMapping")
public class GenerateMappingController {

	@Autowired
	MappingService mappingService;

	@Autowired
	ConfigService configService;
	
	@Autowired
	ModuleCRM moduleCRM;
	
	@Autowired
	ModuleCMS moduleCMS;
	
	@GetMapping
	public String homePage() {
		return "generateMappings";
	}

	@RequestMapping(value = "/saveFinalConfig", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public StreamingResponseBody saveFinalConfig(HttpServletResponse response,
			@RequestParam(value = "file", required = false) MultipartFile dataFile,
			@RequestPart("options") FinalConfig finalConfig) {
		InputStream inputData;
		Model model = ModelFactory.createDefaultModel();
		Lang lang = null;
		String ext = null;

		switch (finalConfig.getLanguageOutput()) {
		case "RDF/XML":
			lang = Lang.RDFXML;
			ext = ".rdf";
			break;
		case "TURTLE":
			lang = Lang.TURTLE;
			ext = ".ttl";
			break;
		case "N3":
			lang = Lang.N3;
			ext = ".n3";
			break;
		case "N-TRIPLES":
			lang = Lang.NTRIPLES;
			ext = ".nt";
			break;
		}

		try {
			inputData = dataFile.getInputStream();
			model.read(inputData, null, finalConfig.getLanguageInput());

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		Dataset dataset = DatasetFactory.create(model);
		Graph results = GraphFactory.createDefaultGraph();
		List<Mapeamento> mappingList = mappingService.findAll();
		Map<String, String> prefixes;
		List<Model> resultsList = new ArrayList<>();
		Model resultModel = ModelFactory.createDefaultModel();
		Model resultprefix = ModelFactory.createDefaultModel();
		Map<String, String> has;
		Iterator<Map.Entry<String, String>> iterator;
		Map<String, String> finalHas = new HashMap<String, String>();
		for (Mapeamento m : mappingList) {
			Query query = QueryFactory.create(m.getMapSPARQL(), Syntax.syntaxARQ);
			QueryExecution qexec = QueryExecutionFactory.create(query, dataset);
			resultModel.add(qexec.execConstruct());
			has = resultModel.getNsPrefixMap();
			iterator = has.entrySet().iterator();
			while (iterator.hasNext()) {
				Map.Entry<String, String> entry = iterator.next();
				if (finalHas.isEmpty() || !finalHas.containsKey(entry.getKey())) {
					finalHas.put(entry.getKey(), entry.getValue());
					resultprefix.setNsPrefixes(resultModel.getNsPrefixMap());
				}
			}
		}

		Model resultFinal = ModelFactory.createDefaultModel();

		resultFinal.add(resultprefix);
		resultFinal.add(resultModel);

		OutputStream output;
		ConfigOntology configS = configService.findByTOntology("S");
		ConfigOntology configT = configService.findByTOntology("T");
		String filename = Constants.nameFile("Data_", configS.getName(), configT.getName(), ext);
		try {
			output = new FileOutputStream(filename);
			RDFDataMgr.write(output, resultFinal, lang);
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("filename", filename);

		InputStream input;
		try {
			input = new FileInputStream(filename);
			return outputStream -> {
				int nRead;
				byte[] data = new byte[1024];
				while ((nRead = input.read(data, 0, data.length)) != -1) {
					outputStream.write(data, 0, nRead);
				}
				;
				input.close();
			};
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		return null;

	}

	@RequestMapping(value = "/exportMapRules", method = RequestMethod.GET)
	public StreamingResponseBody exportListMapRules(HttpServletResponse response) throws FileNotFoundException {
		List<String> listMapRules = moduleCRM.exportMappingRules();
		ConfigOntology configS = configService.findByTOntology("S");
		ConfigOntology configT = configService.findByTOntology("T");
		
		String filename = Constants.nameFile("MapRules", configS.getName(), configT.getName(), ".txt");
		
		FileOutputStream fos = new FileOutputStream(filename);
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(fos));

		try {
			if (!listMapRules.isEmpty()) {
				for (String line : listMapRules) {
					System.out.print(line);
					output.write(line.getBytes());
				}
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("filename", filename);

		FileInputStream input;
		try {
			input = new FileInputStream(filename);
			return outputStream -> {
				int nRead;
				byte[] data = new byte[1024];
				while ((nRead = input.read(data, 0, data.length)) != -1) {
					outputStream.write(data, 0, nRead);
				}
				;
				input.close();
			};
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/exportMapSPARQL", method = RequestMethod.GET)
	public StreamingResponseBody exportListMapSPARQL(HttpServletResponse response) throws FileNotFoundException {
		List<String> listMapRules = moduleCMS.exportMappingSPARQL();
		ConfigOntology configS = configService.findByTOntology("S");
		ConfigOntology configT = configService.findByTOntology("T");
		
		String filename = Constants.nameFile("MapSPARQL", configS.getName(), configT.getName(), ".txt");
		
		FileOutputStream fos = new FileOutputStream(filename);
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(fos));

		try {
			if (!listMapRules.isEmpty()) {
				for (String line : listMapRules) {
					System.out.print(line);
					output.write(line.getBytes());
				}
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("filename", filename);

		FileInputStream input;
		try {
			input = new FileInputStream(filename);
			return outputStream -> {
				int nRead;
				byte[] data = new byte[1024];
				while ((nRead = input.read(data, 0, data.length)) != -1) {
					outputStream.write(data, 0, nRead);
				}
				;
				input.close();
			};
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
	
	@RequestMapping(value = "/exportAMs", method = RequestMethod.GET)
	public StreamingResponseBody exportListAMs(HttpServletResponse response) throws FileNotFoundException {
		List<Mapeamento> listMap = mappingService.getMapToExport();
		List<String> listAMs = new ArrayList<>();
		for (Mapeamento m : listMap) {
			listAMs.add(m.getMapComments());
			listAMs.add("\n");
			listAMs.add(m.getMapAssertive());
			listAMs.add("\n \n");
		}
		
		ConfigOntology configS = configService.findByTOntology("S");
		ConfigOntology configT = configService.findByTOntology("T");
		
		String filename = Constants.nameFile("AMs", configS.getName(), configT.getName(), ".txt");
		
		FileOutputStream fos = new FileOutputStream(filename);
		DataOutputStream output = new DataOutputStream(new BufferedOutputStream(fos));

		try {
			if (!listAMs.isEmpty()) {
				for (String line : listAMs) {
					System.out.print(line);
					output.write(line.getBytes());
				}
			}
			output.close();
		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}

		response.setContentType("text/html;charset=UTF-8");
		response.setHeader("filename", filename);

		FileInputStream input;
		try {
			input = new FileInputStream(filename);
			return outputStream -> {
				int nRead;
				byte[] data = new byte[1024];
				while ((nRead = input.read(data, 0, data.length)) != -1) {
					outputStream.write(data, 0, nRead);
				}
				;
				input.close();
			};
		} catch (FileNotFoundException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
