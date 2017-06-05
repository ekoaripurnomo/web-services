var appLatihan = angular.module('appLatihan', []);

appLatihan.controller('AlamatController', function($http, $scope){
	$scope.dataAlamat = {};
	
	$scope.hapusAlamat = function(x){
		$http.delete('/api/alamat/'+x.id).then(sukses, gagal);
		function sukses(response){
			$scope.updateDataAlamat();
		};
		function gagal(response){
			console.log(response);
			alert('Error : '+response)
		};
	};
	
	$scope.simpanAlamat = function(){
		$http.post('/api/alamat/', $scope.alamat).then(sukses, gagal);
		function sukses(response){
			$scope.updateDataAlamat();
			$scope.alamat = null;
		};
		function gagal(response){
			alert('gagal ya');
			console.log(response);
			alert('Error : '+response);
		};
	};
	
	$scope.updateDataAlamat = function(){
		$http.get('/api/alamat').then(sukses, gagal);
		function sukses (response){
			$scope.dataAlamat = response.data;
			console.log(response);
		};
		function gagal (response){
			console.log(response);
			alert('Error = '+ response);
		};
	};
	$scope.updateDataAlamat();
});