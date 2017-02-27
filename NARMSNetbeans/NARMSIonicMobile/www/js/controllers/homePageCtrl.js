
/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module('app.controllers.homepage', []) 

.controller('homePageCtrl', function($scope, $cordovaGeolocation, dataService,$ionicLoading, AuthServiceUtilities,dataService) {
    $scope.showLoading = function() {
              $ionicLoading.show({
                template: 'Loading...'
              });
            };
            $scope.hideLoading = function(){
              $ionicLoading.hide();
            };
      $scope.model = "";
      $scope.clickedValueModel = "";
      $scope.removedValueModel = "";
      // TODO navigation with multiple locations.
      $scope.navigationToRoute = function (data){
          dataService.showLog("navigationToRoute called ");
          //$scope.showLoading();
          launchnavigator.navigate(
              "London, UK",
              "Manchester, UK",
              function(){
                  dataService.showLog("Plugin success");
                  $scope.hideLoading();
              },
              function(error){
                   dataService.showLog("Plugin error: "+ error);
              });
      };
      // Getlocation 
    $scope.getLocation = function () {

      $cordovaGeolocation
        .getCurrentPosition({timeout: 10000, enableHighAccuracy: false})
        .then(function (position) {
          console.log("position found");
          $scope.position = position; 
        }, function (err) {
          console.log("unable to find location");
          $scope.errorMsg = "Error : " + err.message;
        });
    };
    /* BeforeEnter homePageCtrl */
        $scope.$on('$ionicView.beforeEnter', function(){
            // Set username
            dataService.getData(URLgetActiveUserByToken+"?tokenID="+AuthServiceUtilities.username()).then(function(result){
                dataService.showLog("URLgetActiveUserByToken from homePageCtrl "+JSON.stringify(result));
                if(result.data && result.data.data && result.data.data.firstName)
                    $scope.username = result.data.data.firstName;
                    $scope.usertype = result.data.data.usertype;
                    window.localStorage.setItem(LSNARMSuserType,$scope.usertype);
                                                                                                      
                });
            // Set centerName,CenterId
            dataService.getData(URLgetUserCenterBySession+"?tokenID="+AuthServiceUtilities.username()).then(function(result){
                dataService.showLog("URLgetUserCenterBySession from homePageCtrl "+JSON.stringify(result));
                var centerlimit = 0;
                var centercount = 0;
                var centerremaining = 0;
                if(result.data && result.data.data && result.data.data.centerName)
                    $scope.centername = result.data.data.centerName;
                if(result.data && result.data.data && result.data.data.id){
                    $scope.centerid = result.data.data.id;
                    // Set cetnerId in localStorage
                    window.localStorage.setItem(LSNARMSCenterID,$scope.centerid);
                    dataService.getData(URLgetCurrentSampleCountByCenterID+"?centerid="+$scope.centerid).then(function(result){
                        if(result.data && result.data.data && result.data.data.Beef)
                            centercount += result.data.data.Beef;
                        if(result.data && result.data.data && result.data.data.Chicken)
                           centercount += result.data.data.Chicken;
                        if(result.data && result.data.data && result.data.data.Pork)
                           centercount += result.data.data.Pork;
                        if(result.data && result.data.data && result.data.data.Turkey)
                           centercount += result.data.data.Turkey;
                        dataService.getData(URLgetCenterLimits+"/"+$scope.centerid).then(function(result){
                           if(result.data && result.data.data){
                               for(var i =0; i < result.data.data.length;i++)
                               {
                                  centerlimit += result.data.data[i].totalLimit;
                               }
                               centerremaining = centerlimit - centercount;
                               $scope.centerremaining = centerremaining;
                            }
                        });
                     });
                                                                                                        
                }
            });
            
            $scope.samplingDate = new Date();
            var monthend = new Date($scope.samplingDate.getFullYear(),($scope.samplingDate.getMonth()+1),1,0,0,0,0);
            var diff = monthend.getTime() - $scope.samplingDate.getTime();
            $scope.daysleft = parseInt(diff/86400000);
        });

})

    