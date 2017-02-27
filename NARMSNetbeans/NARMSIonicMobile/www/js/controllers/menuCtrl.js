angular.module('app.controllers.menu', [])
        .controller('menuCtrl', function ($scope, $ionicHistory, AppConst, $state, AuthServiceUtilities, $http, $ionicPopup, dataService) {

/*
            $scope.logout = function () {
                AuthServiceUtilities.logout();
                // Clear history when user have logout.
                $ionicHistory.clearCache();
                $ionicHistory.clearHistory();  
                $state.go('login', {}, {reload: true});
            };
*/
	$scope.showAdminPanel = AuthServiceUtilities.getIsAdmin();
	console.log("menu show admin panel: "+$scope.showAdminPanel);

	$scope.openAdmin = function() {
		if(!$scope.showAdminPanel){ return false; }
		$state.go('admin.users',{},{reload:true});
	};

$scope.logout = function() {
    AuthServiceUtilities.logout();
            $state.go('login',{},{reload: true});
};

        })
