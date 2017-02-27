angular.module('app.controllers.activity', [])

.controller('activityLogCtrl', function($scope, $cordovaGeolocation, dataService, AuthServiceUtilities) {
            $scope.$on('$ionicView.beforeEnter', function(){
                dataService.getData(URLgetActiveUserByToken+"?tokenID="+AuthServiceUtilities.username()).then(function(result){
                    if(result.data && result.data.data && result.data.data.firstName)
                        $scope.username = result.data.data.firstName;
                    dataService.getData(URLgetSampleCountByUser+"?tokenID="+AuthServiceUtilities.username()).then(function(result){
                        if(result.data && result.data.data && result.data.data.Beef)
                           $scope.userBeefCount = result.data.data.Beef;
                        else
                           $scope.userBeefCount = 0;
                        if(result.data && result.data.data && result.data.data.Chicken)
                          $scope.userChickenCount = result.data.data.Chicken;
                        else
                          $scope.userChickenCount = 0;
                        if(result.data && result.data.data && result.data.data.Pork)
                          $scope.userPorkCount = result.data.data.Pork;
                        else
                          $scope.userPorkCount = 0;
                        if(result.data && result.data.data && result.data.data.Turkey)
                          $scope.userTurkeyCount = result.data.data.Turkey;
                        else
                          $scope.userTurkeyCount = 0;
                     });

                });
                dataService.getData(URLgetUserCenterBySession+"?tokenID="+AuthServiceUtilities.username()).then(function(result){
                    dataService.showLog("URLgetUserCenterBySession "+JSON.stringify(result))
                    if(result.data && result.data.data && result.data.data.centerName)
                        $scope.centername = result.data.data.centerName;
                    if(result.data && result.data.data && result.data.data.id)
                        $scope.centerid = result.data.data.id;
                    dataService.getData(URLgetCurrentSampleCountByCenterID+"?centerid="+$scope.centerid).then(function(result){
                        if(result.data && result.data.data && result.data.data.Beef)
                           $scope.centerBeefCount = result.data.data.Beef;
                        else
                           $scope.centerBeefCount = 0;
                        if(result.data && result.data.data && result.data.data.Chicken)
                           $scope.centerChickenCount = result.data.data.Chicken;
                        else
                           $scope.centerChickenCount = 0;
                        if(result.data && result.data.data && result.data.data.Pork)
                           $scope.centerPorkCount = result.data.data.Pork;
                        else
                           $scope.centerPorkCount = 0;
                        if(result.data && result.data.data && result.data.data.Turkey)
                           $scope.centerTurkeyCount = result.data.data.Turkey;
                        else
                           $scope.centerTurkeyCount = 0;
                    });
                    dataService.getData(URLgetCenterLimits+"/"+$scope.centerid).then(function(result){
                        if(result.data && result.data.data){
                            var centerlimits = {};
                            for(var i =0; i < result.data.data.length;i++)
                            {
                                centerlimits[result.data.data[i].type] = result.data.data[i].totalLimit;
                            }
                            $scope.centerlimits = centerlimits;
                        }
                    });
                });
                       
                $scope.samplingDate = new Date();
                       
            });

            
            
})