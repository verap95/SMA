'use strict';

  angular.module('SparqlFramework', [])
    .controller('ConfigController', function ($scope, $http) {
    	$scope.flgFilter = false;
    	$scope.flgFunction = false;
    	$scope.flgLoad = false;
    	$scope.inputMA = null;
    	$scope.nodeSource = null;
    	$scope.assertives = [];
    	$scope.valueFilterS = null;
    	$scope.valueFunction = null;
    	$scope.pSOld = null;
    	$scope.checkboxProp2 = false;
    	$scope.valueFunc = null;
    	$http.get("http://localhost:8080/configuration/loadOntologyTarget").then(function(response){
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
        		});
        	});
        	
        	$('#treeSource').on('nodeUnselected', function(event, data){
        		console.log("IM HERE");
//        		$scope.$apply(function(){
//        			$scope.nameS = null;
//        		});
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
			
    	console.log($scope.inputMA);
    	var config = {
		 		params:{
		 			nt: $scope.idA,
		 			tt: $scope.typeA,
		 			ns: $scope.idS,
		 			ts: $scope.typeS,
		 			input: inputData, 
		 			p1S: $scope.pSOld
		 		}
		 	}
    	
	 	$http.get("http://localhost:8080/configuration/newAssertive/{nt}&{tt}&{ns}&{ts}&{input}&{p1S}", config).then(function(response){
	 		$scope.inputMA = response.data.assertive;	
	 		$scope.pSOld = response.data.p1S;
	 		console.log("Data from new Assertive: " , response.data);
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
    	console.log("Save mapping ** $scope.pSOld: " , $scope.pSOld , $scope.idS);
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
    		p1S: $scope.pSOld, 
    		funcV1: $scope.prop1, 
    		funcV2: $scope.prop2, 
    		fPO2: $scope.checkboxProp2
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
    		
    		$scope.inputMA = null;
    		$scope.valueFilter = null;
    		$scope.valueProp = null;
    		$scope.valueFilterS = null;
    		$scope.valueFunction = null;
    		$scope.pSOld = null;
    		
        	$http.get("http://localhost:8080/configuration/ontologyTarget/{id}&{type}", config2).then(function(response){
    	 		var dataR = response.data;
	 			console.log(dataR);
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
//	   if($scope.valueType != null){
//		   $scope.valueFilter = $scope.valueProp +  $scope.valueOperator + $scope.value + "^^" + $scope.valueType;
//		   if($scope.typeS == "C" && $scope.typeA == "C"){
//			   $scope.valueFilterS = "?o " + $scope.valueOperator + $scope.value + "^^" + $scope.valueType;
//		   } 
//	   }else{
		   $scope.valueFilter = $scope.valueProp + " " + $scope.valueOperator + " " + $scope.value;
		   if($scope.typeS == "C" && $scope.typeA == "C"){
			   $scope.valueFilterS = "?o " + $scope.valueOperator + " " + $scope.value;
		   } 
//	   }
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
		$scope.valueOperator = null;
		$scope.value = null;
		$scope.inputMA = $scope.inputMA + " / " + $scope.valueFilter;
		$('#filterModal').modal('hide');
   }
   
   $scope.newFunction = function(){
		$scope.valueOfFunction = null;
		$scope.propriedades = null;
		var data = {
			idTemp: $scope.idTemp, 
			assertive: $scope.inputMA,
			funcValue: $scope.valueFunction
		}
		$http.get("http://localhost:8080/configuration/addFunction", data).then(function(response){
	 		var dataR = response.data;
				$scope.inputMA = dataR.assertive;		
	 	});
		
		$('#functionModal').modal('hide');
		
		
  }
   
  $scope.getTypeFunction = function(){
	  console.log("Mudei de opção de função");
	  console.log("TypeOfFunction: " , $scope.typeOfFunction);
	  var config = {
		  params:{
			  tf: $scope.typeOfFunction
		  }
	  }
		  
	  $http.get("http://localhost:8080/configuration/getTypeFunction", config).then(function(response){
		  console.log(response.data);
		  $scope.functionList = response.data;
	  });
	  
	  $scope.getPropList();
  }	
  
  $scope.getPropList = function(){
	  console.log("Entrei no getPropList");
	  var config = {
		  params:{
			  idS: $scope.idS, 
			  typeS: $scope.typeS
		  }
	  }
		  
	  $http.get("http://localhost:8080/configuration/getPropertiesToList", config).then(function(response){
		  console.log(response.data);
		  $scope.propList = response.data;
	  });
  }
});