package pt.ual.meit.Model;

import java.util.ArrayList;
import java.util.List;

public class PublicObject {
	private Integer id; 
	private String text;
	private String type;
	private String aBasic;

	private List<NodesPublicObject> nodes = new ArrayList<>();

	private List<AssertivePublicObject> listA = new ArrayList<>();

	public PublicObject() {
		super();
	}

	public String getText() {
		return text;
	}

	public void setText(String text) {
		this.text = text;
	}

	public List getNodes() {
		return nodes;
	}

	public void setNodes(List nodes) {
		this.nodes = nodes;
	}

	public String getType() {
		return type;
	}

	public void setType(String type) {
		this.type = type;
	}

	public List<AssertivePublicObject> getListA() {
		return listA;
	}

	public void setListA(List<AssertivePublicObject> listA) {
		this.listA = listA;
	}

	public String getaBasic() {
		return aBasic;
	}

	public void setaBasic(String aBasic) {
		this.aBasic = aBasic;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	
}
