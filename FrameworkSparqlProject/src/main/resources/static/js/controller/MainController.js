'use strict'

angular.module('SparqlFramework').controller(
		"MainController",
		function($scope, $http) {
			$scope.call = function() {

				$http.get("http://localhost:8080/getOntologyT").then(
						function(response) {
							$scope.ontologyT = response;
						});

			};
			$scope.saveMapping = function() {
				var message = {
					"ontologyS" : {
						"name" : $scope.nameS,
						"url" : $scope.urlS,
						"language" : $scope.languageS
					},
					"ontologyT" : {
						"name" : $scope.nameT,
						"url" : $scope.urlT,
						"language" : $scope.languageT
					}
				}

				var data = new FormData();
				var fileS = $scope.fileS;
				var fileT = $scope.fileT;
				if (!fileS) {
					console.log("FileS não foi preenchido");
				} else {
					data.append('fileS', fileS);
				}
				data.append('fileT', fileT);
				data.append('requestData', new Blob(
						[ JSON.stringify(message) ], {
							type : "application/json"
						}));
				console.log(data);
				var config = {
					transformRequest : angular.identity,
					transformResponse : angular.identity,
					headers : {
						'Content-Type' : undefined
					}
				};
				// formData.append('file', $scope.fileS);
				var url = "http://localhost:8080/saveMapping";
				$http.post(url, data, config).then(function(response) {

					var data = angular.fromJson(response.data);
					$scope.listaT = data.allClasses;
					window.location.href = "/configuration";

				}, function errorCallback(response) {
					alert(response.data.errorMessage);
				});

				// $http({
				// url:"http://localhost:8080/saveMapping",
				// method: "POST",
				// data: message,
				// headers: {'Content-Type': undefined},
				// transformRequest : angular.identity,
				// //headers: "Content-Type: application/json"
				// })
				// .then(function(response){
				// //console.log(response);
				//
				// window.location.href="/save";
				// }), function(response){
				// alert("A configuração do mapeamento não foi efetuada com
				// sucesso.");
				// }
			}

		});
