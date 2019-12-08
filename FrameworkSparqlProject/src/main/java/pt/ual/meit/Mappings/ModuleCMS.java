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
import pt.ual.meit.utils.SPARQLTemplates;

@Component
public class ModuleCMS {

	private static SPARQLTemplates ms = new SPARQLTemplates();

	@Autowired
	private MappingService mapService;
	
	@Autowired
	private PropriedadesService propService;
	
	/**
	 * Método que permite retornar um mapeamento SPARQL consoante os parâmetros de entrada
	 * @param input - Objeto que contém todos os dados relativos a um mapeamento criado pelo utilizador.
	 * @return mapRule - String que contém o mapeamento SPARQL correspondente
	 */
	public List<String> saveMapSPARQL(TempAssertive input, String classeSource, String classeTarget, Boolean flgProp) {
		String mapComment = null;
		String mapSPARQL = null;
		List<String> getResult = new ArrayList<>();
		
		if(input.getTypeT().equals("C") && input.getTypeS().equals("C")) { // Padrão MC1
			mapComment = Constants.PADRAO_MC1;
			mapSPARQL = ms.createClassMapping(input.getNameS(), input.getNameT(), input.getValuePropS());
		}else if(input.getTypeT().equals("C") && !input.getTypeS().equals("C")) { //Padrão MC2
			mapComment = Constants.PADRAO_MC2;
			mapSPARQL = ms.createClassToPropertyMapping(input.getNameS(), input.getNameT(), classeSource);
		}else if(input.getTypeT().equals("D") && input.getTypeS().equals("D")) { //Padrões de Propriedades de Tipos de Dados
			if(flgProp) { //Padrão MD2
				Propriedades pS1 = propService.findById(input.getP1S());
				Propriedades pS = propService.findById(input.getIdS());
				mapComment = Constants.PADRAO_MD2;
				mapSPARQL = ms.createN1PropertyMapping(Constants.ATRIBUTOS(pS1.getPrefix(), pS1.getName()), 
						input.getNameS(), input.getNameT());
				if(Constants.ATRIBUTOS(pS1.getPrefix(), pS1.getName()).equals(input.getFuncV1()) && Constants.ATRIBUTOS(pS.getPrefix(),pS.getName()).equals(input.getFuncV2())) {
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV1(), "?s"));
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV2(), "?t"));
					mapSPARQL = ms.addFunctionToMapping(mapSPARQL, input.getFuncValue());
				}else if(Constants.ATRIBUTOS(pS1.getPrefix(), pS1.getName()) == input.getFuncV2() && Constants.ATRIBUTOS(pS.getPrefix(),pS.getName()) == input.getFuncV1()) {
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV1(), "?t"));
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV2(), "?s"));
				}else
					return null;					
			}else { //Padrão MD1
				mapComment = Constants.PADRAO_MD1;
				mapSPARQL = ms.createPropertyMapping(input.getNameS(), input.getNameT());
			}
			
			/* Adicionar funções de transformação */
			if(input.getFuncValue() != null) 
				mapSPARQL = ms.addFunctionToMapping(mapSPARQL, input.getFuncValue());
			else
				mapSPARQL = mapSPARQL.replace("functionExp", "");
			
		}else if(input.getTypeT().equals("O") && input.getTypeS().equals("O")) { //Padrões de Propriedades de Objetos
			mapComment = Constants.PADRAO_MO1;
			mapSPARQL = ms.createPropertyMapping(input.getNameS(), input.getNameT());
			//Mapeamento MO1
		}
		
		/* Adicionar filtro aos mapeamentos */
		if(input.getFilterS() != null)
			mapSPARQL = ms.addFilterToMapping(input.getFilterS(), mapSPARQL, input.getTypeS(), input.getTypeT(), input.getValuePropS());
		else
			mapSPARQL = mapSPARQL.replace("queryExp", ".");
		
		getResult.add(mapComment);
		getResult.add(mapSPARQL);
		return getResult;
	}
	
	/**
	 * Método para exportar uma lista com todas as regras de mapeamento
	 * 
	 * @return listMapRules - Lista de String que contém todas as regras de
	 *         mapeamento definidas pelo utilizador através de assertivas de
	 *         mapeamento
	 */
	public List<String> exportMappingSPARQL() {
		List<Mapeamento> listMap = mapService.getMapToExport();
		List<String> listMapRules = new ArrayList<>();
		for (Mapeamento m : listMap) {
			listMapRules.add(m.getMapComments());
			listMapRules.add("\n");
			listMapRules.add(m.getMapSPARQL());
			listMapRules.add("\n \n");
		}

		return listMapRules;
	}
}
