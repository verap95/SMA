package pt.ual.meit.utils;

import java.util.ArrayList;
import java.util.List;


public final class Constants {

	/*
	 * Mapeamentos de Classes
	*/
	public static final String PADRAO_MC1 = "# Template T1 - Mapeamento de Classes";
	public static final String PADRAO_MC2 = "# Template T2 - Mapeamento de Classes";

	/*
	 * Mapeamentos de Propriedades de Tipo de Dados
	*/
	public static final String PADRAO_MD1 = "# Template T6 – Mapeamento de Propriedade";
	public static final String PADRAO_MD2 = "# Template T8 – Mapeamento de Propriedade";
	public static final String PADRAO_MD3 = "# Template T4 – Mapeamento de Propriedade";

	/*
	 *  Mapeamentos de Propriedades de Objeto
	*/
	public static final String PADRAO_MO1 = "# Template T3 – Mapeamento de Propriedade";
	public static final String PADRAO_MO2 = "# Template T5 – Mapeamento de Propriedade";

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
	public static List<String> getListFunctionString() {
		List<String> listFunction = new ArrayList<>();
		listFunction.add("CONCAT");
		listFunction.add("LCASE");
		listFunction.add("REPLACE");
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