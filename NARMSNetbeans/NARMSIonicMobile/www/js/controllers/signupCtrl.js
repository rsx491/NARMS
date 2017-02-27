/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */


angular.module('app.controllers.signup', []) 

.controller('signupCtrl', function($scope,AppConst,$state,AuthServiceUtilities,$http,$ionicPopup, $timeout) {
    $scope.data = {};
    $scope.states = AppConst.STATES;
    $scope.usertypes=AppConst.USERTYPES;
    $scope.processingForm = false;

     $scope.signup = function(data) {
         console.log("email="+data.email+"&password="+data.password+"&firstName="+data.firstname+"&lastName="+data.lastname+"&stateName="+data.state.name+"&userType="+data.usertype.code);
     	if($scope.processingForm) return;
     	$scope.processingForm = true;
      var req = {
        method: 'POST',
        url: AppConst.API_URL+"register",
        data: "email="+data.email+"&password="+data.password+"&firstName="+data.firstname+"&lastName="+data.lastname+"&stateName="+data.state.name+"&usertype="+data.usertype.code,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      };
      //$state.go('hOME.homePage', {}, {reload: true});
      

      $http(req).then(function(result){
          
          console.log(result);
          
          if(result.data.error != null ){
              var alertPopup = $ionicPopup.alert({
                    title: 'Login failed!',
                    template: JSON.stringify(result.data.error)
                });
              $scope.processingForm = false;
          } else if(!result.data || !result.data.message) {
            var alertPopup = $ionicPopup.alert({
                    title: 'Login failed!',
                    template: "Invalid server response"
                });
            $scope.processingForm = false;
          }else{     
                var alertPopup = $ionicPopup.alert({
		          title: 'Registration Succesful',
		          template: 'Please login with your new account'
		        });
		        
		        $timeout(function(){
		        	$scope.processingForm = false;
		        	$state.go('loginpage', {}, {reload: true});
		        },2000);
                
            }
         
         
          }, function(err){
              
              console.log(err);
              $scope.processingForm = false;
              var alertPopup = $ionicPopup.alert({
                title: 'Login failed!',
                template: 'Login failed!'
            });
          });
      
      };
})