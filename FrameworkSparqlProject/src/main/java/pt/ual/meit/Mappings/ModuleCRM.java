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
	public String saveMappingRule(TempAssertive input, String classeSource, Boolean flgProp) {
		String mapRule = null;
		if (input.getTypeT().equals("C") && input.getTypeS().equals("C")) {
			mapRule = mp.createClassMappingRule(input.getNameT(), input.getNameS());
		} else if (input.getTypeT().equals("C") && !input.getTypeS().equals("C")) {
			mapRule = mp.createClassToPropertyMappingRule(input.getNameT(), input.getNameS(), classeSource);
		} else if (input.getTypeT().equals("D") && input.getTypeS().equals("D")) {
			if(!flgProp)
				mapRule = mp.createPropertyMapping(classeSource, input.getNameS(), input.getNameT());
			else { //Padrão MD2
				Propriedades pS = propService.findById(input.getP1S());
				mapRule = mp.createN1PropertyMapping(classeSource, 
							Constants.ATRIBUTOS(pS.getPrefix(), pS.getName()), input.getNameS(), 
							input.getFuncValue(),input.getNameT());
			}
				
		} else if (input.getTypeT().equals("O") && input.getTypeS().equals("O")) {
			mapRule = mp.createPropertyMapping(classeSource, input.getNameS(), input.getNameT());
		}

		if (input.getFilter() != null)
			mapRule = mapRule.concat(mp.addFilterToMapping(input.getValuePropS(), input.getFilter()));

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
