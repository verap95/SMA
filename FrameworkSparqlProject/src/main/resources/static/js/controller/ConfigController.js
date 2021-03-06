'use strict';

  angular.module('SparqlFramework', [])
    .controller('ConfigController', function ($scope, $http) {
    	$scope.loading = true;
    	$scope.flgFilter = false;
    	$scope.flgFunction = false;
    	$scope.flgLoad = false;
    	$scope.inputMA = null;
    	$scope.nodeSource = null;
    	$scope.assertives = [];
    	$scope.valueFilterS = null;
    	$scope.valueFilter = null;
    	$scope.tempValueFilter = null;
    	$scope.valueFunction = null;
    	$scope.pSOld = null;
    	$scope.checkboxProp2 = false;
    	$scope.valueFunc = null;
    	$scope.flgPathExp = null;
    	$scope.pSPath = null;
    	$scope.oldFunction =  null;
    	$scope.tempProp1 = null;
    	$scope.tempProp2 = null;
    	$http.get("http://localhost:8080/configuration/loadOntologyTarget").then(function(response){
    		$scope.loading = false;
    		 var responseData = response.data;
    		 var options = {
		    	data: responseData,
		    	expandIcon: "fa fa-caret-right",
		    	collapseIcon: "fa fa-caret-down",
		    	levels: 0
    		 };
    		 $('#default-tree').treeview(options);
    		 
    		 $('#default-tree').on('nodeSelected', function(event, data){
				 	var config = {
				 		params:{
				 			id: data.id, 
				 			type: data.type
				 		}
				 	}
				 	$http.get("http://localhost:8080/configuration/ontologyTarget/{id}&{type}", config).then(function(response){
    			 			var dataR = response.data;
			 				$scope.inputMA = dataR.aBasic;
			 				$scope.idA = dataR.id;
			 				$scope.nameA = dataR.text;
			 				$scope.typeA = dataR.type;
			 				$scope.assertives = dataR.listA;
			 				var number = $scope.assertives.length;
			 				if(number >= 2){
			 					$scope.flgFull = true;
			 				}
    			 	});
    		    	$scope.$apply(function(){
    		    		$scope.flgLoad = true;
    		    		$scope.mapeamentoSPARQL = null;   
    		    		$scope.regrasMapeamento = null;
    		    		$scope.properties = null;
    		        	$scope.flgFilter = false;
    		        	$scope.flgFunction = false;
    		    	});
    		 });
    		 
    		 $('#default-tree').on('nodeUnselected', function(event, data){
 		    	$scope.$apply(function(){
 		    		$scope.flgLoad = false;
 		    		$scope.flgFull = false;
 		    		$scope.inputMA = null;
 		    		$scope.mapeamentoSPARQL = null;
 		    		$scope.regrasMapeamento = null;
 		    		$scope.assertives = null;
 		    		$scope.properties = null;
		        	$scope.flgFilter = false;
 		    	});
    		 });
    	});

    $scope.loadSource = function(){
    	$http.get("http://localhost:8080/configuration/loadOntologySource").then(function(response){
    		var data = response.data;
    		$scope.listaS = data;
    		var options = {
    		    	data: data,
    		    	expandIcon: "fa fa-caret-right",
    		    	collapseIcon: "fa fa-caret-down",
    		    	levels: 0
        		 };
    		$('#treeSource').treeview(options);
        		 
        	$('#treeSource').on('nodeSelected', function(event, data){
        		$scope.$apply(function(){
        			$scope.idS = data.id;
        			$scope.nameS = data.text;
        			$scope.typeS = data.type;
        			$scope.flgPathExp = data.flgPathExp;
        			$scope.pSPath = data.psPath;
        		});
        	});
        	
        	$('#treeSource').on('nodeUnselected', function(event, data){
        	});
    	});
    }
  
    $scope.newAssertive = function(){
    	var inputData;
    	if($scope.inputMA == null){
    		inputData = "null"
    	}else
    		inputData = $scope.inputMA
    	
    	if($scope.typeS == 'D')
			$scope.flgFunction = true
		else
			$scope.flgFunction = false
			
    	var config = {
		 		params:{
		 			nt: $scope.idA,
		 			tt: $scope.typeA,
		 			ns: $scope.idS,
		 			ts: $scope.typeS,
		 			input: inputData, 
		 			p1S: $scope.pSOld, 
		 			listProps: $scope.listProps, 
		 			flgPathExp: $scope.flgPathExp, 
		 			pSPath : $scope.pSPath
		 		}
		 	}
    	
	 	$http.get("http://localhost:8080/configuration/newAssertive/{nt}&{tt}&{ns}&{ts}&{input}&{p1S}&{listProps}&{flgPathExp}&{pSPath}", config).then(function(response){
	 		$scope.inputMA = response.data.assertive;	
	 		$scope.pSOld = response.data.p1S;
	 		$scope.listProps = response.data.listProps;
	 	});
		
		var config1 = {
			params: {
				s: $scope.nameS
			}
		}
    	$http.get("http://localhost:8080/configuration/loadOntologySource/{s}", config1).then(function(response){
			$scope.properties = response.data.nodes;
	 		$('#loadSourceModal').modal('hide');
		});
    		
    	$scope.flgFilter = true;
    }
    
    $scope.saveMap = function(){
    	var requestData = {
    		idT : $scope.idA,
    		nameT: $scope.nameA,
    		typeT: $scope.typeA,
    		idS : $scope.idS,
    		nameS: $scope.nameS,
    		typeS: $scope.typeS,
    		filter: $scope.valueFilter,
    		filterS: $scope.valueFilterS,
    		valuePropS: $scope.valueProp,
    		assertive : $scope.inputMA, 
    		funcValue: $scope.valueFunction, 
    		oldFunction: $scope.oldFunction,
    		p1S: $scope.pSOld, 
    		funcV1: $scope.tempProp1, 
    		funcV2: $scope.tempProp2, 
    		fPO2: $scope.checkboxProp2, 
    		listProps: $scope.listProps, 
    		flgExpPath: $scope.flgPathExp, 
    		pSPath: $scope.pSPath    		
    	}
    	var config = { 
    		headers:{'Content-Type':'application/json'}
    	}
    	
    	var url = "http://localhost:8080/configuration/saveAssertive";
    	$http.post(url, requestData, config).then(function(response) {
    		var data = response.data;
    		var config2 = {
    			params:{
    				id: $scope.idA, 
    				type: $scope.typeA
    			}
        	}
    		$scope.idA = null;
    		$scope.nameA = null;
     		$scope.typeA = null;
     		$scope.idS = null;
     		$scope.nameS = null;
     		$scope.typeS = null;
    		$scope.inputMA = null;
    		$scope.flgFunction = false;
    		$scope.flgFilter = false;
    		$scope.valueFilter = null;
    		$scope.valueProp = null;
    		$scope.valueFilterS = null;
    		$scope.valueFunction = null;
    		$scope.pSOld = null;
    		$scope.oldFunction = null;
    		$scope.tempProp1 = null;
    		$scope.tempProp2 = null;
    		$scope.listProps = null;
    		
        	$http.get("http://localhost:8080/configuration/ontologyTarget/{id}&{type}", config2).then(function(response){
    	 		var dataR = response.data;
 				$scope.inputMA = dataR.aBasic;
 				$scope.idA = dataR.id;
 				$scope.nameA = dataR.text;
 				$scope.typeA = dataR.type;
 				$scope.assertives = dataR.listA;
 				var number = $scope.assertives.length;
 				if(number >= 2){
 					$scope.flgFull = true;
 				}
    	 	});
    		
    	}).catch(function(fallback){
    		if(fallback.status == 500)
    			$scope.descError = "Não foi possível criar o mapeamento especificado."
			else if(fallback.status == 406)
    			$scope.descError = "Não é possível criar a AMD/AMO sem previamente possuir uma AMC."
    		
    		$('#errorMap').modal('show');
    	});    	
    }
    
   $scope.loadMappingSPARQL = function(mapSPARQL, mapRules){
	   $scope.mapeamentoSPARQL = mapSPARQL;
	   $scope.regrasMapeamento = mapRules;
	   $scope.flgFilter = false;
   }
   
   $scope.loadAllAssertives = function(){
	   $http.get("http://localhost:8080/configuration/loadAllAssertives").then(function(response){
		   var data = response.data;
		   $scope.allList = data;
   			var options = {
   		    	data: data
       		};		   
	   });
   }
   
   $scope.exportAllMappingRules = function(){
	   $http.get("http://localhost:8080/generateMapping/exportMapRules").then(function(data){
		   var filename = data.headers('filename');
		   var file = new Blob([data.data], {type: 'text/html'});
		   var isChrome = !!window.chrome && !!window.chrome.webstore;
		   var url = window.URL || window.webkitURL;
		   var downloadLink = angular.element('<a></a>');
		   downloadLink.attr('href',url.createObjectURL(file));
           downloadLink.attr('target','_self');
           downloadLink.attr('download', filename);
           downloadLink[0].click();
	   });
   }
   
   $scope.exportAllMappingSPARQL = function(){
	   $http.get("http://localhost:8080/generateMapping/exportMapSPARQL").then(function(data){
		   var filename = data.headers('filename');
		   var file = new Blob([data.data], {type: 'text/html'});
		   var isChrome = !!window.chrome && !!window.chrome.webstore;
		   var url = window.URL || window.webkitURL;

		   var downloadLink = angular.element('<a></a>');
		   downloadLink.attr('href',url.createObjectURL(file));
           downloadLink.attr('target','_self');
           downloadLink.attr('download', filename);
           downloadLink[0].click();
	   });
   }
   
   $scope.exportAllAMs = function(){
	   $http.get("http://localhost:8080/generateMapping/exportAMs").then(function(data){
		   var filename = data.headers('filename');
		   var file = new Blob([data.data], {type: 'text/html'});
		   var isChrome = !!window.chrome && !!window.chrome.webstore;
		   var url = window.URL || window.webkitURL;

		   var downloadLink = angular.element('<a></a>');
		   downloadLink.attr('href',url.createObjectURL(file));
           downloadLink.attr('target','_self');
           downloadLink.attr('download', filename);
           downloadLink[0].click();
	   });
   }
   
   $scope.removeAssertiveInList = function(x){
	   	var data = x.id;	   	
  		$http.post("http://localhost:8080/configuration/removeAssertive", data).then(function(response) {
  			$http.get("http://localhost:8080/configuration/loadAllAssertives").then(function(response){
  			   var data = response.data;
  			   $scope.allList = data;
  		   });
  		});
   }
   
   $scope.removeAssertive = function(x){
    	var data = x.id;
   		$http.post("http://localhost:8080/configuration/removeAssertive", data).then(function(response) {
   			var config2 = {
    			params:{
    				id: $scope.idA, 
    				type: $scope.typeA
    			}
        	}
   			$http.get("http://localhost:8080/configuration/ontologyTarget/{id}&{type}", config2).then(function(response){
    	 		var dataR = response.data;
 				$scope.inputMA = null;
 				$scope.idA = dataR.id;
 				$scope.nameA = dataR.text;
 				$scope.typeA = dataR.type;
 				$scope.assertives = dataR.listA;
 				$scope.flgFull = false; 				
    	 	});
   		});
   }
   
   $scope.continue = function(){
	   window.location.href = "/generateMapping";
   }
    
   $scope.applyValue = function(){
	   $scope.valueFilter = $scope.valueProp + " " + $scope.valueOperator + " " + $scope.value;
	   $scope.tempValueFilter = $scope.valueFilter;
	   if($scope.typeS == "C" && $scope.typeA == "C"){
		   $scope.valueFilterS = "?o " + $scope.valueOperator + " " + $scope.value;
	   }
   }
   
   $scope.applyValueFunction = function(){
	   if($scope.typeOfFunction == 'String'){
		   if($scope.prop2 != null){
			   if($scope.checkboxProp2){
				   if($scope.valueFunc != null){
					   $scope.valueFunction = $scope.valueOfFunction + "(" + $scope.prop1 + "," + $scope.valueFunc + ", COALESCE(" + $scope.prop2 + ", ''))";
				   }else 
					   $scope.valueFunction = $scope.valueOfFunction + "(" + $scope.prop1 + ", COALESCE(" + $scope.prop2 + ", ''))";
			   }else
				   if($scope.valueFunc != null){
					   $scope.valueFunction = $scope.valueOfFunction + "(" + $scope.prop1 + "," + $scope.valueFunc + "," + $scope.prop2 + ")";
				   }else 
					   $scope.valueFunction = $scope.valueOfFunction + "(" + $scope.prop1 + "," + $scope.prop2 + ")";
		   }else{
			   if($scope.oldFunction != null){
				   $scope.prop1 = "v";
			   }
			   if($scope.valueFunc != null){
				   $scope.valueFunction = $scope.valueOfFunction + "(" + $scope.prop1 + "," + $scope.valueFunc + ")";
			   }else{
				   $scope.valueFunction = $scope.valueOfFunction + "(" + $scope.prop1 + ")";
			   }
		   }
	   }else{
		   if($scope.valueFunc != null)
			   $scope.valueFunction = $scope.valueOfFunction + "(" +  $scope.valueFunc + ")";
		   else 
			   $scope.valueFunction = $scope.valueOfFunction + "()";
	   }
   }

   $scope.newFilter = function(){
	   	if($scope.valueFilter == null)
	   		$scope.applyValue();
		$scope.valueOperator = null;
		$scope.value = null;
		$scope.inputMA = $scope.inputMA + " / " + $scope.valueFilter;
		$scope.tempValueFilter = null;
		$('#filterModal').modal('hide');
   }
   
   $scope.newFunction = function(){
	   
		$scope.valueOfFunction = null;
		$scope.propriedades = null;
		var data = {
			params:{
				assertive: $scope.inputMA,
				funcValue: $scope.valueFunction, 
				idS : $scope.idS,
				p1S: $scope.pSOld, 
				oldFunction: $scope.oldFunction
			}
		}
		$http.get("http://localhost:8080/configuration/newFunction/{assertive}&{funcValue}&{idS}&{p1S}&{oldFunction}", data).then(function(response){
	 		var dataR = response.data;
			$scope.inputMA = dataR.assertive;	
			$scope.oldFunction = dataR.oldFunction;
			if($scope.oldFunction == $scope.valueFunction){
				$scope.valueFunction = null;
				$scope.tempProp1 = $scope.prop1;
				$scope.tempProp2 = $scope.prop2;
			}else{
				$scope.valueFunction = dataR.funcValue;
			}
			
			$scope.typeOfFunction = null;
			$scope.prop2 = null;
			$scope.prop1 = null;
			$scope.valueFunc = null;
	 	});
		
		$('#functionModal').modal('hide');
		
		
  }
   
  $scope.getTypeFunction = function(){
	  var config = {
		  params:{
			  tf: $scope.typeOfFunction
		  }
	  }
		  
	  $http.get("http://localhost:8080/configuration/getTypeFunction", config).then(function(response){
		  $scope.functionList = response.data;
	  });
	  
	  $scope.getPropList("FUNCTION");
  }	
  
  $scope.getPropList = function(a){
	  var config = {
		  params:{
			  idS: $scope.idS, 
			  typeS: $scope.typeS, 
			  p1S: $scope.pSOld
		  }
	  }
		  
	  $http.get("http://localhost:8080/configuration/getPropertiesToList", config).then(function(response){
		  $scope.propList = response.data;
	  });
  }
  
  $scope.clearModal = function(){
	  $scope.valueOfFunction = null;
	  $scope.propriedades = null;
	  $scope.typeOfFunction = null;
	  $scope.prop2 = null;
	  $scope.prop1 = null;
  }
});