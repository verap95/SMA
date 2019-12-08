package pt.ual.meit.Repository;

import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import pt.ual.meit.JPA.ConfigOntology;

@Repository
public interface ConfigOntologyRepository extends CrudRepository<ConfigOntology, Integer> {

	ConfigOntology findByTOntologia(String tOntologia);
}