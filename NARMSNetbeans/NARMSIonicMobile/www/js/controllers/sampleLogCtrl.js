/*
 * To change thi license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module('app.controllers.samplelog', ['ngCordova'])
        .controller('sampleLogCtrl', function ($scope, $rootScope, $timeout, $http, $ionicModal, $ionicHistory, $cordovaCapture, $cordovaFileTransfer, $cordovaCamera, $cordovaGeolocation, $cordovaBarcodeScanner, $ionicSideMenuDelegate, Samples, AppConst, $ionicPopup, dataService) {
            $scope.selectedCountry = {};
            $scope.store = [];
            $scope.sample = [];
            
            $scope.centerLimit = {
                chicken : 0,
                Beef  : 0,
                Turkey : 0,
                Pork : 0
            };

            $scope.stores = AppConst.STORES;
            $scope.meats = AppConst.MEATS;
            $scope.types = AppConst.TYPES;
            $scope.organics = AppConst.ORGANICS;
            $scope.packedInStores = AppConst.PACKEDINSTORE;
            $scope.selectedCountry = {};
            $scope.countries = [];
            $scope.sample = [];
            $scope.sample.storeName = "";
            $scope.sample.storeAddress = "";            
            //$scope.selectableNames = localStorage.getItem('AllStores');

            dataService.showLog("SAMPLE LOG CONTROLLER");
            /* Get Countries list from constant */
            $scope.getCountriesList = function(){
                // $scope.countries = AppConst.COUNTRIES; 
                dataService.showLog("getCountriesList called");
                $scope.countriesData = AppConst.COUNTRIES;
                        $scope.countries = [];
                        angular.forEach($scope.countriesData, function(value, key) {                            
                            $scope.countries.push({
                                    countryName: value,
                                    value: key,
                                    text:value,
                                    name:value,
                                    id:key,
                                    checked : false
                                });                            
                        }); 
                        /* Countries list from Web-API service */
                /*dataService.getData(URLgetAllCountries).then(function (dataResponse) {
                    dataService.showLog(JSON.stringify(dataResponse));
                    if(typeof dataResponse.data.data !== 'undefined'){
                        var data = dataResponse.data.data;
                        $scope.countriesData = data;
                        $scope.countries = [];
                        angular.forEach($scope.countriesData, function(value, key) {                            
                            $scope.countries.push({
                                    countryName: value,
                                    value: key,
                                    text:value,
                                    name:value,
                                    id:key,
                                    checked : false
                                }); 
                        });
                    } else {
                        dataService.showLog('Else Error message in success URLgetAllCountries' + JSON.stringify(dataResponse));
                        var alertPopup = $ionicPopup.alert({
                               title: ' Sample !',
                               template:  dataResponse.data.error
                           });
                    } 
                }, function (error) {
                    dataService.showLog('PUted Data err of getUSERSTORESFROMCENTER' + JSON.stringify(error));
                });      */
                
            };
/**-----------------------------------------------------------------------------------------------
             *  Event Name: getStoreList
             *  Created By: <Ketan>
             *  Created Date:
             *  Modified By: <Name of modifier>
             *  Modified Date: -
             *  param : --
             *  Propose: get list of all store
             *
             ----------------------------------------------------------------------------------------------------*/
            $scope.getStoreList = function () {
                dataService.showLog("getStoreList called 11111"); 
                   //TODO need to confirm token response not tested.
                   //Get UserID from token 
                   //var userDetail = JSON.parse(localStorage.setItem("UserLogin",JSON.stringify(result)));
                   //TODO getUserCenterIDBySessionToken have not working.
                   var token = window.localStorage.getItem(LSNARMSIonicToken);
                   dataService.showLog(token);
                   var getUSERCENTERFROMTOKEN = URLgetUserCenterBySessionToken + "?tokenID="+ token ;
                   dataService.getData(getUSERCENTERFROMTOKEN).then(function (dataResponse) {
                       var data = dataResponse.data;                       
                       dataService.showLog("SAMPLE LOG CONTROLLER AAA"+JSON.stringify(dataResponse));                       
                       dataService.showLog("USER CENTER ID GOT BY TOKEN " + JSON.stringify(data));           
                       // TODO need to confirme userID field and response for data
                       if(typeof dataResponse.data !== 'undefined'){
                            $scope.userCenterID = data.usercenterid;

                             var getUSERSTORESFROMCENTER = URLgetAllStoresByCenterID + "?centerID="+ $scope.userCenterID ;
                             //get center from username
                             dataService.getData(getUSERSTORESFROMCENTER).then(function (dataResponse) {
                                 dataService.showLog(JSON.stringify(dataResponse));
                                 if(typeof dataResponse.data.data !== 'undefined'){
                                     var data = dataResponse.data.data;                                                
                                     $scope.selectableNames = data; 
                                     //$scope.selectableNames = $scope.dataResponse.data;                    
                                     localStorage.setItem(LSAllStores, JSON.stringify($scope.selectableNames));
                                 } else {
                                     dataService.showLog('Else Error message in success getUSERSTORESFROMCENTER' + JSON.stringify(dataResponse));
                                     var alertPopup = $ionicPopup.alert({
                                            title: ' Sample !',
                                            template:  dataResponse.data.error
                                        });                                     
                                 } 
                                 // bind data of source dynamically 
                             }, function (error) {
                                 dataService.showLog('PUted Data err of getUSERSTORESFROMCENTER' + JSON.stringify(error));
                             });                       
                       
                       } else {
                         //TODO Message  
                         dataService.showLog('PUted Data err of getUSERCENTERFROMTOKEN' + JSON.stringify(dataResponse));
                       }
                       // bind data of source dynamically 
                   }, function (error) {
                       dataService.showLog('PUted Data err of getUSERCENTERFROMTOKEN' + JSON.stringify(error));
                   });
            };

               //if (localStorage["AllStores"]) { 
               if("AllStores" in localStorage){
                   dataService.showLog("SAMPLE LOG CONTROLLER 1 ");
                   $scope.selectableNames = JSON.parse(localStorage.getItem(LSAllStores));        
               } else {                   
                   dataService.showLog("SAMPLE LOG CONTROLLER s");
                   // fetch store list from server.
                   $scope.getStoreList();
              
               }             
            
            //$scope.selectableNames = localStorage.getItem(LSAllStores);
               if (localStorage["AllBrands"]) {                          
                   // Get All brands from local storage if exist.
                   $scope.selectableAllBrands = JSON.parse(localStorage.getItem(LSAllBrands));        
               } else {
                   // Get All brands from server if not exist.
                   dataService.getData(URLgetAllBrands).then(function (dataResponse) {
                       var data = dataResponse.data.data;
                       dataService.showLog(JSON.stringify(data));    
                       if(typeof dataResponse.data.data !== 'undefined'){
                            $scope.selectableAllBrands = data; 
                            //$scope.selectableNames = $scope.dataResponse.data;                    
                            localStorage.setItem(LSAllStores, JSON.stringify($scope.selectableNames));
                            // bind data of source dynamically 
                        } else {
                            //TOD error message
                        }
                   }, function (error) {
                       dataService.showLog('PUted Data err of downloadDoc' + JSON.stringify(error));
                   });                   
               }   
  
            $scope.$on("$ionicView.afterLeave", function () {
                dataService.showLog("$ionicView.afterLeave called");
                //$ionicHistory.clearCache();
            });
            $scope.$on('$ionicView.beforeEnter', function () {
                dataService.showLog("$ionicView.beforeEnter called");
                // update campaigns everytime the view becomes active
                // (on first time added to DOM and after the view becomes active after cached
                $scope.getCountriesList();
                $scope.getCurrentPosition();
                //alert('$scope.getCurrentPosition');
            });
            // Load or initialize samples
            $scope.samples = Samples.all();

            // Grab the last active, or the first sample
            $scope.activesample = $scope.samples[Samples.getLastActiveIndex()];

/**-----------------------------------------------------------------------------------------------
    *  Event Name: showSampleModal
    *  Created By: <Ketan>
    *  Created Date:
    *  Modified By: <Name of modifier>
    *  Modified Date: -
    *  param : store - id, address and locations 
    *  Propose: <open Modal for sample initialize store values in to model.>        
----------------------------------------------------------------------------------------------------*/
            $scope.showSampleModal = function (store) {
                dataService.showLog("showSampleModal called"+JSON.stringify(store));     
                $scope.store = store;
                // Simple Validation to check store name is exist or not
                if(store.storeName){
                    $scope.storeID = store.id;
                    $scope.sample = {
                                            "type": [{
                                                       "value" : ''
                                                   }],
                                            "meat": [{
                                                       "value" : ''
                                                   }],
                                            "organic": [{
                                                       "value" : ''
                                                   }],
                                            "packedInStore":[{
                                                       "value" : ''
                                                   }],
                                            "datePurchase": '',
                                            "dateSaleBy": '',
                                            "dateProcess": '',
                                            "country": '',
                                            "barandCode": '',
                                            "notes": '',
                                            "status": true
                                };
                    $scope.sample.datePurchase = new Date();
                    $scope.sample.dateSaleBy = new Date();
                    $scope.sample.dateProcess = new Date();                    

                    //$scope.sample.id = Samples.getLastActiveIndex() + 1 ;
                    $scope.sampleModal.show();
                } else { 
                    var alertPopup = $ionicPopup.alert({
                            title: 'Error!',
                            template: 'Please fill up all details'
                        });                 
                } 
                
            };
/**-----------------------------------------------------------------------------------------------
    *  Event Name: showSampleImageModal
    *  Created By: <Ketan>
    *  Created Date:
    *  Modified By: <Name of modifier>
    *  Modified Date: -
    *  param : 
    *  Propose: <open Modal for sample upload image of front and back.>
----------------------------------------------------------------------------------------------------*/
            $scope.showSampleImageModal = function () {
                dataService.showLog("showSampleImageModal called");
                $scope.imageBackURI = "";
                $scope.imageFrontURI = "";
                $scope.imageBack = "";
                $scope.imageCaptured = "";
                $scope.sampleImageModal.show();
            };
            
            // Called to create a new sample
/**-----------------------------------------------------------------------------------------------
    *  Event Name: newSample
    *  Created By: <Ketan>
    *  Created Date:
    *  Modified By: <Name of modifier>
    *  Modified Date: -
    *  param : sample : object of sample with all sample model values.
    *  Propose: <checking meat type counts.>
----------------------------------------------------------------------------------------------------*/            
            $scope.newSample = function (sample) {
                dataService.showLog("newSample called");                               
                $scope.createNewSample(sample); 
            };
            //$scope.currentDate = new Date();
            $scope.minDate = new Date(2016, 0, 15);
            $scope.maxDate = new Date();
            var callbackDateSale;
            $scope.callbackofDatePurchase = function (val) {
                var startdate = new Date(new Date(val).getFullYear(),new Date(val).getMonth(),new Date(val).getDay());
                var enddate = new Date(new Date(callbackDateSale).getFullYear()+"-"+new Date(callbackDateSale).getMonth()+"-"+new Date(callbackDateSale).getDay());            
                //console.log($scope.sample.dateSaleBy);
              //   console.log($filter('date')($scope.sample.dateSaleBy, "dd/MM/yyyy"));
                if (!val) { 
                    console.log('Date not selected');
                } else {
                    console.log('Selected date is : ', val);
                }
            };
            
            $scope.callbackofDateSaleBy = function (val) {
                callbackDateSale = val;
                if (!val) { 
                    console.log('Date not selected callbackofDateSaleBy');
                } else {
                    console.log('Selected date is : ', val);
                }
            };

            $scope.callbackofDateProcess = function (val) {
                if (!val) { 
                    console.log('Date not selected callbackofDateProcess');
                } else {
                    console.log('Selected date is : ', val);
                }
            };

/**-----------------------------------------------------------------------------------------------
    *  Event Name: createNewSample
    *  Created By: <Ketan>
    *  Created Date:
    *  Modified By: <Name of modifier>
    *  Modified Date: -
    *  param : sample - object of sample with all sample model values.
    *  Propose: <Creating a new sample after checking meat type counts.>
----------------------------------------------------------------------------------------------------*/                        
            $scope.createNewSample = function (sample) {
                dataService.showLog("createNewSample called");
                dataService.showLog(JSON.stringify(sample));
                //createSample(sample);
                dataService.showLog(sample.type.value);                                

                console.log(sample.type.value +" &&  "+ sample.meat.value +" &&  "+ sample.organic.value +" &&  "+ sample.packedInStore.value +" &&  "+ sample.datePurchase +" &&  "+ sample.dateSaleBy +" &&  "+ sample.dateProcess +" &&  "+ sample.barandCode +" &&  " + sample.notes);
                $scope.sample = sample;                
                if(sample.type.value != null && sample.meat.value  != null && sample.organic.value != null && sample.packedInStore.value != null && sample.datePurchase != null && sample.dateSaleBy != null && sample.dateProcess != null && sample.barandCode != null && sample.notes != null){
                    //TODO need to test with storeAddress
                    // prepare JSON data to create sample
                    //$scope.sample.barCode
                     var data = {
                                    "type": sample.type.value,
                                    "meat": sample.meat.value,
                                    "organic": sample.organic.value,
                                    "packedInStore": sample.packedInStore.value,
                                    "datePurchase": sample.datePurchase,
                                    "dateSaleBy": sample.dateSaleBy,
                                    "dateProcess": sample.dateProcess,
                                    "countries": $scope.val.multiple,
                                    "brandCode": sample.brand.brandCode,
                                    "barCode": sample.barCode,
                                    "note": sample.notes 
                                }; 
                var addSampleURL = URLaddStoreSamples + $scope.storeID;
                console.log(JSON.stringify(data));
                //storeID
                dataService.postData(addSampleURL, data).then(function (dataResponse) {
                    dataService.showLog("Data " + addSampleURL +"----"+ JSON.stringify(data));
                    dataService.showLog("Data " + JSON.stringify(dataResponse));
                    $scope.dataResponseFromSample  = dataResponse.data.data;                      
                    // $scope.closeNewSample();
                    
                    if(typeof dataResponse.data.data !== 'undefined'){
                        var alertPopup = $ionicPopup.alert({
                            title: ' Sample !',
                            template: 'Sample Update successfully!'
                        }); 
                    
                        $scope.sample.id = $scope.dataResponseFromSample.id;
                        $scope.sample.status = false; 
                        $scope.closeNewSample();
                        $scope.showSampleImageModal();
                    } else {                       
                        $scope.dataResponseFromSample  = dataResponse.data;    
                        var alertPopup = $ionicPopup.alert({
                            title: ' Sample !',
                            template:  $scope.dataResponseFromSample.error
                        }); 
                    }             
                }, function (error) {
                    dataService.showLog('error addSample' + JSON.stringify(error));
                    var message;
                    if(error.status == 0){
                        message = error.data.message;
                    }else {
                        message = "Error is not identified."
                    }
                    var alertPopup = $ionicPopup.alert({
                        title: 'Sample Update failed!',
                        template: JSON.stringify(error.data.message)
                    });
                });   
                }else {
                   // alert('else');
                   var alertPopup = $ionicPopup.alert({
                            title: 'Error!',
                            template: 'Please fill up all details'
                        });
                }
               
            };
/**-----------------------------------------------------------------------------------------------
    *  Event Name: uploadFrontBackImage
    *  Created By: <Ketan>
    *  Created Date:
    *  Modified By: <Name of modifier>
    *  Modified Date: --
    *  param : --
    *  Propose: <Upload image.>
----------------------------------------------------------------------------------------------------*/                            
            // Called to select the given sample
            $scope.selectSample = function (sample, index) {
                $scope.activeSample = sample;
                Samples.setLastActiveIndex(index);
                // $scope.sideMenuController.close();
                //$ionicSideMenuDelegate.toggleRight();
            };

            // Create our modal
            $ionicModal.fromTemplateUrl('new-sample.html', function (modal) {
                $scope.projectModal = modal;
                $scope.sample = {
                    id: $scope.samples.length + 1
                }
            }, {
                scope: $scope
            }); 
 
            $scope.showAddNewStoreModel = function () {
                dataService.showLog("showAddNewStoreModel called");                
                $scope.getCurrentPosition();
                $scope.addNewStoreModal.show();
                // Populate current address in address field on current address.             
            }
            
            $scope.closeNewTask = function () {
                dataService.showLog("closeNewTask called");
                $scope.taskModal.hide();
            }

            $scope.closeNewSample = function () {
                dataService.showLog("closeNewSample called");
                $scope.sampleModal.hide();
            }
            $scope.closeNewSampleImage = function () {
                dataService.showLog("closeNewSampleImage called");
                $scope.sampleImageModal.hide();
            }
            $scope.closeAddNewStore = function () {
                dataService.showLog("closeNewSample called");
                $scope.addNewStoreModal.hide();
            }
 
            $ionicModal.fromTemplateUrl('new-sample.html', function (modal) {
                $scope.sampleModal = modal;
            }, {
                scope: $scope
            });

            $ionicModal.fromTemplateUrl('new-sample-image.html', function (modal) {
                $scope.sampleImageModal = modal;
            }, {
                scope: $scope
            });
            $ionicModal.fromTemplateUrl('add-new-store.html', function (modal) {
                $scope.addNewStoreModal = modal;
            }, {
                scope: $scope
            });

            /**-----------------------------------------------------------------------------------------------
             *  Event Name:fileAttach click event
             *  Created By: <Ketan>
             *  Created Date:
             *  Modified By: <Name of modifier>
             *  Modified Date: -
             *  Propose: <On clicking Attachment image in , it will open browse file dialog after selecting file data of name, address and age will updated on server with image/file>
             ----------------------------------------------------------------------------------------------------*/
            $scope.getImageForAttach = function () {
                dataService.showLog("getImageForAttach called");
                var options = {
                    quality: 50,
                    destinationType: navigator.camera.DestinationType.FILE_URI,
                    sourceType: navigator.camera.PictureSourceType.DATA_URL
                            //encodingType: navigator.camera.EncodingType.JPEG
                };
                // open camera with plugin factory method of ng-cordova
                $cordovaCamera.getPicture(options).then(function (imageURI) {
                    //console.log(imageURI);
                    $scope.imageCaptured = imageURI;
                    var options = new FileUploadOptions();
                    options.fileKey = "file";
                    options.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1);
                    options.mimeType = "image/jpeg";

                    // name, address, age
                    var txtFileUpload = imageURI.substr(imageURI.lastIndexOf('/') + 1);
                    var params = new Object();
                    params.documentName = txtFileUpload;
                    //name, address, age, file
                    
                    options.headers = {
                        Connection: 'close'
                    };
                    options.chunkedMode = true;
                    // upload file
                    //$scope.fileUpload(AppConst.API_HOST,imageURI, options);

                }, function (err) {
                    // error
                });
            };

            /* upload uri and set parameter for file upload */
            /**-----------------------------------------------------------------------------------------------
             *  Event Name: fileUpload
             *  Created By: <Ketan>
             *  Created Date:
             *  Modified By: <Name of modifier>
             *  Modified Date: -
             *  param : server - serverpath , imageURI -Contais url of the image , options - attached with params
             *  Propose: <uploadPhoto will take the values from popup and do upload file>
             *      when uploading file with plugin than no need to ajax/$http call from javascript just pass proper data and it will working with post method.
             ----------------------------------------------------------------------------------------------------*/
//            $scope.fileUpload = function (server, filePath, options) {
//                dataService.showLog("fileUpload called");                 
//            };

            /**-----------------------------------------------------------------------------------------------
             *  Event Name: getLocationList
             *  Created By: <Ketan>
             *  Created Date:
             *  Modified By: <Name of modifier>
             *  Modified Date: -
             *  param :
             *  Propose: Get current Location's latitude and longitude from plugin factory method
             *
             ----------------------------------------------------------------------------------------------------*/

            $scope.getCurrentPosition = function () {
                dataService.showLog("getCurrentPosition called");
                var posOptions = {timeout: 10000, enableHighAccuracy: true};

                $cordovaGeolocation
                        .getCurrentPosition(posOptions)
                        .then(function (position) {
                            dataService.showLog(position.coords.latitude);
                            dataService.showLog(position.coords.longitude);
                            $scope.getCurrenPositionLat = position.coords.latitude;
                            $scope.getCurrenPositionLong = position.coords.longitude;
                            $scope.getLocationList($scope.getCurrenPositionLat, $scope.getCurrenPositionLong);
                            /*
                             var geocoder;
                             geocoder = new google.maps.Geocoder();
                             var latlng = new google.maps.LatLng($scope.getCurrenPositionLat, $scope.getCurrenPositionLong);
                             
                             geocoder.geocode(
                             {'latLng': latlng}, 
                             function(results, status) {
                             if (status == google.maps.GeocoderStatus.OK) {
                             console.log(JSON.stringify(results));
                             if (results[0]) {
                             var add= results[0].formatted_address ;
                             var  value=add.split(",");
                             
                             count=value.length;
                             country=value[count-1];
                             state=value[count-2];
                             city=value[count-3];
                             alert("city name is: " + city);
                             }
                             else  {
                             alert("address not found");
                             }
                             }
                             else {
                             alert("Geocoder failed due to: " + status);
                             }
                             }
                             );*/



                        }, function (err) {
                            console.log(JSON.stringify(err));
                        });
            };
            /**-----------------------------------------------------------------------------------------------
             *  Event Name: getLocationList
             *  Created By: <Ketan>
             *  Created Date:
             *  Modified By: <Name of modifier>
             *  Modified Date: -
             *  param : lat -Latitude, Long -Longatude
             *  Propose: get list of all locations detail nearby provided from lat-long
             *
             ----------------------------------------------------------------------------------------------------*/
            $scope.getLocationList = function (lat, long) {
                dataService.showLog("getLocationList called");
                //  https://maps.googleapis.com/maps/api/geocode/json?latlng=40.714224,-73.961452
                var req = {
                    method: 'GET',
                    url: 'https://maps.googleapis.com/maps/api/geocode/json?latlng=' + lat + ',' + long,
                };
                $http(req).then(function (result) {
                //console.log('location');
                  $scope.locationResults = result.data;                                
                  $scope.storeAddress = $scope.locationResults.results[0].formatted_address; 
                }, function (err) {
                    console.log(JSON.stringify(err));
                });
            };
             
            $scope.uploadImage = function (aImageURI,aFrontBack) {
                dataService.showLog("uploadImage");
                
                var SampleID = $scope.sample.id;
                var imageURI = aImageURI;
                var frontBack = aFrontBack;
                
                var options = new FileUploadOptions();
                options.fileKey = "file";
                //options.fileName = imageURI.substr(imageURI.lastIndexOf('/') + 1);
                options.mimeType = "image/jpeg";

                // name, address, age
                //var txtFileUpload = imageURI.substr(imageURI.lastIndexOf('/') + 1);
                var params = new Object();
                //params.data = JSON.stringify(data);
                options.params = params;

                options.headers = {
                    Connection: 'close'
                };
                options.chunkedMode = true;
                // upload file
                var sampleAddURL = AppConst.API_URL + URLuploadSampleCapture + frontBack + "/" + SampleID;                 
                //$scope.frontImage = $scope.fileUpload(sampleAddURL, imageURI, options);  
                // upload file form cordova file-transfer plugin with factoy method of ng-cordova -> scannerApp.plugins.fileTransfer
                $cordovaFileTransfer.upload(sampleAddURL, imageURI, options)
                        .then(function (result) {
                            dataService.showLog(JSON.stringify(result));                            
                            var alertPopup = $ionicPopup.alert({
                                title: ' Sample !',
                                template: frontBack + ' image Update successfully !'
                            });
                            //return true;
                        }, function (err) {
                            // Error
                            dataService.showLog('error fileupload' + JSON.stringify(error));
                            var alertPopup = $ionicPopup.alert({
                                title: frontBack + ' image Update failed !',
                                template: JSON.stringify(error.data.message)
                            });
                            return false;

                        }, function (progress) {
                            dataService.showLog(progress);
                            // constant progress updates
                        }); 
            };

            $scope.getImageFront = function () {
                dataService.showLog("getImageFront called ");
                var options = {
                    quality: 20,
                    destinationType: navigator.camera.DestinationType.FILE_URI,
                    sourceType: navigator.camera.PictureSourceType.DATA_URL
                    //sourceType: navigator.camera.PictureSourceType.PHOTOLIBRARY
                            //encodingType: navigator.camera.EncodingType.JPEG
                };
                // open camera with plugin factory method of ng-cordova
                $cordovaCamera.getPicture(options).then(function (imageURI) {
                    //console.log(imageURI);
                    $scope.imageFrontURI = imageURI;
                    $scope.imageFront = imageURI.substr(imageURI.lastIndexOf('/') + 1);                    
                    $scope.uploadImage($scope.imageFrontURI,"front");

                }, function (err) {
                    // error
                });
            };
            
            $scope.getImageBack = function () {
                dataService.showLog("getImageBack called ");
                var options = {
                    quality: 20,
                    destinationType: navigator.camera.DestinationType.FILE_URI,
                    sourceType: navigator.camera.PictureSourceType.DATA_URL
                    //sourceType: navigator.camera.PictureSourceType.PHOTOLIBRARY
                            //encodingType: navigator.camera.EncodingType.JPEG
                };
                // open camera with plugin factory method of ng-cordova
                $cordovaCamera.getPicture(options).then(function (imageURI) {
                    
                    $scope.imageBackURI = imageURI;
                    $scope.imageBack = imageURI.substr(imageURI.lastIndexOf('/') + 1); 
                    $scope.uploadImage($scope.imageBackURI,"back");
                      

                }, function (err) {
                    // error
                });
            };
/**-----------------------------------------------------------------------------------------------
 *  Event Name: captureBarcode
 *  Created By: <Ketan>
 *  Created Date:
 *  Modified By: <Name of modifier>
 *  Modified Date: -
 *  param : 
 *  Propose: Will return data from scanned barcode.
 *
 ----------------------------------------------------------------------------------------------------*/            
            $scope.captureBarcode = function () {
                // Barcode scanner without link just offline barcode
                if (navigator.userAgent.match(/(iPhone|iPod|iPad|Android|BlackBerry|IEMobile)/)) {
                    //console.log('if');                
                    dataService.showLog("captureBarcode called ");
                    $cordovaBarcodeScanner
                      .scan()
                      .then(function(barcodeData) {
                        // Success! Barcode data is here
         
                        $scope.sample.barCode = barcodeData.text;
                            //alert(JSON.stringify(barcodeData));
                        }, function(error) {
                          // An error occurred
                          var alertPopup = $ionicPopup.alert({
                                  title: 'Barcode !',
                                  template: 'Error in barcode scan !'
                              }); 
                              //alert(JSON.stringify(error));
                      });           
                  } else {
                    console.log('else');
                }
            };
/**-----------------------------------------------------------------------------------------------
 *  Event Name: addStore
 *  Created By: <Ketan>
 *  Created Date: 15/02/2016
 *  Modified By: <Name of modifier>
 *  Modified Date: -
 *  param : 
 *  Propose: create a new store from model.
 *
 ----------------------------------------------------------------------------------------------------*/                         
            $scope.addNewStore = function (storeName,storeAddress) {
                dataService.showLog("addNewStore called "+storeName + "++"+storeAddress);
                //dataService.showLog(JSON.stringify(addStore));
                if (storeName != null && storeAddress != null) {

                    var addStoreURL = URLaddNewStore + $scope.userCenterID;
                    var data = {
                        "storeName": storeName,
                        "storeAddress": storeAddress
                    };

                    dataService.postData(addStoreURL, data).then(function (dataResponse) {
                        dataService.showLog("Data " + addStoreURL + "----" + JSON.stringify(data));
                        dataService.showLog("Data " + JSON.stringify(dataResponse));
                        $scope.dataResponseFromSample = dataResponse.data.data;
                        // $scope.closeNewSample();
                        if(typeof dataResponse.data.data !== 'undefined'){
                            var alertPopup = $ionicPopup.alert({
                                title: ' Store !',
                                template: 'Store Added successfully!'
                            });
                            $scope.getStoreList();
                            $scope.closeAddNewStore();
                        } else {
                            // error message popup
                            dataService.showLog("Error From  "+ addStoreURL);
                        }
                    }, function (error) {
                        dataService.showLog('error addSample' + JSON.stringify(error));
                        var message;
                        if (error.status == 0) {
                            message = error.data.message;
                        } else {
                            message = "Error is not identified."
                        }
                        var alertPopup = $ionicPopup.alert({
                            title: 'Sample Update failed!',
                            template: JSON.stringify(error.data.message)
                        });
                    });
                } else {
                    // alert('else');
                    var alertPopup = $ionicPopup.alert({
                        title: 'Error!',
                        template: 'Please fill up all details'
                    });
                }
            }
            
            $scope.chageMeatType = function (sampleMeat){
                dataService.showLog("chageMeat called ");
                dataService.showLog("chageMeat called "+ sampleMeat.meat.value);
                $scope.sample.types = [];
                if (sampleMeat.meat.value == 1){
                    $scope.types = AppConst.TYPES1;
                } else if (sampleMeat.meat.value == 2){
                    $scope.types = AppConst.TYPES2;
                } else if (sampleMeat.meat.value == 3){
                    $scope.types = AppConst.TYPES3;
                } else if (sampleMeat.meat.value == 4){
                    $scope.types = AppConst.TYPES4;
                }  
            }
            
            $scope.getData = function (){
                return fancySelect.getValue();
            };
            $scope.countries_text_multiple = 'Choose countries';
            $scope.val =  {single: null, multiple: null};
        })

