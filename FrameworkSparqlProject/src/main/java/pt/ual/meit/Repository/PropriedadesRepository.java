package pt.ual.meit.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.Propriedades;

@Repository
public interface PropriedadesRepository extends CrudRepository<Propriedades, Integer> {

	List<Propriedades> findByClasse(Classe classe);

	Propriedades findByNameAndPrefix(String name, String prefix);
}
