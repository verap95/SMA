package pt.ual.meit.Model;

import java.util.ArrayList;
import java.util.List;

public class NodesPublicObject {
	private Integer id;
	private String text;
	private String type;
	private String aBasic;
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


	
}
