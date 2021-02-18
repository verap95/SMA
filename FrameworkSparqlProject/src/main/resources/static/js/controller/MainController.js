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
				if (fileS) {
					data.append('fileS', fileS);
				}
				data.append('fileT', fileT);
				data.append('requestData', new Blob(
						[ JSON.stringify(message) ], {
							type : "application/json"
						}));
				var config = {
					transformRequest : angular.identity,
					transformResponse : angular.identity,
					headers : {
						'Content-Type' : undefined
					}
				};
				var url = "http://localhost:8080/saveMapping";
				$http.post(url, data, config).then(function(response) {

					var data = angular.fromJson(response.data);
					$scope.listaT = data.allClasses;
					window.location.href = "/configuration";

				}, function errorCallback(response) {
					alert("Erro na configuração do mapeamento");
				});
			}

		});
