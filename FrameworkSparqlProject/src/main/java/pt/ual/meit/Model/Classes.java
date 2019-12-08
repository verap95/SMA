package pt.ual.meit.Model;

import java.util.ArrayList;
import java.util.List;

public class Classes {
	private Integer idC;
	private String text;
	private List<Property> propertiesList = new ArrayList<>();
	private List<Mapping> mappingList = new ArrayList<>();

	/**
	 * 
	 */
	public Classes() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public Classes(String text) {
		super();
		this.text = text;
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List<Property> getPropertiesList() {
		return propertiesList;
	}

	public void setPropertiesList(List<Property> propertiesList) {
		this.propertiesList = propertiesList;
	}

	public List<Mapping> getMappingList() {
		return mappingList;
	}

	public void setMappingList(List<Mapping> mappingList) {
		this.mappingList = mappingList;
	}

	public Integer getIdC() {
		return idC;
	}

	public void setIdC(Integer idC) {
		this.idC = idC;
	}

	
}
