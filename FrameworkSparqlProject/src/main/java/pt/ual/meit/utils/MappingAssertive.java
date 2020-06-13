package pt.ual.meit.utils;

public class MappingAssertive {

	public MappingAssertive() {
	}
	
	//Adicionar propriedade da ontologia fonte à assertiva
	public static String addPropertySourceToAssertive(String assertive, String prop) {
		String s = assertive + " / " + prop;
		return s;
	}

	//Assertiva básica
	public static String getBasicAssertive(String target) {
		String s = target + " ≡ null";
		return s;
	}

	//MC1 - Mapeamento de Classes
	public static String createAssertiveMappingClass(String source, String target) {
		String s = target + " ≡ " + source;
		return s;
	}

	//MC2 - Mapeamento de Classes
	public static String createAssertiveMapClassProperty(String cS, String props, String cT) {
		//Falta adicionar as várias propriedades, apenas funciona para uma
		String s = cT + " ≡ " + cS + " [ " + props + " ]";
		return s;
	}
	
	//MD1/MO1 - Mapeamento de Propriedades
	public static String createAssertiveMappingProperties(String cS, String pS, String cT, String pT) {
		String s = cT + " / " + pT + " ≡ " + cS + " / " + pS;
		return s;
	}
	
	//MD2 - Mapeamento de Propriedades
	public static String createN1PropertyMapping(String cT, String pT, String cS, String p1S, String p2S) {
		String s = cT + " / " + pT + " ≡ " + cS + " { " + p1S + "," + p2S + "}";
		return s;
	}
	
	//MD3 - Mapeamento de Propriedades
	public static String createEmbedPropertyMapping(String cT, String pT, String cS, String pS) {
		String s = cT + " / " + pT + " ≡ " + cS + " [" + pS + "] / " + pS;
		return s;
	}
	
	//MO2 - Mapeamento de Propriedades
	public static String createEmbedObjectPropertyMapping(String cT, String pT, String cS, String pS) {
		String s = cT + " / " + pT + " ≡ " + cS + " [" + pS + "] / NULL";
		return s;
	}
	
	/**
	 * Adicionar filtro à assertiva
	 * @param assertive
	 * @param filter
	 * @param typeS
	 * @param typeT
	 * @return
	 */
	public static String addFilterToAssertive(String assertive, String filter, String typeS, String typeT) {
		String s = assertive + " / " + filter;
		return s;
	}
	
	/**
	 * Adicionar função à assertiva
	 * @param assertive
	 * @param filter
	 * @param typeS
	 * @param typeT
	 * @return
	 */
	public static String addFunctionToAssertive(String assertive, String function) {
		String s = null;
		for(String key: Constants.getListFunctionString().keySet()) {
			if(function.contains(key)) {
				function = function.replace(key, Constants.getListFunctionString().get(key));
			}
		}		
		s = assertive + " / " + function;
		
		return s;
	}
}
