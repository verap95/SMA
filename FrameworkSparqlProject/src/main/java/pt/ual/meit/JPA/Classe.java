package pt.ual.meit.JPA;

import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;

@Entity
public class Classe {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "class_id")
	private Integer id;

	private String prefix;

	private String name;

	private Boolean flgAtivo;
	
	@ManyToOne
	@JoinColumn(name = "config_id", nullable = false)
	private ConfigOntology ontology;

	@OneToMany(targetEntity = Propriedades.class, mappedBy = "classe", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
	private Set<Propriedades> prop;

	protected Classe() {
	};

	public Classe(String name, String prefix, ConfigOntology ontology, Boolean flgAtivo) {
		this.name = name;
		this.prefix = prefix;
		this.ontology = ontology;
		this.flgAtivo = flgAtivo;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public ConfigOntology getOntology() {
		return ontology;
	}

	public void setOntology(ConfigOntology ontology) {
		this.ontology = ontology;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Boolean getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(Boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

	public Set<Propriedades> getProp() {
		return prop;
	}

	public void setProp(Set<Propriedades> prop) {
		this.prop = prop;
	}
	
	

}
