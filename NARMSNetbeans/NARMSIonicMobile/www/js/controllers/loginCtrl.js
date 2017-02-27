angular.module('app.controllers.login', []) 
.controller('loginCtrl', function($scope, $ionicHistory ,AppConst,$state,AuthServiceUtilities,$http,$ionicPopup, dataService) {
     
    $scope.data = {};
            // username: muazzam.ali@aurotechcorp.com
            // password: 123
    AuthServiceUtilities.logout();
    var LoginURL = "login";
    dataService.showLog("login controller");
    
    $scope.model = "";
    $scope.clickedValueModel = "";
    $scope.removedValueModel = "";
    /* BeforeEnter loginCtrl */
    $scope.$on('$ionicView.beforeEnter', function () { 
        dataService.showLog("login controller beforeEnter");
        //$scope.options.hideBackButton = false;
        /*$ionicHistory.clearCache().then(function() {
    //now you can clear history or goto another state if you need
        $ionicHistory.clearHistory();
        $ionicHistory.nextViewOptions({ disableBack: true, historyRoot: true });
            $state.go('login');
        })*/
    });
    $scope.getTestItems = function (query) {
        if (query) {
            return {
                items: [
                    {id: "1", storeName: query + "storeName1", storeAddress: "storeAddress: " + query + "1"},
                    {id: "2", storeName: query + "storeName2", storeAddress: "storeAddress: " + query + "2"},
                    {id: "3", storeName: query + "storeName3", storeAddress: "storeAddress: " + query + "3"}]
            };
        }
        return {items: []};
    };

    $scope.itemsClicked = function (callback) {
        $scope.clickedValueModel = callback;
    };
    $scope.itemsRemoved = function (callback) {
        $scope.removedValueModel = callback;
    };
    
/**-----------------------------------------------------------------------------------------------
    *  Event Name: login
    *  Created By: <>
    *  Created Date:
    *  Modified By: <Name of modifier>
    *  Modified Date: -
    *  param : server - data of login form
    *  Propose: <To check login credientional.>        
----------------------------------------------------------------------------------------------------*/
    $scope.login = function(data) {
        dataService.showLog("login called");
        /*
        if(DEVELOPEROPTION == 1){
            $state.go('hOME.homePage', {}, {reload: true});
        */
            dataService.postLogin(LoginURL, data).then(function(result){
                dataService.showLog(JSON.stringify(result));                
                if(result.data.message != null ){
                    var alertPopup = $ionicPopup.alert({
                                                    title: 'Login failed!',
                                                    template: JSON.stringify(result.data.message)
                                                    });
                } else if(!result.data || !result.data.token) {
                    var alertPopup = $ionicPopup.alert({
                                                    title: 'Login failed!',
                                                    template: JSON.stringify(result.data.message)
                                                    });
                } else {     
                    console.log("load homepage");
                    //localStorage.setItem("UserLogin",JSON.stringify(result.data));
                    AuthServiceUtilities.storeUserCredentials(result.data);
                    $state.go('hOME.homePage', {}, {reload: true});                    
                }                                                
            }, function(error) {
                 // User made a wrong user name and pwd
                    dataService.showLog('Error req Login: ' + JSON.stringify(error));
                    var alertPopup = $ionicPopup.alert({
                      title: 'Login failed!',
                      template: 'Login failed!'
                    });                 
            });
     /*
      var req = {
        method: 'GET',
        url: AppConst.API_URL+"login",
        data: "email="+data.email+"&password="+data.password,
        headers: {'Content-Type': 'application/x-www-form-urlencoded'}
      };
    //  $state.go('hOME.homePage', {}, {reload: true});
      
/*
      $http(req).then(function(result){
          
          console.log(result);
          
          if(result.data.message != null ){
              var alertPopup = $ionicPopup.alert({
                    title: 'Login failed!',
                    template: JSON.stringify(result.data.message)
                });
          } else if(!result.data || !result.data.token) {
            var alertPopup = $ionicPopup.alert({
                    title: 'Login failed!',
                    template: JSON.stringify(result.data.message)
                });
          }else{     
                console.log("load homepage");
                AuthServiceUtilities.storeUserCredentials(result.data.token);
                $state.go('hOME.homePage', {}, {reload: true});
                
                AuthServiceUtilities.loadUserCredentials();
            }
         
         
          }, function(err){
              
              console.log(err)
              
              var alertPopup = $ionicPopup.alert({
                title: 'Login failed!',
                template: 'Login failed!'
            });
          });
            */
      
      };
      
    

})
