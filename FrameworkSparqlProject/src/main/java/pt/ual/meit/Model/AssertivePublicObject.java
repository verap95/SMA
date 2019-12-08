package pt.ual.meit.Model;

public class AssertivePublicObject {
	private Integer id;
	private String text;
	private String mapSPARQL;
	private String mapRules;
	
	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
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

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}	
}
