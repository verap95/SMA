package pt.ual.meit.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.ConfigOntology;
import pt.ual.meit.Repository.ClasseRepository;

@Service
public class ClasseService {

	@Autowired
	ClasseRepository classeRepository;

	public Classe createBasicClass(Classe classe) {
		Classe classeEntity = new Classe(classe.getName(), classe.getPrefix(), classe.getOntology(), classe.getFlgAtivo());
		return classeEntity = classeRepository.save(classeEntity);
	}
	
	public Classe findById(Integer id) {
		Optional<Classe> classe = classeRepository.findById(id);		
		return classe.get(); 
	}

	public List<Classe> findByIDConfig(ConfigOntology config) {
		System.out.println("Entrei no findByIDConfig do classeRepository");
		List<Classe> list = classeRepository.findByOntologyAndFlgAtivo(config, true);
		System.out.println("LIst classe: " + list.size());
		return list;
	}

	public Classe findClasse(String name, String prefix, ConfigOntology ontology) {
		Classe classe = classeRepository.findByPrefixAndNameAndOntologyAndFlgAtivo(prefix, name, ontology, true);
		return classe;
	}
	
//	public Classe findById(Classe c) {
//		Optional<Classe> classe = classeRepository.findById(id);
//		Classe classResult;
//		if(classe.isPresent()) {
//			classResult = classe.get();
//		}else {
//			classResult = null;
//		}
//		return classResult;
//	}

	public List<Classe> findAll() {
		List<Classe> classe = (List<Classe>) classeRepository.findAll();
		return classe;
	}

	public Classe findClasseByNameAndPrefix(String name) {
		String[] nameClass = name.split(":");
		Classe classe = classeRepository.findByNameAndPrefixAndFlgAtivo(nameClass[1], nameClass[0], true);
		return classe;
	}

//	public void deleteAllClasses() {
//		List<Classe> classes = findAll();
//		assertivaRepository.deleteAllAssertivesForClasse(classes);
//		classeRepository.deleteAll();
//		System.out.println("Delete all classes");
//	}

}
