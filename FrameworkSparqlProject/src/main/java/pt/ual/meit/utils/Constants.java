package pt.ual.meit.utils;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public final class Constants {

	/*
	 * Padrão MC1 - Mapeamento de Classes (Template T1)
	*/
	public static final String PADRAO_MC1 = "# Template T1 - Mapeamento de Classes";
	public static final String TemplateT1 = "PREFIX prefixExp" + 
											"CONSTRUCT { ?SUBJ a Ct . } \n" + 
											"WHERE { ?SUBJ a Cs queryExp }";
	/*
	 * Padrão MC2 - Mapeamento de Classes (Template T2)
	*/
	public static final String PADRAO_MC2 = "# Template T2 - Mapeamento de Classes";
	public static final String TemplateT2 = "PREFIX prefixExp" +
											"CONSTRUCT { ?generateURI a Ct . } \n" + 
											"WHERE { ?SUBJ a Cs queryExp \n" + 
											"BIND( IRI( CONCAT( STR(?SUBJ), uriExp) ) AS ?generateURI) }";

	/*
	 * Padrão MD1 - Mapeamento de Propriedades de Tipos de Dados (Template T6 e T3)
	*/
	public static final String PADRAO_MD1 = "# Template T6 – Mapeamento de Propriedade";
	public static final String TemplateT6 = "PREFIX prefixExp" +
											"CONSTRUCT { ?SUBJ Pt ?p . } \n"+
											"WHERE {?SUBJ a Cs ; queryExp \n" +
											"functionExp }";

	/*
	 * Padrão MD2 - Mapeamento de Propriedades de Tipos de Dados (Template T7 e T8)
	*/
	public static final String PADRAO_MD2_T7 = "# Template T7 – Mapeamento de Propriedade";
	public static final String TemplateT7 = "PREFIX prefixExp" + 
											"CONSTRUCT { ?SUBJ Pt ?p . } \n"+
											"WHERE {?SUBJ a Cs ; queryExp \n" +
											"BIND( CONCAT(Ps) AS ?p ) }";
	
	public static final String PADRAO_MD2_T8 = "# Template T8 – Mapeamento de Propriedade";
	public static final String TemplateT8 = "PREFIX prefixExp" + 
											"CONSTRUCT { ?SUBJ Pt ?v . } \n"+
											"WHERE {?SUBJ a Cs ; queryExp \n" +
											"BIND( CONCAT(Ps) AS ?p ) ."+
											"BIND( functionExp AS ?v )}";
	
	/*
	 * Padrão MD3 - Mapeamento de Propriedades de Tipos de Dados (Template T4)
	*/
	public static final String PADRAO_MD3 = "# Template T4 – Mapeamento de Propriedade";
	public static final String TemplateT4 = "PREFIX prefixExp" +
											"CONSTRUCT { ?generateURI Pt ?Ai . } \n"+
											"WHERE { ?SUBJ a Cs ; queryExp \n" + 
											"BIND( IRI( CONCAT( STR(?SUBJ), uriExp) ) AS ?generateURI) }";
	
	/*
	 * Padrão MO1 - Mapeamento de Propriedades de Objetos (Template T3)
	*/
	public static final String PADRAO_MO1 = "# Template T3 – Mapeamento de Propriedade";
	public static final String TemplateT3 = "PREFIX prefixExp" +
											"CONSTRUCT { ?SUBJ Pt ?p . } \n"+
											"WHERE { ?SUBJ a Cs ; queryExp }";
	/*
	 * Padrão MO2 - Mapeamento de Propriedades de Objetos (Template T5)
	*/
	public static final String PADRAO_MO2 = "# Template T5 – Mapeamento de Propriedade";
	public static final String TemplateT5 = "PREFIX prefixExp" +
											"CONSTRUCT { ?SUBJ Pt ?generateURI . } \n" + 
											"WHERE { ?SUBJ a Cs ; queryExp \n" + 
											"BIND( IRI( CONCAT( STR(?SUBJ), uriExp)) AS ?generateURI) }";

	
	/*
	 * Método auxiliar que permite obter o nome do Atributo com respetivo prefixo 
	*/
	public static final String ATRIBUTOS(String prefix, String name) {
		String nomeClasse = prefix + ":" + name;
		return nomeClasse;
	}

	/*
	 * Método auxiliar que permite obter o nome do Ficheiro
	 */
	public static final String nameFile(String dominio, String nameS, String nameT, String ext) {
		String nameFile = dominio + "_" + nameS + "_to_" + nameT + ext;
		return nameFile;
	}

	/*
	 *  Método auxiliar que permite obter a Lista de Funções String
	 */
	public static HashMap<String, String> getListFunctionString() {
		HashMap<String, String> listFunction = new HashMap<String, String>();
		listFunction.put("CONCAT", "concat");
		listFunction.put("LCASE", "lower-case");
		return listFunction;
	}
	
	/*
	 * Método auxiliar que permite obter a Lista de Funções do Tipo Data
	*/
	public static List<String> getListFunctionData() {
		List<String> listFunction = new ArrayList<>();
		listFunction.add("NOW");
		listFunction.add("YEAR");
		
		return listFunction;
	}
}