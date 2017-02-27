angular.module('app.controllers.resetcode', []) 
.controller('resetCodeCtrl', function($scope,AppConst,$state,AuthServiceUtilities,$http,$ionicPopup) {
     
    $scope.data = {
      submitLabel : 'Update Password',
      hasCode: true,
      showPassword: true,
      isProcessing : false
    };
    console.log(window.location.hash.length);
    if(window.location.hash.length < 17){
      $state.go('login',{},{reload:true});
      return;
    }
    $scope.data.reset_code = window.location.hash.substr(13);
    console.log($scope.data.reset_code);

    $scope.reset = function(data) {
      if($scope.data.isProcessing) return;
      $scope.data.isProcessing = true;
     
      var req = {
        method: 'POST',
        url: AppConst.API_URL+"update_login",
        data: "email="+data.email+"&new_password="+data.password+"&reset_code="+data.reset_code,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      };   

      $http(req).then(function(result){
          
          console.log(result);
          if(!result.data || !result.data.message || result.data.message != "Ok" ){
              var alertPopup = $ionicPopup.alert({
                    title: 'Reset failed!',
                    template: JSON.stringify(result.data.message)
                });
              $scope.data.isProcessing = false;
          } else{     
                var alertPopup = $ionicPopup.alert({
                  title: "Password updated",
                  template: 'You may login now with your new password'
                });
                setTimeout(function(){
                  $scope.data.isProcessing = false;
                  $state.go('loginpage', {}, {reload: true});
                },2000);
                
            }
         
         
          }, function(err){
              
              console.log(err)
              $scope.data.isProcessing = false;
              var alertPopup = $ionicPopup.alert({
                title: 'Request failed!',
                template: 'Request failed!'
            });
          });
      
      };
      
     

}) 