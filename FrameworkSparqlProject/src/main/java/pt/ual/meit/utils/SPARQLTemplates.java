package pt.ual.meit.utils;

public class SPARQLTemplates {

	private Prefixes prefix = new Prefixes();

	// Padrão MC1 - Mapeamento de Classes
	public String createClassMapping(String source, String target, String prop) {
		String[] pS = source.split(":");
		String[] pT = target.split(":");
		String s = "";
		if (prop != null) {
			String[] pP = prop.split(":");
			if (!pS[0].equals(pP[0]))
				s = s.concat("PREFIX " + pP[0] + ": <" + prefix.getPrefixes(pP[0]) + "> \n");
		}
		s = s.concat("PREFIX " + pS[0] + ": <" + prefix.getPrefixes(pS[0]) + "> \n" + 
				"PREFIX " + pT[0] + ": <" + prefix.getPrefixes(pT[0]) + "> \n" + 
				"CONSTRUCT { ?SUBJ a " + target + " . } \n" +
				"WHERE { ?SUBJ a " + source + " queryExp " + "}");

		return s;
	}

	// Padrão MC2 - Mapeamento de Classes
	public String createClassToPropertyMapping(String source, String target, String classeSource) {
		String[] pS = source.split(":");
		String[] pT = target.split(":");
		String[] pCS = classeSource.split(":");

		String s = "PREFIX " + pS[0] + ": <" + prefix.getPrefixes(pS[0]) + "> \n" + 
				"PREFIX " + pT[0] + ": <" + prefix.getPrefixes(pT[0]) + "> \n";

		if (!pS[0].equals(pCS[0]))
			s = s.concat("PREFIX " + pCS[0] + ": <" + prefix.getPrefixes(pCS[0]) + "> \n");

		s += "CONSTRUCT { ?generatedURI a " + target + "} \n" + 
				"WHERE { ?SUBJ a " + classeSource + " queryExp \n "
					+ "BIND( IRI(CONCAT(STR(?SUBJ), ENCODE_FOR_URI(?o))) AS ?generatedURI) }";

		return s;
	}

	// Padrão MD1/MO1 - Mapeamento de Propriedades
	public String createPropertyMapping(String source, String target, String classeSource, String propWhere) {
		String[] pS = source.split(":");
		String[] pT = target.split(":");
		String[] cS = classeSource.split(":");
		String s = "";
		if(!cS[0].equals(pS[0]))
			s += "PREFIX " + cS[0] + ": <" + prefix.getPrefixes(cS[0]) + "> \n";
		
		if(propWhere != null)
			s += "PREFIX " + propWhere + ": <" + prefix.getPrefixes(propWhere) + "> \n";
		
		s += "PREFIX " + pS[0] + ": <" + prefix.getPrefixes(pS[0]) + "> \n" + 
					"PREFIX " + pT[0] + ": <" + prefix.getPrefixes(pT[0]) + "> \n" + 
					"CONSTRUCT {?SUBJ " + target + " ?p } \n" + 
					"WHERE {?SUBJ queryExp functionExp" + "} \n";
		return s;
	}

	// Padrão MD2 - Mapeamento de Propriedades
	public String createN1PropertyMapping(String source1, String source2, String target) {
		String[] pS1 = source1.split(":");
		String[] pS2 = source2.split(":");
		String[] pT = target.split(":");

		String s = "PREFIX " + pS1[0] + ": <" + prefix.getPrefixes(pS1[0]) + "> \n";
		if (pS1[0] != pS2[0])
			s += "PREFIX " + pS2[0] + ": <" + prefix.getPrefixes(pS2[0]) + "> \n";

		s += "PREFIX " + pT[0] + ": <" + prefix.getPrefixes(pT[0]) + "> \n" + 
				"CONSTRUCT { ?SUBJ " + target + " ?o } \n" + 
				"WHERE { ?SUBJ " + source1 + " ?s ; " + source2 + " ?t queryExp functionExp " + "}";
		return s;
	}
	
	// Padrão MD3 - Mapeamento de Propriedades
	public String createEmbedPropertyMapping(String source, String target, String classeSource) {
		String[] pS = source.split(":");
		String[] pT = target.split(":");
		String[] pCS = classeSource.split(":");

		String s = "PREFIX " + pS[0] + ": <" + prefix.getPrefixes(pS[0]) + "> \n" + 
				"PREFIX " + pT[0] + ": <" + prefix.getPrefixes(pT[0]) + "> \n";

		if (!pS[0].equals(pCS[0]))
			s = s.concat("PREFIX " + pCS[0] + ": <" + prefix.getPrefixes(pCS[0]) + "> \n");

		s += "CONSTRUCT { ?generatedURI " + target + " ?p . } \n" + 
				"WHERE { ?SUBJ a " + classeSource + "; \n" +
					source + " ?p queryExp \n"
					+ "BIND( IRI(CONCAT(STR(?SUBJ), ENCODE_FOR_URI(?p))) AS ?generatedURI) }";

		return s;
	}
	
	// Padrão MO2 - Mapeamento de Propriedades
	public String createEmbedObjectPropertyMapping(String source, String target, String classeSource) {
		String[] pS = source.split(":");
		String[] pT = target.split(":");
		String[] pCS = classeSource.split(":");

		String s = "PREFIX " + pS[0] + ": <" + prefix.getPrefixes(pS[0]) + "> \n" + 
				"PREFIX " + pT[0] + ": <" + prefix.getPrefixes(pT[0]) + "> \n";

		if (!pS[0].equals(pCS[0]))
			s = s.concat("PREFIX " + pCS[0] + ": <" + prefix.getPrefixes(pCS[0]) + "> \n");

		s += "CONSTRUCT { ?SUBJ " + target + " ?generatedURI . } \n" + 
				"WHERE { ?SUBJ a " + classeSource + "; \n" +
					source + " ?p queryExp \n"
					+ "BIND( IRI(CONCAT(STR(?SUBJ), ENCODE_FOR_URI(?p))) AS ?generatedURI) }";

		return s;
	}
	
	// Adding Filters to existing mapping
	public String[] addFilterToMapping(String filter, String mapSPARQL, String typeS, String typeT, String queryExp) {
		String f = " FILTER(" + filter + ")";
		String s = mapSPARQL.replace("queryExp",  queryExp + f);
		String[] finalF = new String [] {s, f};
		return finalF;
	}
	
	// Adicionar funções de transformação a um mapeamento existente
	public String addFunctionToMapping(String mapSPARQL, String function) {
		String s;
		s = mapSPARQL.replace("functionExp", "BIND(CONCAT(" + function +") AS ?o)");
		return s;
	}
	
	public String setQueryExp(Integer pattern, String valuePropS,
			String domainWhereClause, String domainFilterClause, String source) {
		String s;
		
		switch(pattern) {
			case 1: //Mapeamento de Classes (MC1 e MC2)
				if (valuePropS == null)
					s = ".";
				else //Se possuir filtro
					s = "; " + valuePropS + " ?o .";
				break;
			case 2: //Mapeamento de Propriedades de Tipos de Dados (MD1 e MD3)
				domainWhereClause = domainWhereClause.replace(".", "; ");
				s = domainWhereClause + source + " ?p ." + domainFilterClause;
				break;
			case 3: //Mapeamento de Propriedades de Tipos de Dados (MD2)
				s = "";
				break;
			case 4: //Mapeamento de Propriedades de Objetos (MO1 e MO2)
				s = "";
				break;
			default:
				s = ".";
				break;
		}
		return s;
	}
	
}
