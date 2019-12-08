package pt.ual.meit.Repository;

import java.util.List;

import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.Mapeamento;
import pt.ual.meit.JPA.Propriedades;

@Repository
public interface MappingRepository extends CrudRepository<Mapeamento, Integer>{

	//List<Mapeamento> deleteByClasse(Classe classe);
	
	//List<Mapeamento> findByClasse(Classe classe);
	
	List<Mapeamento> findByClasseTargetId(Classe classe);
	
	Mapeamento findByClasseTargetIdAndClasseSourceId(Classe classeT, Classe classeS);
	
	List<Mapeamento> findByPropriedadeTargetId(Propriedades propriedade);
	
	List<Mapeamento> findByFlgBasic(Boolean flgBasic);
	
	Mapeamento findByMapAssertive(String mapAssertive);
	
	@Modifying
    @Query("UPDATE Mapeamento a SET a.mapAssertive = :mapAssertive, a.mapSPARQL = :mapSPARQL, a.mapRules = :mapRules WHERE a.id = :assertive_id")
    int updateByMapAssertiveAndMapSPARQLAndMapRules(@Param("assertive_id") int id, @Param("mapAssertive") String mapAssertive, @Param("mapSPARQL") String mapSPARQL, @Param("mapRules") String mapRules);
	
}
