package pt.ual.meit.Model;

import org.springframework.stereotype.Component;

@Component
public class MappingConfiguration {
	private MappingConfigurationOntology ontologyS;
	private MappingConfigurationOntology ontologyT;

	public MappingConfiguration() {
		super();
	}

	public MappingConfiguration(MappingConfigurationOntology ontologyS, MappingConfigurationOntology ontologyT) {
		super();
		this.ontologyS = ontologyS;
		this.ontologyT = ontologyT;
	}

	public MappingConfigurationOntology getOntologyS() {
		return ontologyS;
	}

	public void setOntologyS(MappingConfigurationOntology ontologyS) {
		this.ontologyS = ontologyS;
	}

	public MappingConfigurationOntology getOntologyT() {
		return ontologyT;
	}

	public void setOntologyT(MappingConfigurationOntology ontologyT) {
		this.ontologyT = ontologyT;
	}
}
