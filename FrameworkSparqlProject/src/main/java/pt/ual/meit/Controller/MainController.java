package pt.ual.meit.Controller;

import java.io.FileNotFoundException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

import pt.ual.meit.DAO.ObjectOWL_DAO;
import pt.ual.meit.JPA.ConfigOntology;
import pt.ual.meit.Model.MappingConfiguration;
import pt.ual.meit.Service.ClasseService;
import pt.ual.meit.Service.ConfigService;
import pt.ual.meit.Service.OntologyBuilder;

@Controller
public class MainController {

	@Autowired
	ConfigService configService;

	@Autowired
	OntologyBuilder ont;

	@Autowired
	ClasseService classService;

	private ObjectOWL_DAO objectOWL_DAO = new ObjectOWL_DAO();

	@RequestMapping("/")
	public String home() {
		System.out.println("Entrei no main");
		return "main";
	}

	@RequestMapping(value = "/saveMapping", method = RequestMethod.POST, consumes = { "multipart/form-data" })
	public ResponseEntity<?> saveMapping(@RequestParam(value = "fileS", required = false) MultipartFile fileS,
			@RequestPart(value = "fileT", required = false) MultipartFile fileT,
			@RequestPart("requestData") MappingConfiguration mappingConfiguration) {

		if (configService.findAll() != null) {
			//classService.deleteAllClasses();
			objectOWL_DAO.getAllClasses("S").clear();
			objectOWL_DAO.getAllClasses("T").clear();

		}
		ConfigOntology confS = new ConfigOntology(mappingConfiguration.getOntologyS().getName(),
				mappingConfiguration.getOntologyS().getLanguage(), "S");

		ConfigOntology confT = new ConfigOntology(mappingConfiguration.getOntologyT().getName(),
				mappingConfiguration.getOntologyT().getLanguage(), "T");

		ConfigOntology confSaveS = configService.createConfigOntology(confS);
		ConfigOntology confSaveT = configService.createConfigOntology(confT);

		if (fileS != null) {
			mappingConfiguration.getOntologyS().setFile(fileS);
			try {
				ont.buildOntologyTree(mappingConfiguration.getOntologyS(), confSaveS.getId());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			mappingConfiguration.getOntologyS().setFile(null);
		}

		if (fileT != null) {
			mappingConfiguration.getOntologyT().setFile(fileT);
			try {
				ont.buildOntologyTree(mappingConfiguration.getOntologyT(), confSaveT.getId());
			} catch (FileNotFoundException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
		} else {
			mappingConfiguration.getOntologyT().setFile(null);
		}
		
		return new ResponseEntity<ObjectOWL_DAO>(objectOWL_DAO, HttpStatus.OK);
	};
}
