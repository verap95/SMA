package pt.ual.meit.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.Mapeamento;
import pt.ual.meit.JPA.Propriedades;
import pt.ual.meit.Repository.MappingRepository;

@Service
public class MappingService {

	@Autowired
	MappingRepository mappingRepository;
	
	/*
	 *	Métodos para criar mapeamentos
	 * 	createBasicAssertive
	 * 	createMapping
	 */
	public Mapeamento createBasicAssertive(Classe classe, Propriedades propriedade) {
		String mapA;
		Mapeamento mapeamentoEntity;
		if(classe == null) {
			mapA = propriedade.getPrefix() + ":" + propriedade.getName() + " ≡ null";
			mapeamentoEntity = new Mapeamento(null, null, propriedade, null, mapA, null, null, true, null);
		}else {
			mapA = classe.getPrefix() + ":" + classe.getName() + " ≡ null";
			mapeamentoEntity = new Mapeamento(classe, null, null, null, mapA, null, null, true, null);
		}
		return mapeamentoEntity = mappingRepository.save(mapeamentoEntity);
	}
	
	public Mapeamento createMapping(Classe classeTarget, Classe classeSource, Propriedades propTarget, Propriedades propSource, String mapAssertive, String mapSPARQL, String mapRules, String mapComment, String clauseWhere, String clauseFilter, String listProps) {
		Mapeamento mapeamentoEntity;
		List<Mapeamento> mList = null;
		if (classeTarget == null) {
			mList = findByPropertyID(propTarget);
			if (mList.size() == 1) {
				if(mList.get(0).getFlgBasic() == true)
					deleteBasicMapping(mList.get(0));
			}
			mapeamentoEntity = new Mapeamento(null, null, propTarget, propSource, mapAssertive,
					mapSPARQL, mapRules, false, mapComment);
		} else {
			mList = findByClassID(classeTarget);
			if (mList.size() == 1) {
				if (mList.get(0).getFlgBasic() == true) 
					deleteBasicMapping(mList.get(0));
			}
			mapeamentoEntity = new Mapeamento(classeTarget, classeSource, propSource, mapAssertive, 
					mapSPARQL, mapRules, false, mapComment, clauseWhere, clauseFilter, listProps);
		}
		return mapeamentoEntity = mappingRepository.save(mapeamentoEntity);
	}
	
	
	/*
	 * Métodos utilizados para encontrar os mapeamentos
	 * findByClassID
	 * findByPropertyID
	 * findByMapAssertive
	 */
	
	public Mapeamento finById(Integer id) {
		Optional<Mapeamento> mapeamentoEntity = mappingRepository.findById(id);
		if(mapeamentoEntity.isPresent())
			return mapeamentoEntity.get();
		else
			return null;
	}
	
	public List<Mapeamento> findByClassID(Classe classe) {
		List<Mapeamento> mapeamentoEntity = mappingRepository.findByClasseTargetId(classe);
		return mapeamentoEntity;
	}
	
	public List<Mapeamento> findByPropertyID(Propriedades prop) {
		List<Mapeamento> mapeamentoEntity = mappingRepository.findByPropriedadeTargetId(prop);
		return mapeamentoEntity;
	}

	public Mapeamento findByMapAssertive(String mapA) {
		Mapeamento mapeamentoEntity = mappingRepository.findByMapAssertive(mapA);
		return mapeamentoEntity;
	}

	
	public List<Mapeamento> findAll() {
		List<Mapeamento> mapeamentoEntity = (List<Mapeamento>) mappingRepository.findAll();
		List<Mapeamento> finalM = new ArrayList<>();
		for(Mapeamento m : mapeamentoEntity) {
			if(m.getFlgBasic() == false)
				finalM.add(m);
		}		
		return finalM;
	}
	
	/**
	 * Método para procurar os mapeamentos efetuados pelo utilizador
	 */
	public List<Mapeamento> getMapToExport(){
		List<Mapeamento> listMapExport = mappingRepository.findByFlgBasic(false);
		return listMapExport;
	}
	
	/*
	 * Método para eliminar a assertiva básica 
	 */
	public void deleteBasicMapping(Mapeamento m) {
		mappingRepository.delete(m);
	}

	public Mapeamento findMapClasse(Classe classeT, Classe classeS, Propriedades propS) {
		Mapeamento mapC = mappingRepository.findByClasseTargetIdAndClasseSourceId(classeT, classeS);
		if(mapC == null) {
			mapC = findByMapClasseProp(classeT, propS);
		}
		return mapC;
	}
	
	public Mapeamento findByMapClasseProp(Classe classeT, Propriedades propS) {
		Mapeamento mapC = mappingRepository.findByClasseTargetIdAndPropriedadeSourceId(classeT, propS);
		return mapC;
	}
	
	public void deleteAllMapping() {
		mappingRepository.deleteAll();
	}
}
