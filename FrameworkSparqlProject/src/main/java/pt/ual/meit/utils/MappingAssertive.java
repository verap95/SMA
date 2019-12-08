package pt.ual.meit.utils;

public class MappingAssertive {

	public MappingAssertive() {
	}

	public static String createAssertiveMappingClass(String source, String target) {
		
		String s = target + " ≡ " + source;
		return s;
	}
	
	public static String addPropertySourceToAssertive(String assertive, String prop) {
		String s = assertive + " / " + prop;
		return s;
	}

	public static String getBasicAssertive(String target) {
		String s = target + " ≡ null";
		return s;
	}
	
	public static String addFilterToAssertive(String assertive, String filter, String typeS, String typeT) {
		String s = null;
		if(typeS.equals("C")) {
			s = assertive + " / " + filter;
		}
		
		return s;
	}
	
	public static String createAssertiveMappingProperties(String cS, String pS, String cT, String pT) {
		String s = cT + " / " + pT + " ≡ " + cS + " / " + pS;
		return s;
	}
	
	public static String createAssertiveMapClassProperty(String cS, String pS, String cT) {
		String s = cT + " ≡ " + cS + " [ " + pS + " ]";
		return s;
	}
	
	public static String createN1PropertyMapping(String cT, String pT, String cS, String p1S, String p2S) {
		String s = cT + " / " + pT + " ≡ " + cS + "{ " + p1S + "," + p2S + "}";
		return s;
	}
}
