package pt.ual.meit.JPA;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

@Entity
public class Propriedades {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	private Integer id;

	private String name;

	private String prefix;

	private String type;
	
	private Boolean flgAtivo;

	@ManyToOne
	@JoinColumn(name = "classe_id", nullable = false)
	private Classe classe;
	
	@ManyToOne(cascade = {CascadeType.ALL})
	@JoinColumn(name = "rangeClasse_id", nullable = true)
	private Classe rangeClasse;
	

	protected Propriedades() {
	};

	public Propriedades(String name, String prefix, String type, Classe classe, Classe rangeClasse, Boolean flgAtivo) {
		super();
		this.name = name;
		this.prefix = prefix;
		this.type = type;
		this.classe = classe;
		this.rangeClasse = rangeClasse;
		this.flgAtivo = flgAtivo;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getPrefix() {
		return prefix;
	}

	public void setPrefix(String prefix) {
		this.prefix = prefix;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public Classe getClasse() {
		return classe;
	}

	public void setClasse(Classe classe) {
		this.classe = classe;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Classe getRangeClasse() {
		return rangeClasse;
	}

	public void setRangeClasse(Classe rangeClasse) {
		this.rangeClasse = rangeClasse;
	}

	public Boolean getFlgAtivo() {
		return flgAtivo;
	}

	public void setFlgAtivo(Boolean flgAtivo) {
		this.flgAtivo = flgAtivo;
	}

	
	
}
