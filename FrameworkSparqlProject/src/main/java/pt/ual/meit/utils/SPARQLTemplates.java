package pt.ual.meit.utils;

import java.util.List;

import pt.ual.meit.Model.TempAssertive;

public class SPARQLTemplates {

	private Prefixes prefix = new Prefixes();
	
	/**
	 * Método utilizado para obter os prefixos presentes na AM
	 * @param pattern - Integer que represnta qual o padrão da AM
	 * @param am - Objeto do tipo TempAssertive que representa uma AM definida pelo utilizador
	 * @param sourceComp - String que representa a classe da propriedade fonte numa AMD/AMO
	 * @param propComp - Lista que representa as várias propriedades existentes numa AM
	 * @return
	 */
	public String getPrefixes(Integer pattern, TempAssertive am, String sourceComp, String propComp, String propRangeClass) {
		String[] pS = am.getNameS().split(":");
		String[] pT = am.getNameT().split(":");
		String prefixExp = pS[0]  + ": <" + prefix.getPrefixes(pS[0]) + "> \n"
				+ "PREFIX " + pT[0] + ": <" + prefix.getPrefixes(pT[0]) + "> \n";
		switch (pattern) {
		case 1: //Template T1 - Mapeamento de Classes (Padrão MC1)
			if(am.getValuePropS() != null) { //Se existir uma propriedade
				String[] pP = am.getValuePropS().split(":");
				if (!pS[0].equals(pP[0])) {
					prefixExp = prefixExp.concat("PREFIX " + pP[0] + ": <" + prefix.getPrefixes(pP[0]) + "> \n");
				}
			}
			break;
		case 2: // Template T2 - Mapeamento de Classes (Padrão MC2)
			String[] propS2 = propComp.split(":");
			if (!pS[0].equals(propS2[0])) {
				prefixExp = prefixExp.concat("PREFIX " + propS2[0] + ": <" + prefix.getPrefixes(propS2[0]) + "> \n");
			}
			for(String prop : am.getListProps()) { // Para funcionar para várias propriedades
				String[] p = prop.split(":");
				if (!pS[0].equals(p[0])) {
					prefixExp = prefixExp.concat("PREFIX " + p[0] + ": <" + prefix.getPrefixes(p[0]) + "> \n");
				}
			}
			break;
		case 3: // Template T3 - Mapeamento de Propriedade de Tipo de Dados / Objeto (Padrão MD1 / MO1)
		case 4: // Template T4 - Mapeamento de Propriedade de Tipo de Dados (Padrão MD3)
		case 5: // Template T5 - Mapeamento de Propriedade de Objetos (Padrão MO2)
		case 6: // Template T6 - Mapeamento de Propriedade de Tipo de Dados / Objeto (Padrão MD1 / MO1)
		case 7: // Template T7 - Mapeamento de Propriedade de Tipo de Dados (Padrão MD2)
		case 8: // Template T8 - Mapeamento de Propriedade de Tipo de Dados (Padrão MD2)
			String[] cS = sourceComp.split(":");
			if(!cS[0].equals(pS[0]))
				prefixExp = prefixExp.concat("PREFIX " + cS[0] + ": <" + prefix.getPrefixes(cS[0]) + "> \n");
			if(propComp != null) { //Falta adicionar as várias propriedades, neste momento apenas funciona para uma (2 no caso do Padrão MD2)
				prefixExp = prefixExp.concat("PREFIX " + propComp + ": <" + prefix.getPrefixes(propComp) + "> \n");
			}
			if(propRangeClass != null) { // Apenas serve para o Padrão MO2
				prefixExp = prefixExp.concat("PREFIX " + propRangeClass + ": <" + prefix.getPrefixes(propRangeClass) + "> \n");
			}
		default:
			break;
		}
		
		return prefixExp;
	}
	
	public String[] getFilterExp(String queryExp, String filter) {
		String f = "\n FILTER(" + filter + ")";
		String s = queryExp + f;
		String[] finalF = new String [] {s, f};
		return finalF;
	}
	
	/**
	 * Obtém a função de transformação T presente na AM
	 * @param function
	 * @return
	 */
	public String getFunctionExp(String function) {
		String f  = "";
		if(function != null) { 
			f = "BIND("+ function +" AS ?p)";
		}
		return f;
	}
	
	public String[] getUriExp(List<String> props, String listPropsSources) {
		String prop = "", temp = "", t="";
		if(props.size() == 1) {
			if(listPropsSources != null && !listPropsSources.isEmpty()) {
				String[] sources = listPropsSources.split(",");
				for(int i=0; i< sources.length; i++) {
					if(!t.isEmpty()) {
						t = t.concat(",");
					}
					t = t.concat("?A" + (i+1));
					
					if(sources[i].equals(props.get(0))) {
						temp = "?A" + (i+1);
					}
					if((i+1) == sources.length) 
						prop = t;
					else
						prop = "CONCAT("+t+")";
				}
			}else {
				prop = "?A1";
				temp=prop;
			}
			
		}else{
			for(int i=0; i< props.size(); i++) {
				if(!temp.isEmpty()) {
					temp = temp.concat(",");
				}
				temp = temp.concat("?A" + (i+1));
			}
			prop = "CONCAT("+temp+")";
			
		}
//		String[] listP = props.split(",");
//		String p = null;
//		String temp = null;
//		if(listP.length >1) {
//			for(int i=0; i< listP.length; i++) {
//				if(!temp.isEmpty()) {
//					temp = temp.concat(",");
//				}
//				temp = temp.concat("?A" + i);
//			}
//			p = "CONCAT("+temp+")";
//		}else {
//			p = "?A1";
//		}
		
		String [] result = {temp, "ENCODE_FOR_URI(" + prop + ")"};
		return result;
	}
	
	// Padrão MC1 - Mapeamento de Classes (Template T1)
	public String createClassMapping(String prefixExp, String queryExp, String Cs, String Ct) {
		String t1 = Constants.TemplateT1.replace("Ct", Ct);
		t1 = t1.replace("Cs", Cs);
		t1 = t1.replace("prefixExp", prefixExp);
		t1 = t1.replace("queryExp", queryExp);
		return t1;
	}

	// Padrão MC2 - Mapeamento de Classes (Template T2)
	public String createClassToPropertyMapping(String prefixExp, String queryExp, String uriExp, String Ct, String Cs) {
		String t2 = Constants.TemplateT2.replace("Ct", Ct);
		t2 = t2.replace("Cs", Cs);
		t2 = t2.replace("prefixExp", prefixExp);
		t2 = t2.replace("queryExp", queryExp);
		t2 = t2.replace("uriExp", uriExp);
		return t2;
	}

	// Padrão MD1/MO1 - Mapeamento de Propriedades
	public String createPropertyMapping(String classeSource, String propTarget, String prefixExp, String queryExp, String functionExp) {
		String t;
		if(functionExp != null && !functionExp.isEmpty()) {
			t = Constants.TemplateT6.replace("functionExp", functionExp);
		}else {
			t = Constants.TemplateT3;
		}
		t = t.replace("Cs", classeSource);
		t = t.replace("Pt", propTarget);
		t = t.replace("prefixExp", prefixExp);
		t = t.replace("queryExp", queryExp);
		return t;
	}
		
	// Padrão MD2 - Mapeamento de Propriedades
	public String createN1PropertyMapping(String prefixExp, String Pt, String Cs, String Ps, String queryExp, String functionExp) {
		String template; 
		if(functionExp == null) {
			template = Constants.TemplateT7;
		}else {
			template = Constants.TemplateT8;
			template = template.replace("functionExp", functionExp);
		}
		template = template.replace("prefixExp", prefixExp);
		template = template.replace("Pt", Pt);
		template = template.replace("Cs", Cs);
		template = template.replace("Ps", Ps);
		template = template.replace("queryExp", queryExp);
		return template;
	}
	
	// Padrão MD3 - Mapeamento de Propriedades
	public String createEmbedPropertyMapping(String Cs, String target, String prefixExp, String queryExp, String uriExp, String valuePt) {
		String t4 = Constants.TemplateT4.replace("prefixExp", prefixExp);
		t4 = t4.replace("?Ai", valuePt);
		t4 = t4.replace("Pt", target);
		t4 = t4.replace("Cs", Cs);
		t4 = t4.replace("queryExp", queryExp);
		t4 = t4.replace("uriExp", uriExp);
		return t4;
	}
	
	// Padrão MO2 - Mapeamento de Propriedades
	public String createEmbedObjectPropertyMapping(String Cs, String Pt, String prefixExp, String queryExp, String uriExp) {
		String t5 = Constants.TemplateT5.replace("prefixExp", prefixExp);
		t5 = t5.replace("Pt", Pt);
		t5 = t5.replace("Cs", Cs);
		t5 = t5.replace("queryExp", queryExp);
		t5 = t5.replace("uriExp", uriExp);
		
		return t5;
	}
	
	// Adding Filters to queryExp
	public String[] addFilter(String filter, String mapSPARQL, String typeS, String typeT, String queryExp) {
		String f = " FILTER(" + filter + ")";
		String s = mapSPARQL.replace("queryExp",  queryExp + f);
		String[] finalF = new String [] {s, f};
		return finalF;
	}
	
	// Adicionar funções de transformação a um mapeamento existente
	public String addFunctionToMapping(String mapSPARQL, String function) {
		String s;
		s = mapSPARQL.replace("functionExp", "BIND(" + function +") AS ?o)");
		return s;
	}
	
	// Carregar a informação necessária da variável queryExp
	public String setQueryExp(Integer pattern, String valuePropS,
			String domainWhereClause, String domainFilterClause, String source, 
			Boolean flgOP2, Boolean flgFunction, String rangeWhereClause, String rangeFilterClause, 
			List<String> listProps, String psPath) {
		String s = "";
		
		switch(pattern) {
			case 1: //Template T1 - Mapeamento de Classes (MC1)
				if (valuePropS == null)
					s = ".";
				else //Se possuir filtro
					s = "; " + valuePropS + " ?o .";
				break;
			case 2: //Template T2 - Mapeamento de Classes (MC2)
				for(int i=0; i < listProps.size(); i++) {
					s = s + " ; " + listProps.get(i) + " ?A"+(i+1) ;
					if(listProps.size() == (i+1))
						s = s + ".";
				}
				break;	
			case 4: //Template T4 - Mapeamento de Propriedades de Tipo de Dados (Padrão MD3)
				domainWhereClause = domainWhereClause.replaceFirst("; ", "");
				if(domainFilterClause != null) {
					s = domainWhereClause + domainFilterClause;
				}else {
					s = domainWhereClause;
				}
				break;
			case 5: //Template T5 - Mapeamento de Propriedades de Tipos de Dados (Padrão MO2)
				domainWhereClause = domainWhereClause.replaceFirst("; ", "");
				domainWhereClause = domainWhereClause.replace(".", "");
				s = domainWhereClause + rangeWhereClause;
				
				if(domainFilterClause != null) {
					s = s.concat(domainFilterClause);
				}
				
				if(rangeFilterClause != null) {
					s = s.concat(rangeFilterClause);
				}
				
				break;
			case 6: //Mapeamento de Propriedades de Tipos de Dados (MD1 e MD3)
				if(domainFilterClause == null) {
					if(domainWhereClause.contains(source)) {
						domainWhereClause = domainWhereClause.replace("?o", "?p");
						s = domainWhereClause.replace(";", "");
					}else {
						if(psPath != null && domainWhereClause.equals(".")) {
							s = psPath + " ?a . " + " ?a " + source;
							if(flgFunction)
								s = s + " ?s .";
							else
								s = s + " ?p .";
						}else if (psPath != null && !domainWhereClause.equals(".")) {
							domainWhereClause = domainWhereClause.replace(".","; ");
							s = domainWhereClause + psPath + " ?a . ?a " + source;
						}else {
							if(flgFunction)
								s = source + " ?s " + domainWhereClause;
							else
								s =  source + " ?p" + domainWhereClause;
						}
					}
				}else {
					if(domainWhereClause.contains(source)){
						domainWhereClause = domainWhereClause.replace("?o", "?p");
						s = domainWhereClause + domainFilterClause;
					}else {
						domainWhereClause = domainWhereClause.replace(";","");
						domainWhereClause = domainWhereClause.replace(".","; ");
						if(psPath != null)
							s = domainWhereClause + psPath + " ?a . ?a " + source;
						else
							s = domainWhereClause + source;
						if(flgFunction)
							s = s + " ?s ."+ domainFilterClause;
						else
							s = s + " ?p ."+ domainFilterClause;
					}
				}
				
				break;
			case 7: //Template T7 - Mapeamento de Propriedades de Tipos de Dados (Padrão MD2)
			case 8: //Template T8 - Mapeamento de Propriedades de Tipos de Dados (Padrão MD2)
				domainWhereClause = domainWhereClause.replace(".", "");
				if(domainWhereClause.contains(";"))
					domainWhereClause = domainWhereClause.replaceFirst(";", "");
				if(!domainWhereClause.isEmpty())
					s = domainWhereClause + ";";
				if(psPath != null) {
					s = s + psPath + "?a. ?a " + valuePropS + " ?s ";
					if(flgOP2){
						s = s + ". OPTIONAL { ?a "  + source + " ?t } ."; 
					}else {
						s = s + source + " ?t .";
					}
				}else {
					if(flgOP2)
						s = s + valuePropS + " ?s . OPTIONAL { ?SUBJ " + source + " ?t } .";
					else
						s = s + valuePropS + " ?s ; " + source + " ?t .";
				}

				if(domainFilterClause != null) {
					s = s + domainFilterClause + ". ";
				}				
				break;
			case 3: //Mapeamento de Propriedades de Objetos (MO1 e MO2)
				if(domainFilterClause == null) {
					if(domainWhereClause.contains(source)) {
						domainWhereClause = domainWhereClause.replace("?o", "?p");
						s = domainWhereClause;
					}else {
						domainWhereClause = domainWhereClause.replace(".", "; ");
						s = domainWhereClause + source + " ?p .";
					}
				}else {
					if(domainWhereClause.contains(source)){
						domainWhereClause = domainWhereClause.replace("?o", "?p");
						s = domainWhereClause + domainFilterClause;
					}else {
						domainWhereClause = domainWhereClause.replace(".", "; ");
						s = domainWhereClause + source + " ?p ." + domainFilterClause;
					}
				}
				break;
			default:
				s = ".";
				break;
		}
		return s;
	}
	
}
