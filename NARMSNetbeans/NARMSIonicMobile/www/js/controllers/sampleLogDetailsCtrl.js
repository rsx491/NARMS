

angular.module('app.controllers.samplelogdetails', [])
.controller('sampleLogDetailsCtrl', function ($state, $scope, $rootScope,$filter, $timeout, $http, $ionicModal,
         $ionicHistory, $cordovaGeolocation, $cordovaCapture, $cordovaFileTransfer, $cordovaCamera,
          $cordovaBarcodeScanner, $ionicSideMenuDelegate, $ionicTabsDelegate, Samples, AppConst, $ionicPopup, AuthServiceUtilities, dataService) {
              
        /*------------------- Start Searvices -------------------------*/      
                 
            $scope.stores = AppConst.STORES;
            $scope.meats = AppConst.MEATS;
            $scope.types = AppConst.TYPES;
            $scope.organics = AppConst.ORGANICS;
            $scope.packedInStores = AppConst.PACKEDINSTORE;
            $scope.countries_text_multiple = 'Choose countries';
            $scope.val =  {single: null, multiple: null};
            
            
            $scope.someModel = null;
           // $scope.selectables = AppConst.STORES;
           var meetNames = {};
           for(var i=0;i<AppConst.MEATS.length;i++)
           meetNames[AppConst.MEATS[i].value]=AppConst.MEATS[i].name;
            $scope.meetNames = meetNames;
            
            var typeNames={};
            for(var i=0;i<AppConst.TYPES.length;i++)
            typeNames[AppConst.TYPES[i].value]=AppConst.TYPES[i].name;
            $scope.typeNames = typeNames;
              $scope.$on('$ionicView.beforeEnter', function(){
                dataService.showLog("sampleLogDetailsCtrl beforeEnter");
                 //$scope.getCountriesList();
                 $scope.mySample = [];
                $scope.currentMonth = new Date().getMonth()+1;
                dataService.getData("getAllSamples/usertoken/"+AuthServiceUtilities.username()+"/month/"+$scope.currentMonth).then(function(result){
                    dataService.showLog("mySample "+JSON.stringify(result));
                    if(result.data && result.data.data)
                        $scope.mySample = result.data.data;
                });                                              
              }); 
      $scope.showSampleEdit = function (item) {
          
          localStorage.setItem(LSsampleLogDetails,JSON.stringify(item));
          /*$ionicHistory.nextViewOptions({          
            disableAnimate : true,          
            });*/
        //$ionicHistory.clearCache();
        //$ionicHistory.clearHistory();

        //$ionicSideMenuDelegate.toggleLeft(); // Menu didn't close in this case  so toggle menu assign code.
        $ionicSideMenuDelegate.canDragContent(false);
          $state.go('hOME.sampleLogDetailEdit', {}, {reload: true});                    
          //hOME.sampleLogDetailEdit
      };
//------------------------- end services ---------------------            
    
  });      

