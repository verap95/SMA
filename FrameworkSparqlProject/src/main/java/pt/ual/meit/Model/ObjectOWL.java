package pt.ual.meit.Model;

import java.util.List;

import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.Mapeamento;
import pt.ual.meit.JPA.Propriedades;

public interface ObjectOWL {

	List<Classes> getAllClasses(String fonte);

	void setClasses(String fonte, List<Classe> classe);

	void addPropertytoClass(String fonte, String nameClass, List<Propriedades> propriedades);

	void addAsserivetoClass(String fonte, String nameClass, List<Mapeamento> mapeamentos);

	void addDtPropertytoClass_(String nameClass, Property property);

	void addObjPropertytoClass_(String nameClass, Property property);

}
