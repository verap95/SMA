package pt.ual.meit.Model;

public class FinalConfig {
	private String languageInput;
	private String languageOutput;
	private String RDFXML;
	private String TURTLE;
	private String N3;
	private String NTRIPLES;
	
	public FinalConfig() {
		super();
	}
	public String getRDFXML() {
		return RDFXML;
	}
	public void setRDFXML(String rDFXML) {
		RDFXML = rDFXML;
	}
	public String getTURTLE() {
		return TURTLE;
	}
	public void setTURTLE(String tURTLE) {
		TURTLE = tURTLE;
	}
	public String getN3() {
		return N3;
	}
	public void setN3(String n3) {
		N3 = n3;
	}
	public String getNTRIPLES() {
		return NTRIPLES;
	}
	public void setNTRIPLES(String nTRIPLES) {
		NTRIPLES = nTRIPLES;
	}
	public String getLanguageInput() {
		return languageInput;
	}
	public void setLanguageInput(String languageInput) {
		this.languageInput = languageInput;
	}
	public String getLanguageOutput() {
		return languageOutput;
	}
	public void setLanguageOutput(String languageOutput) {
		this.languageOutput = languageOutput;
	}
	
	
}
