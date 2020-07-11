package pt.ual.meit.Model;

import java.util.ArrayList;
import java.util.List;

public class NodesPublicObject {
	private Integer id;
	private String text;
	private String type;
	private String aBasic;
	private boolean flgPathExp;
	private Integer psPath;
	private List<AssertivePublicObject> listA = new ArrayList<>();
	private List<NodesPublicObject> nodes;

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public String getaBasic() {
		return aBasic;
	}

	public void setaBasic(String aBasic) {
		this.aBasic = aBasic;
	}

	public List<AssertivePublicObject> getListA() {
		return listA;
	}

	public void setListA(List<AssertivePublicObject> listA) {
		this.listA = listA;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public List<NodesPublicObject> getNodes() {
		return nodes;
	}

	public void setNodes(List<NodesPublicObject> nodes) {
		this.nodes = nodes;
	}

	/**
	 * @return the flgPathExp
	 */
	public boolean isFlgPathExp() {
		return flgPathExp;
	}

	/**
	 * @param flgPathExp the flgPathExp to set
	 */
	public void setFlgPathExp(boolean flgPathExp) {
		this.flgPathExp = flgPathExp;
	}

	/**
	 * @return the psPath
	 */
	public Integer getPsPath() {
		return psPath;
	}

	/**
	 * @param psPath the psPath to set
	 */
	public void setPsPath(Integer psPath) {
		this.psPath = psPath;
	}


	
}
