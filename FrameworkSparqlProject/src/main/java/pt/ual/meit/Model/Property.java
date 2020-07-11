package pt.ual.meit.Model;

public class Property {
	private Integer id;
	private String name;
	private String flgType;
	private Classes classe;
	private boolean flgPathExp;
	
	/**
	 * 
	 */
	public Property() {
		super();
		// TODO Auto-generated constructor stub
	}

	/**
	 * @param name
	 */
	public Property(String name) {
		super();
		this.name = name;
	}

	/**
	 * @return the name
	 */
	public String getName() {
		return name;
	}

	/**
	 * @param name
	 *            the name to set
	 */
	public void setName(String name) {
		this.name = name;
	}

	public String getFlgType() {
		return flgType;
	}

	public void setFlgType(String flgType) {
		this.flgType = flgType;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public Classes getClasse() {
		return classe;
	}

	public void setClasse(Classes classe) {
		this.classe = classe;
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

	
	
}
