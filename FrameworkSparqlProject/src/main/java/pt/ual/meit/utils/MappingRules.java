package pt.ual.meit.utils;

public class MappingRules {
	public MappingRules() {
	};

	public String createBasicMappingRule() {
		String s = "";
		return s;
	}
	
	//Mapeamento de Classes (Padrão MC1)
	public String createMC1MappingRule(String target, String source, String propSource, String filter) {
		String s = "";
		if(filter != null) {
			s = target + "(s) ← " + source + "(s) ; " + filter.replace(propSource, propSource + "(s,v)");
		}else {
			s = target + "(s) ← " + source + "(s)";
		}
		return s;
	}
	
	//Mapeamento de Classes (Padrão MC2)
	public String createMC2MappingRule(String target, String source, String cSource, String propSource, String filter) {
		String s = "";
		if(filter != null) {
			s = target + "(u) ← " + cSource +"(s); generateUri[ψ](s,u) ; " + filter.replace(propSource, propSource + "(s,v)");
		}else {
			s = target + "(u) ← " + cSource +"(s); generateUri[ψ](s,u)";
		}
		return s;
	}
	
	//Mapeamento de Propriedades de Tipos de Dados (Padrão MD1 e MO1)
	public String createMD1_MO1MappingRule(String cSource, String pSource, String pTarget, 
			String path, String propSource, String filter, String function) {
		String s = pTarget + "(s,v) ← "+ cSource + "(s) ; ";
		if(path != null ) {
			s = s.concat(path + " / ");
		}
		if(function != null) {
			s = s.concat(pSource + "(s,x)");
		}else {
			s = s.concat(pSource);
			if(filter != null) {
				s = s.concat("(s,t)");
			}else
				s = s.concat("(s,v)");
		}
		if(filter != null) {
			s = s.concat(" ; " + filter.replace(propSource, propSource + "(t,v)"));
		}
		if(function != null) {
			s = s.concat( "; " + function + "(x,v)");
		}
		return s;
	}
	
	//MD2 - Mapeamento de Propriedades
	public String createN1PropertyMapping(String cSource, String p1Source, String p2Source, String function, String pTarget, String path, String oldFunction) {
		String s = pTarget + "(s,v) ← "+ cSource + "(s) ; ";
		if(path != null) {
			s = s.concat(path + " / ");
		}
		if(oldFunction.contains("))")) {
			oldFunction = oldFunction.replace("))", ",v)");
		}else
			oldFunction = oldFunction.replace(p2Source, p2Source + ",v");
		
		oldFunction = oldFunction.replace(p1Source, "v1");
		oldFunction = oldFunction.replace(p2Source, "v2");
		
		if(function != null)
			s = s.concat(p1Source + "(s,v1) ; " + p2Source + "(s,v2) ; " + oldFunction + " ; "+ function);
		else 
			s = s.concat(p1Source + "(s,v1) ; " + p2Source + "(s,v2) ; " + oldFunction) ;
		
		return s;
	}
	
	// Mapeamento de Propriedades de Tipos de Dados (Padrão MD3)
	public String createMD3mappingRule(String cSource, String pSource, String filter, String pTarget) {
		String s = "";
		if(filter != null) {
			s = pTarget + "(u,v) ← " + cSource +"(s); " + filter + "; " + pSource + "(s,v) ; generateUri[ψ](s,u)";
		}else {
			s = pTarget + "(u,v) ← " + cSource +"(s);" + pSource + "(s,v) ; generateUri[ψ](s,u)";
		}
		return s;
	}
	
	// Mapeamento de Propriedades de Tipos de Dados (Padrão MO2)
	public String createMO2mappingRule(String cSource, String filter, String pTarget) {
		String s = "";
		if(filter != null) {
			s = pTarget + "(s,u) ← " + cSource +"(s); " + filter + " ; generateUri[ψ](s,u)";
		}else {
			s = pTarget + "(s,u) ← " + cSource +"(s); generateUri[ψ](s,u)";
		}
		return s;
	}
		
	//Adicionar filtros às regras de mapeamento
	public String addFilterToMapping(String prop, String filter) {
		String s = "";
		if(filter != null)
			s = " ; " + filter.replace(prop, prop + "(s,v)");
		return s;
	}
	
	public String addFunctionToMapping(String function) {
		String s="";
		if(function != null) {
			s = "; " + function + "(x,v)";
		}
		return s;
	}
}
