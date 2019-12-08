package pt.ual.meit.utils;

public class MappingRules {
	public MappingRules() {
	};

	public String createBasicMappingRule() {
		String s = "";
		return s;
	}
	
	//MC1 - Mapeamento de Classes
	public String createClassMappingRule(String target, String source) {
		String s = target + "(s) ← " + source + "(s)";
		return s;
	}
	
	//MC2 - Mapeamento de Classes
	public String createClassToPropertyMappingRule(String target, String source, String cSource) {
		String s = target + "(u) ← " + cSource +"(s); " + source + "(s,v) ; concat(s, xpath:encode-for-uri(v), u)";
		return s;
	}
	
	//MD1 - Mapeamento de Propriedades
	public String createPropertyMapping(String cSource, String pSource, String pTarget) {
		String s = pTarget + "(s,v) ← "+ cSource + "(s) ; " + pSource + "(s,v)";
		return s;
	}
	
	//MD2 - Mapeamento de Propriedades
	public String createN1PropertyMapping(String cSource, String p1Source, String p2Source, String function, String pTarget) {
		String s = pTarget + "(s,v) ← "+ cSource + "(s) ; " + p1Source + "(s,v1) ; " + p2Source + "(s,v2) ; " + function;
		return s;
	}
	
	//Adicionar filtros às regras de mapeamento
	public String addFilterToMapping(String prop, String filter) {
		String s = "";
		if(filter != null)
			s = " ; " + filter.replace(prop, prop + "(s,v)");
		return s;
	}
}
