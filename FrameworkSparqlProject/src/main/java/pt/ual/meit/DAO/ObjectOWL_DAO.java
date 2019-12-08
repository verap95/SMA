package pt.ual.meit.DAO;

import java.util.ArrayList;
import java.util.List;

import org.springframework.stereotype.Component;

import pt.ual.meit.JPA.Classe;
import pt.ual.meit.JPA.Mapeamento;
import pt.ual.meit.JPA.Propriedades;
import pt.ual.meit.Model.Classes;
import pt.ual.meit.Model.Mapping;
import pt.ual.meit.Model.ObjectOWL;
import pt.ual.meit.Model.Property;
import pt.ual.meit.Service.ClasseService;
import pt.ual.meit.Service.PropriedadesService;
import pt.ual.meit.utils.Constants;;

@Component
public class ObjectOWL_DAO implements ObjectOWL {

	private List<Classes> Target = new ArrayList<>();
	private List<Classes> Source = new ArrayList<>();

	// private List<Classe> classList = new ArrayList<>();
	// private List<Class_> classList_ = new ArrayList<>();

	@Override
	public void addDtPropertytoClass_(String nameClass, Property property) {
		// for (Class_ c : classList_) {
		// if (c.getName().equals(nameClass)) {
		// c.getListDataTypeProperty().add(property);
		// }
		// }
	}

	@Override
	public void addObjPropertytoClass_(String nameClass, Property property) {
		// for (Class_ c : classList_) {
		// if (c.getName().equals(nameClass)) {
		// c.getListObjectProperty().add(property);
		// }
		// }

	}

	@Override
	public void setClasses(String fonte, List<Classe> classe) {
		Classes classes;
		for (Classe c : classe) {
			classes = new Classes();
			classes.setText(c.getPrefix() + ":" + c.getName());
			classes.setIdC(c.getId());
			if (fonte == "T")
				Target.add(classes);
			else
				Source.add(classes);
		}
	}

	@Override
	public List<Classes> getAllClasses(String fonte) {
		if (fonte.equals("T"))
			return Target;
		else
			return Source;
	}

	@Override
	public void addPropertytoClass(String fonte, String nameClass, List<Propriedades> propriedades) {
		Property prop;
		List<Property> listProp = new ArrayList<>();
		ClasseService classService = new ClasseService();
		PropriedadesService propService = new PropriedadesService();
		for (Propriedades p : propriedades) {
			prop = new Property();
			prop.setId(p.getId());
			prop.setName(p.getPrefix() + ":" + p.getName());
			prop.setFlgType(p.getType());
			if(p.getRangeClasse() != null) {
				Classes c = new Classes();
				c.setIdC(p.getRangeClasse().getId());
				c.setText(Constants.ATRIBUTOS(p.getRangeClasse().getPrefix(), p.getRangeClasse().getName()));
				
				c.setPropertiesList(null);
				prop.setClasse(c);
			}else
				prop.setClasse(null);
			listProp.add(prop);
		}

		if (fonte.equals("T")) {
			for (Classes c : Target) {
				if (c.getText().equals(nameClass))
					c.setPropertiesList(listProp);
			}
		} else {
			for (Classes c : Source) {
				if (c.getText().equals(nameClass))
					c.setPropertiesList(listProp);
			}
		}
	}

	@Override
	public void addAsserivetoClass(String fonte, String nameClass, List<Mapeamento> mapeamento) {
		Mapping mapping;
		List<Mapping> listMappings = new ArrayList<>();

		for (Mapeamento m : mapeamento) {
			mapping = new Mapping();
			mapping.setMapAssertive(m.getMapAssertive());
			listMappings.add(mapping);
		}

		if (fonte.equals("T")) {
			for (Classes c : Target) {
				if (c.getText().equals(nameClass))
					c.setMappingList(listMappings);
			}
		} else {
			for (Classes c : Source) {
				if (c.getText().equals(nameClass))
					c.setMappingList(listMappings);
			}
		}

	}

}
