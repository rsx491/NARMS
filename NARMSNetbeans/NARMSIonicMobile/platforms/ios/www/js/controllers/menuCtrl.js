angular.module('app.controllers.menu', [])
.controller('menuCtrl', function($scope,AppConst,$state,AuthServiceUtilities,$http,$ionicPopup, dataService) {

$scope.logout = function() {
    AuthServiceUtilities.logout();
            $state.go('login',{},{reload: true});
};

            })