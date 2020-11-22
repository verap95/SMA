package pt.ual.meit.JPA;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.Lob;
import javax.persistence.ManyToOne;

@Entity
public class Mapeamento {
	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@Column(name = "assertive_id")
	private Integer id;
	
	private String mapAssertive;
	
	@Lob
	private String mapSPARQL;
	
	private String mapRules;
	
	private Boolean flgBasic;
	
	private String mapComments;
	
	@ManyToOne
	@JoinColumn(name = "classT_id", nullable = true)
	private Classe classeTargetId;
	
	@ManyToOne
	@JoinColumn(name = "classS_id", nullable = true)
	private Classe classeSourceId;

	@ManyToOne
	@JoinColumn(name = "propertyS_id", nullable = true)
	private Propriedades propriedadeSourceId;

	@ManyToOne
	@JoinColumn(name = "propertyT_id", nullable = true)
	private Propriedades propriedadeTargetId;
	
	private String listProps;
	
	private String clauseWhere;
	
	private String clauseFilter;
	
	protected Mapeamento() {
	}

	public Mapeamento(
			Classe classeTarget,
			Classe classeSource,
			Propriedades propriedadeTarget,
			Propriedades propriedadeSource,
			String mapAssertive, String mapSPARQL, String mapRules, Boolean flgBasic, String mapComments) {
		super();
		this.classeTargetId = classeTarget;
		this.classeSourceId = classeSource;
		this.propriedadeTargetId = propriedadeTarget;
		this.propriedadeSourceId = propriedadeSource;
		this.mapAssertive = mapAssertive;
		this.mapSPARQL = mapSPARQL;
		this.mapRules = mapRules;
		this.setFlgBasic(flgBasic);
		this.setMapComments(mapComments);
	}

	//Mapeamento de Classes
	public Mapeamento(
			Classe classeTargetId, Classe classeSourceId,  Propriedades propriedadeSourceId, String mapAssertive, String mapSPARQL, String mapRules, Boolean flgBasic,
			String mapComments, String clauseWhere, String clauseFilter, String listProps) {
		super();
		this.mapAssertive = mapAssertive;
		this.mapSPARQL = mapSPARQL;
		this.mapRules = mapRules;
		this.flgBasic = flgBasic;
		this.mapComments = mapComments;
		this.classeTargetId = classeTargetId;
		this.classeSourceId = classeSourceId;
		this.propriedadeSourceId = propriedadeSourceId;
		this.listProps = listProps;
		this.clauseWhere = clauseWhere;
		this.clauseFilter = clauseFilter;
	}

	public String getMapAssertive() {
		return mapAssertive;
	}
	
	public void setMapAssertive(String mapAssertive) {
		this.mapAssertive = mapAssertive;
	}

	public String getMapSPARQL() {
		return mapSPARQL;
	}

	public void setMapSPARQL(String mapSPARQL) {
		this.mapSPARQL = mapSPARQL;
	}

	public String getMapRules() {
		return mapRules;
	}

	public void setMapRules(String mapRules) {
		this.mapRules = mapRules;
	}

	public Classe getClasseTargetId() {
		return classeTargetId;
	}

	public void setClasseTargetId(Classe classeTargetId) {
		this.classeTargetId = classeTargetId;
	}

	public Classe getClasseSourceId() {
		return classeSourceId;
	}

	public void setClasseSourceId(Classe classeSourceId) {
		this.classeSourceId = classeSourceId;
	}
	
	public Propriedades getPropriedadeSourceId() {
		return propriedadeSourceId;
	}

	public void setPropriedadeSourceId(Propriedades propriedadeSourceId) {
		this.propriedadeSourceId = propriedadeSourceId;
	}

	public Propriedades getPropriedadeTargetId() {
		return propriedadeTargetId;
	}

	public void setPropriedadeTargetId(Propriedades propriedadeTargetId) {
		this.propriedadeTargetId = propriedadeTargetId;
	}

	public Boolean getFlgBasic() {
		return flgBasic;
	}

	public void setFlgBasic(Boolean flgBasic) {
		this.flgBasic = flgBasic;
	}

	public String getMapComments() {
		return mapComments;
	}

	public void setMapComments(String mapComments) {
		this.mapComments = mapComments;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getClauseWhere() {
		return clauseWhere;
	}

	public void setClauseWhere(String clauseWhere) {
		this.clauseWhere = clauseWhere;
	}

	public String getClauseFilter() {
		return clauseFilter;
	}

	public void setClauseFilter(String clauseFilter) {
		this.clauseFilter = clauseFilter;
	}

	public String getListProps() {
		return listProps;
	}

	public void setListProps(String listProps) {
		this.listProps = listProps;
	}	
}
