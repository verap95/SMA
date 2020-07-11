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
		String s = cT + " ≡ " + cS + " [ " + props + " ]";
		return s;
	}
	
	public static String updateAssertiveMappingClassProperty(String assertive, String prop) {
		String props = assertive.substring(assertive.indexOf("[")+1, assertive.indexOf("]")-1);
		String temp = props + " , " + prop;		
		return assertive.replace(props, temp);
	}
	
	//MD1/MO1 - Mapeamento de Propriedades
	public static String createAssertiveMappingProperties(String cS, String pS, String cT, String pT, boolean flgPathExp, String pSPath) {
		String s;
		if(flgPathExp)
			s = cT + " / " + pT + " ≡ " + cS + " / [" + pSPath +"/" + pS + "]";
		else
			s = cT + " / " + pT + " ≡ " + cS + " /" + pS;
		return s;
	}
	
	//MD2 - Mapeamento de Propriedades
	public static String createN1PropertyMapping(String cT, String pT, String cS, String p1S, String p2S, boolean flgPathExp, String pSPath) {
		String s;
		if(flgPathExp)
			s = cT + " / " + pT + " ≡ " + cS + " [" + pSPath + "/{ " + p1S + "," + p2S + "}]";
		else
			s = cT + " / " + pT + " ≡ " + cS + " { " + p1S + "," + p2S + "}";
		return s;
	}
	
	//MD3 - Mapeamento de Propriedades
	public static String createEmbedPropertyMapping(String cT, String pT, String cS, String listP, String pS) {
		String s = cT + " / " + pT + " ≡ " + cS + " [" + listP + "] / " + pS;
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
