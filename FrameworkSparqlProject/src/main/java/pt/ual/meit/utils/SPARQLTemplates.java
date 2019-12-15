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
				"WHERE {?SUBJ a " + source + " queryExp " + "}");

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
				"WHERE { ?SUBJ a " + classeSource + "; \n" +
					source + " ?p queryExp \n "
					+ "BIND( IRI(CONCAT(STR(?SUBJ), ENCODE_FOR_URI(?p))) AS ?generatedURI) }";

		return s;
	}

	// Padrão MD1/MO1 - Mapeamento de Propriedades
	public String createPropertyMapping(String source, String target) {
		String[] pS = source.split(":");
		String[] pT = target.split(":");
		String s = "PREFIX " + pS[0] + ": <" + prefix.getPrefixes(pS[0]) + "> \n" + 
					"PREFIX " + pT[0] + ": <" + prefix.getPrefixes(pT[0]) + "> \n" + 
					"CONSTRUCT {?SUBJ " + target + " ?o } \n" + 
					"WHERE {?SUBJ " + source + " ?o " + " queryExp functionExp" + "} \n";
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
		
	// Adding Filters to existing mapping
	public String addFilterToMapping(String filter, String mapSPARQL, String typeS, String typeT, String valuePropS) {
		String f = " FILTER(" + filter + ")";
		String s = null;
		String finalS = null;
		String a = "";
		if (typeS.equals("C")) {
			if (valuePropS == null)
				a = ".";
			else
				a = "; " + valuePropS + " ?o .";
			s = mapSPARQL.replace("queryExp", a + f);
			System.out.println("Replace map: " + s);
		}
		return s;
	}
	
	// Adicionar funções de transformação a um mapeamento existente
	public String addFunctionToMapping(String mapSPARQL, String function) {
		String s;
		s = mapSPARQL.replace("functionExp", "BIND(CONCAT(" + function +") AS ?o)");
		return s;
	}
}
