package pt.ual.meit.Repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.ConfigOntology;

@Repository
public interface ClasseRepository extends CrudRepository<Classe, Integer> {

	List<Classe> findByOntologyAndFlgAtivo(ConfigOntology config, Boolean flgAtivo);

	Classe findByPrefixAndNameAndOntologyAndFlgAtivo(String prefix, String name, ConfigOntology ontology, Boolean flgAtivo);

	Classe findByNameAndPrefixAndFlgAtivo(String name, String prefix, Boolean flgAtivo);
}
