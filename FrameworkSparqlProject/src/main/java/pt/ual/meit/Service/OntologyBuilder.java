package pt.ual.meit.Service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.List;

import org.apache.jena.ontology.DatatypeProperty;
import org.apache.jena.ontology.ObjectProperty;
import org.apache.jena.ontology.OntClass;
import org.apache.jena.ontology.OntModel;
import org.apache.jena.ontology.OntModelSpec;
import org.apache.jena.ontology.OntResource;
import org.apache.jena.rdf.model.ModelFactory;
import org.apache.jena.rdf.model.StmtIterator;
import org.apache.jena.util.iterator.ExtendedIterator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ual.meit.DAO.ObjectOWL_DAO;
//import pt.ual.meit.Model.Classe;
import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.ConfigOntology;
import pt.ual.meit.JPA.Propriedades;
import pt.ual.meit.Model.MappingConfigurationOntology;
import pt.ual.meit.utils.Constants;
import pt.ual.meit.utils.Prefixes;

@Component
public class OntologyBuilder {

	@Autowired
	ConfigService configService;

	@Autowired
	ClasseService classService;

	@Autowired
	PropriedadesService propService;

	@Autowired
	MappingService mapService;

	@Autowired
	ObjectOWL_DAO objectOWL_DAO;
	
	public List<Classe> buildOntologyTree(MappingConfigurationOntology mc, Integer idO) throws FileNotFoundException {
		OntModel m = ModelFactory.createOntologyModel(OntModelSpec.OWL_MEM);
		InputStream file;
		ConfigOntology ontology = configService.findById(idO);
		Classe classe;

		Prefixes prefixes = new Prefixes();
		try {
			file = mc.getFile().getInputStream();
			if (file != null)
				m.read(mc.getFile().getInputStream(), null, mc.getLanguage());

			// Lista as classes
			ExtendedIterator<OntClass> class_ = m.listClasses();
			while (class_.hasNext()) {
				OntClass ontclass = (OntClass) class_.next();

				String name = ontclass.getLocalName();

				if (name != null) {
					String prefix = m.getNsURIPrefix(ontclass.getNameSpace());
					prefixes.setValue(prefix, ontclass.getNameSpace());
					classe = new Classe(name, prefix, ontology, true);
					classService.createBasicClass(classe);
				}
			}

			// Lista as propriedades do tipo DataType
			ExtendedIterator<DatatypeProperty> dtp_ = m.listDatatypeProperties();
			Propriedades prop;
			while (dtp_.hasNext()) {
				DatatypeProperty dataTypeProp = (DatatypeProperty) dtp_.next();
				ExtendedIterator iDomains_ = dataTypeProp.listDomain();
				while (iDomains_.hasNext()) {
					OntClass cDomain = (OntClass) iDomains_.next();
					
					String cPrefix = m.getNsURIPrefix(cDomain.getNameSpace());
					String cName = cDomain.getLocalName();
					String dPrefix = m.getNsURIPrefix(dataTypeProp.getNameSpace());
					prefixes.setValue(dPrefix, dataTypeProp.getNameSpace());
					String dpName = dataTypeProp.getLocalName();
					OntResource dpTypeR = dataTypeProp.getRange();
					System.out.println("Tipo de Dados: " + dpTypeR.getLocalName());
					classe = classService.findClasse(cName, cPrefix, ontology);
					prop = new Propriedades(dpName, dPrefix, "D", classe, null, true);
					propService.createBasicProperty(prop);
				}
			}

			System.out.println("** Listagem das propriedades do tipo Object **");
			// Lista as propriedades do tipo Object
			ExtendedIterator<ObjectProperty> objp_ = m.listObjectProperties();
			while (objp_.hasNext()) {
				ObjectProperty objectProp = (ObjectProperty) objp_.next();
				ExtendedIterator iDomains_ = objectProp.listDomain();
				while (iDomains_.hasNext()) {
					OntClass cDomain = (OntClass) iDomains_.next();
					System.out.println("será aqui ? : " + cDomain.getLocalName() + m.getNsURIPrefix(cDomain.getNameSpace()));
					classe = classService.findClasse(cDomain.getLocalName(), m.getNsURIPrefix(cDomain.getNameSpace()),
							ontology);
					System.out.println("DOMAIN CLASS OBJECT : "+ cDomain.getLocalName());
					System.out.println("Object Range: " + objectProp.getRange());
					List<Propriedades> propA = new ArrayList<>();
					if(objectProp.getRange() != null) {
						OntResource ont = objectProp.getRange();
						System.out.println(ont.getLocalName());
						System.out.println("WTF: " + Constants.ATRIBUTOS(m.getNsURIPrefix(ont.getNameSpace()), ont.getLocalName()));
						Classe classObj = classService.findClasseByNameAndPrefix(Constants.ATRIBUTOS(m.getNsURIPrefix(ont.getNameSpace()), ont.getLocalName()));
						System.out.println("ClassObj : " + classObj);
						if(classObj == null) {
							Classe rangeClasse = new Classe(ont.getLocalName(), m.getNsURIPrefix(ont.getNameSpace()), ontology, false);
							//classService.createBasicClass(rangeClasse);
							prop = new Propriedades(objectProp.getLocalName(),  m.getNsURIPrefix(objectProp.getNameSpace()), "O", classe, rangeClasse, true);
							prefixes.setValue( m.getNsURIPrefix(ont.getNameSpace()), ont.getNameSpace());
							
							StmtIterator listP = ont.listProperties();
							while(listP.hasNext()) {
								DatatypeProperty dp = (DatatypeProperty) listP.next();
								String dPrefix = m.getNsURIPrefix(dp.getNameSpace());
								prefixes.setValue(dPrefix, dp.getNameSpace());
								String dpName = dp.getLocalName();
								Propriedades propC = new Propriedades(dpName, dPrefix, "D", rangeClasse, null, true);
								propService.createBasicProperty(propC);
							}
						}else {
							prop = new Propriedades(objectProp.getLocalName(),  m.getNsURIPrefix(objectProp.getNameSpace()), "O", classe, classObj, true);
						}
							
					}else
						prop = new Propriedades(objectProp.getLocalName(), m.getNsURIPrefix(objectProp.getNameSpace()), "O", classe, null, true);
					
					propService.createBasicProperty(prop);
//					for(Propriedades p : propA) {
//						Propriedades pa = new Propriedades(p.getName(), p.getPrefix(), p.getType(), p.getClasse(), p.getRangeClasse(), false);
//						propService.createBasicProperty(pa);
//					}
					prefixes.setValue(m.getNsURIPrefix(objectProp.getNameSpace()), objectProp.getNameSpace());
				}
			}

			List<Classe> classeList = classService.findAll();
			for (Classe c : classeList) {
				if (c.getOntology().gettOntologia() == "T")
					mapService.createBasicAssertive(c, null);
			}

			List<Propriedades> propList = propService.findAll();
			for (Propriedades p : propList) {
				Classe c = classService
						.findClasseByNameAndPrefix(p.getClasse().getPrefix() + ":" + p.getClasse().getName());

				ConfigOntology o = c.getOntology();
				if (o.gettOntologia() == "T")
					mapService.createBasicAssertive(null, p);
			}

		} catch (IOException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
		}
		return null;
	}
}
