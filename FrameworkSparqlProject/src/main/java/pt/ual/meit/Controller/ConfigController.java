package pt.ual.meit.Controller;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import pt.ual.meit.DAO.ObjectOWL_DAO;
import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.ConfigOntology;
import pt.ual.meit.JPA.Mapeamento;
import pt.ual.meit.JPA.Propriedades;
import pt.ual.meit.Mappings.ModuleCMS;
import pt.ual.meit.Mappings.ModuleCRM;
import pt.ual.meit.Model.AssertivePublicObject;
import pt.ual.meit.Model.Classes;
import pt.ual.meit.Model.Mapping;
import pt.ual.meit.Model.NodesPublicObject;
import pt.ual.meit.Model.Property;
import pt.ual.meit.Model.PublicObject;
import pt.ual.meit.Model.TempAssertive;
import pt.ual.meit.Service.ClasseService;
import pt.ual.meit.Service.ConfigService;
import pt.ual.meit.Service.MappingService;
import pt.ual.meit.Service.PropriedadesService;
import pt.ual.meit.utils.Constants;
import pt.ual.meit.utils.MappingAssertive;

@Controller
@RequestMapping(value = "/configuration")
public class ConfigController {

	@Autowired
	ClasseService classService;

	@Autowired
	ConfigService configService;

	@Autowired
	PropriedadesService propService;

	@Autowired
	MappingService mapService;

	@Autowired
	ObjectOWL_DAO objectOWL_DAO;

	@Autowired
	ModuleCRM moduleCRM;
	
	@Autowired
	ModuleCMS moduleCMS;
	
	@GetMapping
	public String config() {
		return "config";
	}

	@RequestMapping(value = "/test")
	public String test() {
		return "teste";
	}

	@RequestMapping(value = "/loadOntologyTarget", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<PublicObject> configuration(Model model) {
		ConfigOntology config = configService.findByTOntology("T");
		List<Classe> classe = classService.findByIDConfig(config);

		if (!objectOWL_DAO.getAllClasses("T").isEmpty())
			objectOWL_DAO.getAllClasses("T").clear();

		objectOWL_DAO.setClasses("T", classe);

		List<Propriedades> prop;
		List<Mapeamento> map;
		for (Classe c : classe) {
			String nameClass = c.getPrefix() + ":" + c.getName();
			prop = propService.findByClassID(c);
			objectOWL_DAO.addPropertytoClass("T", nameClass, prop);

			map = mapService.findByClassID(c);
			objectOWL_DAO.addAsserivetoClass("T", nameClass, map);
		}

		PublicObject objectPublic;
		List<PublicObject> listPublicObject = new ArrayList<>();
		for (int i = 0; i < objectOWL_DAO.getAllClasses("T").size(); i++) {
			objectPublic = new PublicObject();
			objectPublic.setId(objectOWL_DAO.getAllClasses("T").get(i).getIdC());
			objectPublic.setText(objectOWL_DAO.getAllClasses("T").get(i).getText());
			objectPublic.setType("C");

			for (Property p : objectOWL_DAO.getAllClasses("T").get(i).getPropertiesList()) {
				NodesPublicObject node = new NodesPublicObject();
				node.setId(p.getId());
				node.setText(p.getName());
				node.setType(p.getFlgType());
				if (p.getClasse() != null) {
					NodesPublicObject pC = new NodesPublicObject();
					pC.setId(p.getClasse().getIdC());
					pC.setText(p.getClasse().getText());
					pC.setType("C");
					pC.setNodes(null);
					if (node.getNodes() != null)
						node.getNodes().add(pC);
					else {
						List<NodesPublicObject> listNodes = new ArrayList<>();
						listNodes.add(pC);
						node.setNodes(listNodes);
					}
				}

				objectPublic.getNodes().add(node);

			}
			listPublicObject.add(objectPublic);
		}
		return listPublicObject;
	}

	@RequestMapping(value = "/ontologyTarget/{id}&{type}", method = RequestMethod.GET, produces = {
			"application/json" })
	public @ResponseBody PublicObject loadAssertive(@RequestParam("id") Integer id, @RequestParam("type") String tipo) {
		PublicObject publicObject = new PublicObject();
		AssertivePublicObject assertivePublicO;
		List<AssertivePublicObject> listA = new ArrayList<>();
		List<Mapeamento> mapeamento;
		List<NodesPublicObject> nodes = new ArrayList<>();
		publicObject.setId(id);
		publicObject.setType(tipo);
		if (tipo.equals("C")) {
			Classe classe = classService.findById(id);
			publicObject.setText(Constants.ATRIBUTOS(classe.getPrefix(), classe.getName()));
			mapeamento = mapService.findByClassID(classe);
		} else {
			Propriedades propriedade = propService.findById(id);
			publicObject.setText(propriedade.getPrefix() + ":" + propriedade.getName());
			mapeamento = mapService.findByPropertyID(propriedade);
		}

		if (mapeamento.size() == 1 && mapeamento.get(0).getFlgBasic() == true) {
			publicObject.setaBasic(mapeamento.get(0).getMapAssertive());
		} else {
			publicObject.setaBasic(null);
			for (Mapeamento m : mapeamento) {
				assertivePublicO = new AssertivePublicObject();
				assertivePublicO.setId(m.getId());
				assertivePublicO.setText(m.getMapAssertive());
				assertivePublicO.setMapSPARQL(m.getMapSPARQL());
				assertivePublicO.setMapRules(m.getMapRules());
				listA.add(assertivePublicO);
			}
			publicObject.setListA(listA);
		}
		return publicObject;
	}

	@RequestMapping(value = "/loadOntologySource/{s}", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody PublicObject loadOntologySource(@RequestParam("s") String name) {
		PublicObject publicObject = new PublicObject();
		Classe classe = classService.findClasseByNameAndPrefix(name);
		List<NodesPublicObject> nodes = new ArrayList<>();
		List<Propriedades> propriedade = propService.findByClassID(classe);
		for (Propriedades p : propriedade) {
			NodesPublicObject node = new NodesPublicObject();
			node.setId(p.getId());
			node.setText(p.getPrefix() + ":" + p.getName());
			node.setType(p.getType());
			if (p.getClasse() != null) {
				Classes rangeClasse = new Classes();
				rangeClasse.setIdC(p.getClasse().getId());
				rangeClasse.setText(Constants.ATRIBUTOS(p.getClasse().getPrefix(), p.getClasse().getName()));
				// node.setNodes(rangeClasse);
			}
			nodes.add(node);
		}
		publicObject.setNodes(nodes);
		return publicObject;
	}

	@RequestMapping(value = "/loadOntologySource", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<PublicObject> loadOntologySource(Model model) {
		ConfigOntology config = configService.findByTOntology("S");
		List<Classe> classe = classService.findByIDConfig(config);

		if (!objectOWL_DAO.getAllClasses("S").isEmpty())
			objectOWL_DAO.getAllClasses("S").clear();

		objectOWL_DAO.setClasses("S", classe);
		List<Propriedades> prop;
		for (Classe c : classe) {
			prop = propService.findByClassID(c);
			objectOWL_DAO.addPropertytoClass("S", c.getPrefix() + ":" + c.getName(), prop);
		}

		PublicObject objectPublic;
		List<PublicObject> listPublicObject = new ArrayList<>();

		for (int i = 0; i < objectOWL_DAO.getAllClasses("S").size(); i++) {
			objectPublic = new PublicObject();
			objectPublic.setId(objectOWL_DAO.getAllClasses("S").get(i).getIdC());
			objectPublic.setText(objectOWL_DAO.getAllClasses("S").get(i).getText());
			objectPublic.setType("C");
			for (Property p : objectOWL_DAO.getAllClasses("S").get(i).getPropertiesList()) {
				NodesPublicObject node = new NodesPublicObject();
				node.setId(p.getId());
				node.setText(p.getName());
				node.setType(p.getFlgType());
				if (p.getClasse() != null) {
					NodesPublicObject pC = new NodesPublicObject();
					pC.setId(p.getClasse().getIdC());
					pC.setText(p.getClasse().getText());
					pC.setType("C");
					pC.setNodes(null);
					if (node.getNodes() != null)
						node.getNodes().add(pC);
					else {
						List<NodesPublicObject> listNodes = new ArrayList<>();
						listNodes.add(pC);
						node.setNodes(listNodes);
					}
				}
				objectPublic.getNodes().add(node);
			}
			listPublicObject.add(objectPublic);
		}

		return listPublicObject;
	}

	@RequestMapping(value = "/saveAssertive", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> saveAssertive(@RequestBody TempAssertive temp) {
		String mapRules = null;
		Classe classeSource = null;
		Classe classeTarget = null;
		Propriedades propT = null;
		Propriedades propS = null;
		List<String> mapResult = new ArrayList<>();

		if (!temp.getTypeS().equals("C") && !temp.getTypeT().equals("C")) { // Se não for mapeamento de classes
			propT = propService.findById(temp.getIdT());
			propS = propService.findById(temp.getIdS());
			Mapeamento mapC = mapService.findMapClasse(propT.getClasse(), propS.getClasse(), propS);
			if (mapC == null) 
				return new ResponseEntity<>(HttpStatus.NOT_ACCEPTABLE);
			
			String classeS = Constants.ATRIBUTOS(propS.getClasse().getPrefix(), propS.getClasse().getName());
			
			if(temp.getP1S() != null && !temp.getP1S().equals(temp.getIdS())){ //Padrão MD2
				System.out.println("IF correto - Padrão MC2");
				mapRules = moduleCRM.saveMappingRule(temp, classeS, true, false);
				mapResult = moduleCMS.saveMapSPARQL(temp, classeS, true, false);
			}else if(mapC.getPropriedadeSourceId() != null) { //Padrões MD3 e MO2
				mapRules = moduleCRM.saveMappingRule(temp, classeS, false, true);
				mapResult = moduleCMS.saveMapSPARQL(temp, classeS, false, true);
			}else{ //Padrões MD1 e MO1
				mapRules = moduleCRM.saveMappingRule(temp, classeS, false, false);
				mapResult = moduleCMS.saveMapSPARQL(temp, classeS, false, false);
			}
		} else {
			if (!temp.getTypeS().equals("C")) { // Padrão MC2
				propS = propService.findById(temp.getIdS());
				classeTarget = classService.findById(temp.getIdT());
				String classeS = Constants.ATRIBUTOS(propS.getClasse().getPrefix(), propS.getClasse().getName());
				mapRules = moduleCRM.saveMappingRule(temp, classeS, false, false);
				mapResult = moduleCMS.saveMapSPARQL(temp, classeS, false, false);
			} else { // Padrão MC1
				classeSource = classService.findById(temp.getIdS());
				classeTarget = classService.findById(temp.getIdT());
				mapRules = moduleCRM.saveMappingRule(temp, null, false, false);
				mapResult = moduleCMS.saveMapSPARQL(temp, null, false, false);
			}

		}

		if(mapResult == null)
			return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
		
		String mapComment = mapResult.get(0);
		String mapSPARQL = mapResult.get(1);

		mapService.createMapping(classeTarget, classeSource, propT, propS, temp.getAssertive(), mapSPARQL, mapRules,
				mapComment);

		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/newAssertive/{nt}&{tt}&{ns}&{ts}&{input}&{p1S}", method = RequestMethod.GET, produces = {
			"application/json" })
	public @ResponseBody TempAssertive newAssertive(@RequestParam("nt") Integer idA, @RequestParam("tt") String typeT,
			@RequestParam("ns") Integer idS, @RequestParam("ts") String typeS, @RequestParam("input") String input,
			@RequestParam(value = "p1S", required = false) Integer p1S) {
		String a;
		Propriedades pS, pT, pSOld;
		Classe cS, cT;
		Integer prop1S = null;

		if (!input.contains("null")) {
			if (typeS.equals("C")) {
				cS = classService.findById(idS);
				a = MappingAssertive.addPropertySourceToAssertive(input,
						Constants.ATRIBUTOS(cS.getPrefix(), cS.getName()));
			} else {
				pS = propService.findById(idS);
				pT = propService.findById(idA);

				if (!p1S.equals(null)) {
					pSOld = propService.findById(p1S);
					a = MappingAssertive.createN1PropertyMapping(
							Constants.ATRIBUTOS(pT.getClasse().getPrefix(), pT.getClasse().getName()),
							Constants.ATRIBUTOS(pT.getPrefix(), pT.getName()),
							Constants.ATRIBUTOS(pS.getClasse().getPrefix(), pS.getClasse().getName()),
							Constants.ATRIBUTOS(pSOld.getPrefix(), pSOld.getName()),
							Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()));
					prop1S = p1S;
				} else {
					a = MappingAssertive.addPropertySourceToAssertive(input,
							Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()));
				}
			}
		} else {
			if ((typeT.equals("D") || typeT.equals("O")) && (typeS.equals("D") || typeS.equals("O"))) {
				pS = propService.findById(idS);
				pT = propService.findById(idA);
				
				Mapeamento map = mapService.findMapClasse(pT.getClasse(),pS.getClasse(), pS);
				if(map != null) {
					if (map.getPropriedadeSourceId() == null){ //Padrões MD1, MD2 e MO1 
						a = MappingAssertive.createAssertiveMappingProperties(
								Constants.ATRIBUTOS(pS.getClasse().getPrefix(), pS.getClasse().getName()),
								Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()),
								Constants.ATRIBUTOS(pT.getClasse().getPrefix(), pT.getClasse().getName()),
								Constants.ATRIBUTOS(pT.getPrefix(), pT.getName()));
						prop1S = idS;
					}else { //Padrões MD3 e MO2
						if(pT.getType().equals("D") && pS.getType().equals("D"))
							a = MappingAssertive.createEmbedPropertyMapping(
									Constants.ATRIBUTOS(pT.getClasse().getPrefix(), pT.getClasse().getName()),
									Constants.ATRIBUTOS(pT.getPrefix(), pT.getName()), 
									Constants.ATRIBUTOS(pS.getClasse().getPrefix(), pS.getClasse().getName()), 
									Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()));
						else
							a = "Não implementado";
					}
				}else
					a = MappingAssertive.createAssertiveMappingProperties(
							Constants.ATRIBUTOS(pS.getClasse().getPrefix(), pS.getClasse().getName()),
							Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()),
							Constants.ATRIBUTOS(pT.getClasse().getPrefix(), pT.getClasse().getName()),
							Constants.ATRIBUTOS(pT.getPrefix(), pT.getName()));
			} else if (typeT.equals("C") && typeS.equals("D")) {
				cT = classService.findById(idA);
				pS = propService.findById(idS);
				a = MappingAssertive.createAssertiveMapClassProperty(
						Constants.ATRIBUTOS(pS.getClasse().getPrefix(), pS.getClasse().getName()),
						Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()),
						Constants.ATRIBUTOS(cT.getPrefix(), cT.getName()));
			} else {
				cT = classService.findById(idA);
				cS = classService.findById(idS);
				a = MappingAssertive.createAssertiveMappingClass(Constants.ATRIBUTOS(cS.getPrefix(), cS.getName()),
						Constants.ATRIBUTOS(cT.getPrefix(), cT.getName()));
			}

			// if(typeT.equals("P") && typeS.equals("P")) {
			// pS = propService.findByNameAndPrefix(nameS);
			// pT = propService.findByNameAndPrefix(nameT);
			//
			// System.out.println("Classe name S: " + pS.getClasse().getName());
			// String classS, classT;
			// classS = pS.getClasse().getPrefix() + ":" + pS.getClasse().getName();
			// classT = pT.getClasse().getPrefix() + ":" + pT.getClasse().getName();
			//
			// a = MappingAssertive.createAssertiveMappingProperties(classS, nameS, classT,
			// nameT);
			// }else if(typeT.equals("C") && typeS.equals("P")) {
			// pS = propService.findByNameAndPrefix(nameS);
			// String classS = pS.getClasse().getPrefix() + ":" + pS.getClasse().getName();
			// a = MappingAssertive.createAssertiveMapClassProperty(classS, nameS, nameT);
			// }else {
			// a = MappingAssertive.criarAssertivaBasica(nameT, nameS);
			// }
		}

		TempAssertive temp = new TempAssertive();
		temp.setAssertive(a);
		temp.setP1S(prop1S);
		return temp;
	}

	@RequestMapping(value = "/loadAllAssertives", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<Mapping> loadAllAssertives() {
		List<Mapeamento> mappings = mapService.findAll();
		List<Mapping> publicObjectM = new ArrayList<>();
		Mapping tempM;
		for (Mapeamento m : mappings) {
			tempM = new Mapping();
			tempM.setId(m.getId());
			tempM.setMapAssertive(m.getMapAssertive());
			publicObjectM.add(tempM);
		}
		return publicObjectM;
	}

	@RequestMapping(value = "/loadAllMapRules", method = RequestMethod.GET, produces = { "application/json" })
	public @ResponseBody List<Mapping> loadAllMapRules() {
		List<Mapeamento> mappings = mapService.findAll();
		List<Mapping> publicObjectM = new ArrayList<>();
		Mapping tempM;
		for (Mapeamento m : mappings) {
			tempM = new Mapping();
			tempM.setId(m.getId());
			tempM.setMapRules(m.getMapRules());
			publicObjectM.add(tempM);
		}
		return publicObjectM;
	}

	@RequestMapping(value = "/removeAssertive", method = RequestMethod.POST, headers = "Accept=application/json")
	public ResponseEntity<?> removeAssertive(@RequestBody Integer id) {
		Mapeamento tempM = mapService.finById(id);
		mapService.deleteBasicMapping(tempM);
		return new ResponseEntity<>(HttpStatus.OK);
	}

	@RequestMapping(value = "/newFilter", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody TempAssertive addFilter(@RequestBody TempAssertive temp) {
		if(temp.getP1S() != null)
			return temp;
		
		String assertiveFilter = MappingAssertive.addFilterToAssertive(temp.getAssertive(), temp.getFilter(),
				temp.getTypeS(), temp.getTypeT());
		TempAssertive output = new TempAssertive();
		output.setAssertive(assertiveFilter);
		return output;
	}

	@RequestMapping(value = "/getTypeFunction", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<String> getFunctions(@RequestParam("tf") String typeFunction) {
		List<String> listFunction = new ArrayList();
		
		if(typeFunction.equals("String")) 
			listFunction = Constants.getListFunctionString();
		else if(typeFunction.equals("Data")) 
			listFunction = Constants.getListFunctionData();
		else
			listFunction.add("NOT IMPLEMENTED YET");
			
		return listFunction;
	}
	
	@RequestMapping(value="/getPropertiesToList", method = RequestMethod.GET, headers = "Accept=application/json")
	public @ResponseBody List<String> getProperties(@RequestParam("idS") Integer idSource, @RequestParam("typeS") String typeS){
		List<String> listFunction = new ArrayList<>();
		List<Propriedades> listP = new ArrayList<>();
		if(!typeS.equals("C")) {
			Propriedades pS = propService.findById(idSource);
			listP = propService.findByClassID(pS.getClasse());
		}else {
			Classe cS = classService.findById(idSource);
			listP = propService.findByClassID(cS);
		}
		
		if(!listP.isEmpty()) {
			for(Propriedades p : listP) {
				listFunction.add(Constants.ATRIBUTOS(p.getPrefix(), p.getName()));
			}
		}
		
		return listFunction;
	}
}
