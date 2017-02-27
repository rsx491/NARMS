angular.module('app.controllers.reset', []) 
.controller('resetCtrl', function($scope,AppConst,$state,AuthServiceUtilities,$http,$ionicPopup) {
     
    $scope.data = {
      submitLabel : 'Reset',
      hasCode: false,
      showPassword: false
    };
    
    $scope.reset = function(data) {
        
     
      var req = {
        method: 'POST',
        url: AppConst.API_URL+"request_reset",
        data: "email="+data.email,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      };
      

      $http(req).then(function(result){
          
          console.log(result);
          
          if(!result.data || !result.data.message || result.data.message != "Ok" ){
              var alertPopup = $ionicPopup.alert({
                    title: 'Request failed!',
                    template: JSON.stringify(result.data.message)
                });
          } else{     
                var alertPopup = $ionicPopup.alert({
                  title: "Password reset sent",
                  template: 'A password reset has been sent to your email with further instructions'
                });
                setTimeout(function(){
                  $state.go('loginpage', {}, {reload: true});
                },2000);
                
            }
         
         
          }, function(err){
              
              console.log(err)
              
              var alertPopup = $ionicPopup.alert({
                title: 'Request failed!',
                template: 'Request failed!'
            });
          });
      
      };
      
     

}) 