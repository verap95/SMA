package pt.ual.meit.Service;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ual.meit.JPA.ConfigOntology;
import pt.ual.meit.Repository.ConfigOntologyRepository;

@Service
public class ConfigService {

	@Autowired
	ConfigOntologyRepository configOntologyRepository;

	public ConfigOntology createConfigOntology(ConfigOntology confO) {
		ConfigOntology confEntity = new ConfigOntology(confO.getName(), confO.getLanguage(), confO.gettOntologia());

		return confEntity = configOntologyRepository.save(confEntity);
	}

	public ConfigOntology findById(Integer id) {
		System.out.println("Entrei aqui antes de rebentar");
		Optional<ConfigOntology> config = configOntologyRepository.findById(id);
		ConfigOntology getResult;
		if (config.isPresent())
			getResult = config.get();
		else
			getResult = null;

		return getResult;
	}

	public Iterable<ConfigOntology> findAll() {
		Iterable<ConfigOntology> config = configOntologyRepository.findAll();
		return config;
	}

	public ConfigOntology findByTOntology(String tOntology) {
		ConfigOntology config = configOntologyRepository.findByTOntologia(tOntology);
		return config;
	}
	
	public void deleteAllConfig() {
		configOntologyRepository.deleteAll();
	}
}
