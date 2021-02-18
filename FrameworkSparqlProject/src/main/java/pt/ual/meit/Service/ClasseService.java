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
		if(classe.isPresent())
			return classe.get(); 
		else
			return null;
	}

	public List<Classe> findByIDConfig(ConfigOntology config) {
		List<Classe> list = classeRepository.findByOntologyAndFlgAtivo(config, true);
		return list;
	}

	public Classe findClasse(String name, String prefix, ConfigOntology ontology) {
		Classe classe = classeRepository.findByPrefixAndNameAndOntologyAndFlgAtivo(prefix, name, ontology, true);
		return classe;
	}

	public List<Classe> findAll() {
		List<Classe> classe = (List<Classe>) classeRepository.findAll();
		return classe;
	}

	public Classe findClasseByNameAndPrefix(String name) {
		String[] nameClass = name.split(":");
		Classe classe = classeRepository.findByNameAndPrefixAndFlgAtivo(nameClass[1], nameClass[0], true);
		return classe;
	}

	public void deleteAllClasses() {
		classeRepository.deleteAll();
	}

}
