

angular.module('app.controllers.samplelogdetailsByCenter', [])
    .controller('sampleLogDetailsByCenterCtrl', function ($state,$scope, $rootScope, $filter, $timeout, $http, $ionicModal,
            $ionicHistory, $cordovaGeolocation, $cordovaCapture, $cordovaFileTransfer, $cordovaCamera,
            $cordovaBarcodeScanner, $ionicSideMenuDelegate, $ionicTabsDelegate, Samples, AppConst, $ionicPopup, AuthServiceUtilities, dataService) {

        $scope.$on('$ionicView.beforeEnter', function () {
            dataService.showLog("samplelogdetailsByCenter beforeEnter");     
            $scope.centerId = window.localStorage.getItem(LSNARMSCenterID);
            var attachURL = URLgetAllSamplesByCenter+"?centerid="+$scope.centerId;
            $scope.CenterSample = [];
            dataService.getData(attachURL).then(function (result) {
                dataService.showLog("CenterSample " + JSON.stringify(result));
                if (result.data && result.data.data) {
                    $scope.CenterSample = result.data.data;
                }
            });   
        });

            var meetNames = {};
           for(var i=0;i<AppConst.MEATS.length;i++)
           meetNames[AppConst.MEATS[i].value]=AppConst.MEATS[i].name;
            $scope.meetNames = meetNames;
            
            var typeNames={};
            for(var i=0;i<AppConst.TYPES.length;i++)
            typeNames[AppConst.TYPES[i].value]=AppConst.TYPES[i].name;
            $scope.typeNames = typeNames;
                     
      $scope.showSampleEdit = function (item) {
          
          localStorage.setItem(LSsampleLogDetails,JSON.stringify(item));
//          $ionicHistory.nextViewOptions({          
//            disableAnimate : true,
//          
//            });
        //$ionicHistory.clearCache();
        //$ionicHistory.clearHistory();

        //$ionicSideMenuDelegate.toggleLeft(); // Menu didn't close in this case  so toggle menu assign code.
        $ionicSideMenuDelegate.canDragContent(false);
          $state.go('hOME.sampleLogDetailEdit', {}, {reload: true});                    
          //hOME.sampleLogDetailEdit
      };

});

