angular.module('SparqlFramework').controller("GenerateMappingController",function($scope, $http) {
	
	$scope.saveForm = function(){
		console.log("Entrei aqui");
		var data = new FormData();
		var file = $scope.fileData;
		var message = {
			"languageInput": $scope.languageInput, 
			"languageOutput": $scope.languageOutput
		};
		data.append('file', file);
		data.append('options', new Blob(
			[JSON.stringify(message)], {
				type : "application/json"
			}
		));
		
		var config = {
			transformRequest : angular.identity,
			responseType:'arraybuffer',
			headers : {
				'Content-Type' : undefined
			}
		};
		
		var url = "http://localhost:8080/generateMapping/saveFinalConfig";
		$http.post(url, data, config).then(function(data) {			
			console.log(data);
			var filename = data.headers('filename');
			console.log(filename);

			var file = new Blob([data.data], {type: 'text/html'});
			
			var isChrome = !!window.chrome && !!window.chrome.webstore;
			var url = window.URL || window.webkitURL;

			var downloadLink = angular.element('<a></a>');
           	downloadLink.attr('href',url.createObjectURL(file));
            downloadLink.attr('target','_self');
            downloadLink.attr('download', filename);
            downloadLink[0].click();
			
//			var data = angular.fromJson(response.data);
//			console.log(data);
//
//			console.log(data.allClasses);
//			$scope.listaT = data.allClasses;
//			window.location.href = "/configuration";

		}, function errorCallback(response) {
			alert(response.data.errorMessage);
		});
	}
});