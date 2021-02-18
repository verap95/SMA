package pt.ual.meit.Service;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.Propriedades;
import pt.ual.meit.Repository.PropriedadesRepository;

@Service
public class PropriedadesService {

	@Autowired
	PropriedadesRepository propriedadesRepository;

	public Propriedades createBasicProperty(Propriedades propriedade) {
		Propriedades propEntity = new Propriedades(propriedade.getName(), propriedade.getPrefix(),
				propriedade.getType(), propriedade.getClasse(), propriedade.getRangeClasse(), propriedade.getFlgAtivo());
		return propEntity = propriedadesRepository.save(propEntity);
	}

	public List<Propriedades> findByClassID(Classe classe) {
		List<Propriedades> list = propriedadesRepository.findByClasse(classe);
		return list;
	}

	public Propriedades findByNameAndPrefix(String name) {
		String[] nameProp = name.split(":");
		Propriedades prop = propriedadesRepository.findByNameAndPrefix(nameProp[1], nameProp[0]);
		return prop;
	}

	public List<Propriedades> findAll() {
		List<Propriedades> propEntity = (List<Propriedades>) propriedadesRepository.findAll();
		return propEntity;
	}
	
	public Propriedades findById(Integer id) {
		Optional<Propriedades> prop = propriedadesRepository.findById(id);
		Propriedades getResult;
		if(prop.isPresent())
			getResult = prop.get();
		else
			getResult = null;
		
		return getResult;
	}
	
	public void deleteAllProp() {
		propriedadesRepository.deleteAll();
	}
}
