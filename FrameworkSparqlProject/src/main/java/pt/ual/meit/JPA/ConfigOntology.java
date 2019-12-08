package pt.ual.meit.JPA;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

@Entity
public class ConfigOntology {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "config_id")
	private Integer id;

	private String name;
	private String language;
	private String tOntologia;

	@OneToMany(targetEntity = Classe.class, mappedBy = "ontology", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Classe> classe;

	protected ConfigOntology() {
	}

	public ConfigOntology(String name, String language, String tOntologia) {
		this.name = name;
		this.language = language;
		this.tOntologia = tOntologia;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getLanguage() {
		return language;
	}

	public void setLanguage(String language) {
		this.language = language;
	}

	public String gettOntologia() {
		return tOntologia;
	}

	public void settOntologia(String tOntologia) {
		this.tOntologia = tOntologia;
	}

	@Override
	public String toString() {
		return "ConfigOntology [id=" + id + ", name=" + name + ", language=" + language + ", tOntologia=" + tOntologia
				+ "]";
	}

}
