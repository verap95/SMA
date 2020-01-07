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
	public List<String> saveMapSPARQL(TempAssertive input, String classeSource, Boolean flgProp, Boolean flgMPC) {
		String mapComment = null;
		String mapSPARQL = null;
		String clauseFilter = null;
		String clauseWhere = null;
		List<String> getResult = new ArrayList<>();
		Mapeamento mapping = null;
		String queryExp = null;
		
		if(input.getTypeT().equals("C") && input.getTypeS().equals("C")) { // Padrão MC1
			mapComment = Constants.PADRAO_MC1;
			mapSPARQL = ms.createClassMapping(input.getNameS(), input.getNameT(), input.getValuePropS());
			queryExp = ms.setQueryExp(1, input.getValuePropS(), null, null, null);
			clauseWhere = "a " + input.getNameS() + queryExp;
		}else if(input.getTypeT().equals("C") && !input.getTypeS().equals("C")) { //Padrão MC2
			mapComment = Constants.PADRAO_MC2;
			mapSPARQL = ms.createClassToPropertyMapping(input.getNameS(), input.getNameT(), classeSource);
			queryExp = ms.setQueryExp(1, input.getNameS(), null, null, null);
			clauseWhere = "a " + classeSource + queryExp;
		}else if(input.getTypeT().equals("D") && input.getTypeS().equals("D")) { //Padrões de Propriedades de Tipos de Dados
			Propriedades propDS = propService.findByNameAndPrefix(input.getNameS());
			Propriedades propDT = propService.findByNameAndPrefix(input.getNameT());
			mapping = mapService.findMapClasse(propDT.getClasse(), propDS.getClasse(), propDS);
			String[] whereClause = mapping.getClauseWhere().split(";");
			String[] propWhere;
			String prop = null;
			if(whereClause.length > 1) {
				propWhere = whereClause[1].split(":");
				if(propWhere[0].contentEquals(propDS.getClasse().getPrefix()))
					prop = propWhere[0];
			}
			
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
				}else if(Constants.ATRIBUTOS(pS1.getPrefix(), pS1.getName()).equals(input.getFuncV2()) && Constants.ATRIBUTOS(pS.getPrefix(),pS.getName()).equals(input.getFuncV1())) {
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV1(), "?t"));
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV2(), "?s"));
					mapSPARQL = ms.addFunctionToMapping(mapSPARQL, input.getFuncValue());
				}else
					return null;					
			}else if(flgMPC) { //Padrão MD3
				mapComment = Constants.PADRAO_MD3;
				queryExp = ms.setQueryExp(2, null, mapping.getClauseWhere(), mapping.getClauseFilter(), input.getNameS());
				mapSPARQL = ms.createEmbedPropertyMapping(input.getNameS(), input.getNameT(), classeSource, prop);	
			}
			else{ //Padrão MD1
				mapComment = Constants.PADRAO_MD1;
				queryExp = ms.setQueryExp(2, null, mapping.getClauseWhere(), mapping.getClauseFilter(), input.getNameS());
				mapSPARQL = ms.createPropertyMapping(input.getNameS(), input.getNameT(), Constants.ATRIBUTOS(propDS.getClasse().getPrefix(), propDS.getClasse().getName()), prop);
			}
			
			/* Adicionar funções de transformação */
			if(input.getFuncValue() != null) 
				mapSPARQL = ms.addFunctionToMapping(mapSPARQL, input.getFuncValue());
			else
				mapSPARQL = mapSPARQL.replace("functionExp", "");
			
		}else if(input.getTypeT().equals("O") || input.getTypeS().equals("O")) { //Padrões de Propriedades de Objetos
			if(!flgMPC) { //Padrão MO1
				mapComment = Constants.PADRAO_MO1;
				//mapSPARQL = ms.createPropertyMapping(input.getNameS(), input.getNameT());
			}else {
				mapComment = Constants.PADRAO_MO2;
				mapSPARQL = ms.createEmbedObjectPropertyMapping(input.getNameS(), input.getNameT(), classeSource);
			}
		}
		
		/* Adicionar filtro aos mapeamentos */
		if(input.getFilterS() != null) {
			String[] map = ms.addFilterToMapping(input.getFilterS(), mapSPARQL, input.getTypeS(), input.getTypeT(), queryExp);
			mapSPARQL = map[0];
			clauseFilter = map[1];
		}else {
			if(!queryExp.equals(null)) 
				mapSPARQL = mapSPARQL.replace("queryExp", queryExp);
			else
				mapSPARQL = mapSPARQL.replace("queryExp", ".");
		}
		getResult.add(mapComment);
		getResult.add(mapSPARQL);
		getResult.add(clauseWhere);
		getResult.add(clauseFilter);
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
