

angular.module('app.controllers.samplelogdetailsBySearch', [])
        .controller('sampleLogDetailsBySearchCtrl', function ($state, $scope, $rootScope, $filter, $timeout, $http, $ionicModal,
                $ionicHistory, $cordovaGeolocation, $cordovaCapture, $cordovaFileTransfer, $cordovaCamera,
                $cordovaBarcodeScanner, $ionicSideMenuDelegate, $ionicTabsDelegate, Samples, AppConst, $ionicPopup, AuthServiceUtilities, dataService) {
            /*------------------- Start Searvices -------------------------*/
            $scope.$on('$ionicView.beforeEnter', function () {
                dataService.showLog("sampleLogDetailsCtrl beforeEnter");             
                $scope.selectables = [];
            });
            
            $scope.$on('$ionicView.enter', function () {
                dataService.showLog("sampleLogDetailsCtrl Enter");
                 $scope.openModal();
            });
            
            /*------------------- End Searvices -------------------------*/
            var meetNames = {};
            for (var i = 0; i < AppConst.MEATS.length; i++)
                meetNames[AppConst.MEATS[i].value] = AppConst.MEATS[i].name;
            $scope.meetNames = meetNames;

            var typeNames = {};
            for (var i = 0; i < AppConst.TYPES.length; i++)
                typeNames[AppConst.TYPES[i].value] = AppConst.TYPES[i].name;
            $scope.typeNames = typeNames;

            $scope.clearSearch = function () {
                $scope.searchQuery = '';
            };
            $ionicModal.fromTemplateUrl('templates/modal.html', {
                scope: $scope
            }).then(function (modal) {
                $scope.modal = modal;
            });
            $scope.openModal = function () {
                dataService.showLog("closemodal called");
                $scope.modal.show();
            }
            $scope.closeModal = function () {
                dataService.showLog("closemodal called");
                if($scope.selectables.length > 0){
                    //console.log("$scope.selectables exist");
                }else {
                    $scope.getCurrentMonthData();
                    //console.log("$scope.selectables not exist");                    
                }
                $scope.modal.hide();
            }

            $scope.createContact = function (u) {
                $scope.contacts.push({name: u.firstName + ' ' + u.lastName});
                $scope.modal.hide();
            };
            $scope.currentEndDate = new Date();
            $scope.currentStartDate = new Date();

            $scope.callbackSearchStartDate = function (date) {
                if (!date) {
                    console.log(date);
                    $scope.currentStartDate = date;
                } else {
                    $scope.currentStartDate = date;
                }
            };
            $scope.callbackSearchEndDate = function (date) {
                if (!date) {
                    console.log(date);
                    $scope.currentEndDate = date;
                } else {
                    $scope.currentEndDate = date;
                }
            };

            $scope.searchByDate = function () {
                dataService.getData("searchByDate called");
                $scope.centerId = window.localStorage.getItem(LSNARMSCenterID);
                //TODO unittesting with different dates apply conditions as per required
                //http://192.192.8.79:8080/narms-web/getAllSamplesByCenterAndDate?fromdate=2016-02-01T00:00:00.000Z&todate=2016-03-01T00:00:00.000Z&centerid=2
                //TODO CONVERT DATE FROMAT
                var urlSearchByDate = URLgetAllSamplesByCenterAndDate + "?centerid=" + $scope.centerId + "&fromdate=" + $scope.changeDateJSON($scope.currentStartDate) + "&todate=" + $scope.changeDateJSON($scope.currentEndDate);
                dataService.getData(urlSearchByDate);
                $scope.selectables = [];
                dataService.getData(urlSearchByDate).then(function (result) {
                    dataService.showLog("selectables " + JSON.stringify(result));
                    if (result.data && result.data.data)
                        $scope.selectables = result.data.data;
                    $scope.modal.hide();
                });
            };

            $scope.changeDateJSON = function (adate) {
                console.log(adate);
                console.log($filter('date')(adate, "yyyy-MM-dd"));
                if (adate != null && adate != "") {
                    var newFormat = $filter('date')(adate, "yyyy-MM-dd");
                    var from = newFormat + "T00:00:00.000Z"
                    return from;
                } else {
                    return null;
                }
            };

            $scope.showSampleEdit = function (item) {
                localStorage.setItem(LSsampleLogDetails, JSON.stringify(item));
//                $ionicHistory.nextViewOptions({
//                    disableAnimate: true,
//                });
                //$ionicHistory.clearCache();
                //$ionicHistory.clearHistory();

                //$ionicSideMenuDelegate.toggleLeft(); // Menu didn't close in this case  so toggle menu assign code.
                $ionicSideMenuDelegate.canDragContent(false);
                $state.go('hOME.sampleLogDetailEdit', {}, {reload: true});
                //hOME.sampleLogDetailEdit
            };
            
            $scope.getCurrentMonthData = function() {
                dataService.showLog("getCurrentMonthData called ");
                $scope.centerId = window.localStorage.getItem(LSNARMSCenterID);                
                var attachURL = URLgetAllSamplesByCenterAndDate +"?centerid=" + $scope.centerId + "&fromdate=2016-03-01T00:00:00.000Z&todate=2016-03-31T00:00:00.000Z"
                dataService.getData(attachURL).then(function (result) {
                    dataService.showLog("selectables " + JSON.stringify(result));
                    if (result.data && result.data.data)
                        $scope.selectables = result.data.data;
                });
            };
        });