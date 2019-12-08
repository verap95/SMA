angular.module('SparqlFramework', [])
  .factory('datfactory', function($http, $q){
	  this.getA = function(){
		  var config = {
			 		params:{
			 			t: $scope.nameA, 
			 			s: $scope.nameS
			 		}
			 	}
		  return $http.get("http://localhost:8080/configuration/newAssertive/{t}&{s}", config)
			.then(function(response){
				console.log("estou aqui");
				console.log(response.data);
				return response.data;
								
		});
	  }
	  return this;
  });