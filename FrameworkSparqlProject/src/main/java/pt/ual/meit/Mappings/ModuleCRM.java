package pt.ual.meit.Mappings;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import pt.ual.meit.JPA.Mapeamento;
import pt.ual.meit.JPA.Propriedades;
import pt.ual.meit.Model.TempAssertive;
import pt.ual.meit.Service.MappingService;
import pt.ual.meit.Service.PropriedadesService;
import pt.ual.meit.utils.Constants;
import pt.ual.meit.utils.MappingRules;

@Component
public class ModuleCRM {

	private static MappingRules mp = new MappingRules();

	@Autowired
	private MappingService mapService;

	@Autowired
	private PropriedadesService propService;

	/**
	 * Método que permite retornar uma regra de mapeamento consoante os parâmetros de entrada
	 * 
	 * @param input - Objeto que contém todos os dados relativos a um mapeamento criado pelo utilizador.
	 * @param classeSource - String que corresponde à Classe a que pertence a propriedade da Ontologia Fonte (Apenas serve para os padrões de propriedades)
	 * @param flgProp
	 * @return mapRule - String que contém a regra de mapeamento correspondente
	 */
	public String saveMappingRule(TempAssertive input, String classeSource, Boolean flgProp, Boolean flgMPC) {
		String mapRule = null;
		if (input.getTypeT().equals("C") && input.getTypeS().equals("C")) {
			mapRule = mp.createMC1MappingRule(input.getNameT(), input.getNameS(), input.getValuePropS(), input.getFilter());
		} else if (input.getTypeT().equals("C") && !input.getTypeS().equals("C")) {
			mapRule = mp.createMC2MappingRule(input.getNameT(), input.getNameS(), classeSource, input.getValuePropS(), input.getFilter());
		} else if (input.getTypeT().equals("D") && input.getTypeS().equals("D")) {
			//Obter a função de transformação para colocar na regra de Mapeamento
			String functionValue = null;
			if(input.getFuncValue() != null) {
				for(String key : Constants.getListFunctionString().keySet()) {
					if(input.getFuncValue().contains(key)) {
						functionValue = Constants.getListFunctionString().get(key);
					}
				}
			}
			if(flgProp) { //Padrão MD2
				Propriedades pS = propService.findById(input.getP1S());
				Propriedades pS1 = null;
				if(input.getpSPath() != null)
					pS1 = propService.findById(input.getpSPath());	
				
				mapRule = mp.createN1PropertyMapping(classeSource, 
							Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()), input.getNameS(), 
							input.getFuncValue(),input.getNameT(), pS1 != null ? Constants.ATRIBUTOS(pS1.getPrefix(), pS1.getName()) : null);
			}else if(flgMPC)//Padrão MD3
				mapRule = mp.createMD3mappingRule(classeSource, input.getNameS(), input.getFilter(), input.getNameT());
			else { //Padrão MD1
				Propriedades pS = null;
				if(input.getpSPath() != null)
					pS = propService.findById(input.getpSPath());	
				mapRule = mp.createMD1_MO1MappingRule(classeSource, input.getNameS(), input.getNameT(), pS != null ? Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()) : null, input.getValuePropS(), input.getFilter(), functionValue);			
			}
		} else if (input.getTypeT().equals("O") || input.getTypeS().equals("O")) {
			if(flgMPC) //Padrão MO2
				mapRule = mp.createMO2mappingRule(classeSource, input.getFilter(), input.getNameT());
			else { //Padrão MO1
				Propriedades pS = null;
				if(input.getpSPath() != null)
					propService.findById(input.getpSPath());	
				mapRule = mp.createMD1_MO1MappingRule(classeSource, input.getNameS(), input.getNameT(), pS != null ? Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()) : null, input.getValuePropS(), input.getFilter(), null);
			}
		}
		
		return mapRule;
	}

	/**
	 * Método para exportar uma lista com todas as regras de mapeamento
	 * 
	 * @return listMapRules - Lista de String que contém todas as regras de
	 *         mapeamento definidas pelo utilizador através de assertivas de
	 *         mapeamento
	 */
	public List<String> exportMappingRules() {
		List<Mapeamento> listMap = mapService.getMapToExport();
		List<String> listMapRules = new ArrayList<>();
		for (Mapeamento m : listMap) {
			listMapRules.add(m.getMapComments());
			listMapRules.add("\n");
			listMapRules.add(m.getMapRules());
			listMapRules.add("\n \n");
		}

		return listMapRules;
	}
}
