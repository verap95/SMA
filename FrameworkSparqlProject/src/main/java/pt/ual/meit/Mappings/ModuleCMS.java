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
	public List<String> saveMapSPARQL(TempAssertive input, String classeSource, Boolean flgProp, Boolean flgMPC, Boolean flgOP2) {
		String mapComment = null, mapSPARQL = null, prefixExp = null, queryExp = null, uriExp = null, functionExp = null, clauseWhere=null, clauseFilter = null;
		String[] uriExpProp;
		String uriProps;
		List<String> getResult = new ArrayList<>();
		Mapeamento mapping = null;
		if(input.getTypeT().equals("C") && input.getTypeS().equals("C")) { // Padrão MC1
			mapComment = Constants.PADRAO_MC1;
			prefixExp = ms.getPrefixes(1,input, classeSource, null, null); //Obtém os prefixos presentes na AM 
			queryExp = ms.setQueryExp(1, input.getValuePropS(), null, null, null, flgOP2, false, null, null, null, null);
			clauseWhere = queryExp;
			if(input.getFilterS() != null) { //Obtém o filtro f presente na AM
				String[] filter = ms.getFilterExp(queryExp, input.getFilterS());
				queryExp = filter[0];
				clauseFilter = filter[1];
			}
			mapSPARQL = ms.createClassMapping(prefixExp, queryExp, input.getNameS(), input.getNameT());
		}else if(input.getTypeT().equals("C") && !input.getTypeS().equals("C")) { //Padrão MC2
			mapComment = Constants.PADRAO_MC2;
			prefixExp = ms.getPrefixes(2,input, null , classeSource, null); //Obtém os prefixos presentes na AM 
			uriExpProp = ms.getUriExp(input.getListProps(), null); //Obtém os valores das propriedades A1 ... An presentes na AM e concatena-os
			uriExp = uriExpProp[1];
			queryExp = ms.setQueryExp(2, input.getNameS(), null, null, null, flgOP2, false, null, null, input.getListProps(), null);
			clauseWhere = queryExp;
			if(input.getFilterS() != null) { //Obtém o filtro f presente na AM
				String[] filter = ms.getFilterExp(queryExp, input.getFilterS());
				queryExp = filter[0];
				clauseFilter = filter[1];
			}
			mapSPARQL = ms.createClassToPropertyMapping(prefixExp, queryExp, uriExp, input.getNameT(), classeSource);
		}else if(input.getTypeT().equals("D") && input.getTypeS().equals("D")) { //Padrões de Propriedades de Tipos de Dados
			Propriedades propDS;
			if(input.isFlgExpPath())
				propDS = propService.findById(input.getpSPath());
			else
				propDS = propService.findById(input.getIdS());
			Propriedades propDT = propService.findById(input.getIdT());
			mapping = mapService.findMapClasse(propDT.getClasse(), propDS.getClasse(), propDS);
			String prop = null;
			if(mapping.getClauseWhere() != null) {
				String[] whereClause = mapping.getClauseWhere().split(";");
				String[] propWhere;
				if(whereClause.length > 1) {
					propWhere = whereClause[1].split(":");
					if(propWhere[0].equalsIgnoreCase(propDS.getClasse().getPrefix()))
						prop = propWhere[0];
				}
			}
			
			if(flgProp) { //Padrão MD2
				Propriedades propDS1 = propService.findById(input.getP1S());
				//Falta colocar diferenciacao entre o template T7 e T8 --Para já fica sempre o template T7
				mapComment = Constants.PADRAO_MD2_T7;
				prefixExp = ms.getPrefixes(7,input, Constants.ATRIBUTOS(propDS1.getPrefix(), propDS1.getName()), prop, null); //Obtém os prefixos presentes na AM 
				queryExp = ms.setQueryExp(7, Constants.ATRIBUTOS(propDS1.getPrefix(), propDS1.getName()), mapping.getClauseWhere(), mapping.getClauseFilter(), input.getNameS(), flgOP2, input.getFuncValue() == null ? false: true, null, null, null, null);
				if(Constants.ATRIBUTOS(propDS1.getPrefix(), propDS1.getName()).equals(input.getFuncV1()) && Constants.ATRIBUTOS(propDS.getPrefix(),propDS.getName()).equals(input.getFuncV2())) {
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV1(), "?s"));
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV2(), "?t"));
					
				}else if(Constants.ATRIBUTOS(propDS1.getPrefix(), propDS1.getName()).equals(input.getFuncV2()) && Constants.ATRIBUTOS(propDS.getPrefix(),propDS.getName()).equals(input.getFuncV1())) {
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV1(), "?t"));
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV2(), "?s"));
				}else
					return null;
				String props = input.getFuncValue().replace("CONCAT(", "");
				if(props.contains("))")){
					props = props.replace("))", ")");
				}else {
					props = props.replace(")", "");
				}
				
				mapSPARQL = ms.createN1PropertyMapping(prefixExp, input.getNameT(), Constants.ATRIBUTOS(propDS.getClasse().getPrefix(), propDS.getClasse().getName()), props, queryExp, functionExp);
				//mapSPARQL = ms.createN1PropertyMapping(Constants.ATRIBUTOS(propDS1.getPrefix(), propDS1.getName()), input.getNameS(), input.getNameT(), input.getFuncValue());	

			}else if(flgMPC) { //Padrão MD3
				mapComment = Constants.PADRAO_MD3;
				prefixExp = ms.getPrefixes(4,input, classeSource, prop, null); //Obtém os prefixos presentes na AM 
				List<String> listProps = new ArrayList<String>();
				listProps.add(input.getNameS());
				uriExpProp = ms.getUriExp(listProps, mapping.getListProps()); //Obtém os valores das propriedades A1 ... An presentes na AM e concatena-os
				uriExp = uriExpProp[1];
				queryExp = ms.setQueryExp(4, null, mapping.getClauseWhere(), mapping.getClauseFilter(), input.getNameS(), flgOP2, input.getFuncValue() == null ? false: true, null, null, null, null);
				mapSPARQL = ms.createEmbedPropertyMapping(Constants.ATRIBUTOS(propDS.getClasse().getPrefix(), propDS.getClasse().getName()), input.getNameT(), prefixExp, queryExp, uriExp, uriExpProp[0]);	
			}
			else{ //Padrão MD1
				mapComment = Constants.PADRAO_MD1;
				prefixExp = ms.getPrefixes(6,input, classeSource, prop, null); //Obtém os prefixos presentes na AM 
				if(input.getFuncValue() != null) {
					input.setFuncValue(input.getFuncValue().replace(input.getFuncV1(), "?s"));
				}
				queryExp = ms.setQueryExp(6, null, mapping.getClauseWhere(), mapping.getClauseFilter(), input.getNameS(), flgOP2, input.getFuncValue() == null ? false: true, null, null, null, input.isFlgExpPath() ? Constants.ATRIBUTOS(propDS.getPrefix(), propDS.getName()) : null);
				functionExp = ms.getFunctionExp(input.getFuncValue());
				mapSPARQL = ms.createPropertyMapping(Constants.ATRIBUTOS(propDS.getClasse().getPrefix(), propDS.getClasse().getName()), input.getNameT(), prefixExp, queryExp, functionExp);
			}
		}else if(input.getTypeT().equals("O") || input.getTypeS().equals("O")) { //Padrões de Propriedades de Objetos
			Propriedades propDS;
			
			if(input.isFlgExpPath())
				propDS = propService.findById(input.getpSPath());
			else
				propDS = propService.findById(input.getIdS());
			
			Propriedades propDT = propService.findById(input.getIdT());
			mapping = mapService.findMapClasse(propDT.getClasse(), propDS.getClasse(), propDS);
			String[] propWhere;
			String propD = null;
			String[] whereClause;
			if(mapping.getClauseWhere() != null) {
				whereClause = mapping.getClauseWhere().split(";");
				if(whereClause.length > 1) {
					propWhere = whereClause[1].split(":");
					if(propWhere[0].equalsIgnoreCase(propDS.getClasse().getPrefix()))
						propD = propWhere[0];
				}
			}
			
			if(!flgMPC) { //Padrão MO1
				mapComment = Constants.PADRAO_MO1;
				prefixExp = ms.getPrefixes(6,input, classeSource, propD, null); //Obtém os prefixos presentes na AM 
				queryExp = ms.setQueryExp(6, null, mapping.getClauseWhere(), mapping.getClauseFilter(), input.getNameS(), flgOP2, false, null, null, null, input.isFlgExpPath() ? Constants.ATRIBUTOS(propDS.getPrefix(), propDS.getName()) : null);
				mapSPARQL = ms.createPropertyMapping(Constants.ATRIBUTOS(propDS.getClasse().getPrefix(), propDS.getClasse().getName()), input.getNameT(), prefixExp, queryExp, null);
			}else {
				Mapeamento mappingRange = mapService.findMapClasse(propDT.getRangeClasse(), propDS.getClasse(), propDS);
				whereClause = mapping.getClauseWhere().split(";");
				String propRangeClass = null;
				if(whereClause.length > 1) {
					propWhere = whereClause[1].split(":");
					if(propWhere[0].equalsIgnoreCase(propDS.getClasse().getPrefix()))
						propRangeClass = propWhere[0];
				}
				mapComment = Constants.PADRAO_MO2;
				prefixExp = ms.getPrefixes(5,input, classeSource, propD, propRangeClass); //Obtém os prefixos presentes na AM 
				List<String> listProps = new ArrayList<String>();
				listProps.add(input.getNameS());
				uriExpProp = ms.getUriExp(listProps, mappingRange.getListProps()); //Obtém os valores das propriedades A1 ... An presentes na AM e concatena-os
				uriExp = uriExpProp[1];
				queryExp = ms.setQueryExp(5, null, mapping.getClauseWhere(), mapping.getClauseFilter(), input.getNameS(), flgOP2, false, mappingRange.getClauseWhere(), mappingRange.getClauseFilter(), null, null);
				mapSPARQL = ms.createEmbedObjectPropertyMapping(Constants.ATRIBUTOS(propDS.getClasse().getPrefix(), propDS.getClasse().getName()), input.getNameT(), prefixExp, queryExp, uriExp);
			}
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
