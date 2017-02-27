angular.module('app.controllers.login', []) 
.controller('loginCtrl', function($scope,AppConst,$state,AuthServiceUtilities,$http,$ionicPopup) {
     
    $scope.data = {};
    
     
     console.log("login controller");
    $scope.login = function(data) {
        
     console.log("login email := " + data.email);
     console.log("login password := " + data.password);
     
      var req = {
        method: 'POST',
        url: AppConst.API_URL+"login",
        data: "email="+data.email+"&password="+data.password,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      };

      $http(req).then(function(result){
          
          console.log(result);
          console.log(result.data);
          console.log(result.data.message);
          
          if(result.data.message != null ){
              var alertPopup = $ionicPopup.alert({
                    title: 'Login failed!',
                    template: JSON.stringify(result.data.message)
                });
          }else{
                testResult = result.data.token;        
                $state.go('hOME.homePage', {}, {reload: true});
                AuthServiceUtilities.storeUserCredentials(testResult);
                AuthServiceUtilities.loadUserCredentials();
            }
         
         
      }, function(err){
          
          console.log(err)
          
          var alertPopup = $ionicPopup.alert({
            title: 'Login failed!',
            template: 'Login failed!'
        });
      });
 
  }; 

}) 