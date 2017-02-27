

angular.module('app.controllers.samplelogdetails', [])
.controller('sampleLogDetailsCtrl', function ($scope, $rootScope,$filter, $timeout, $http, $ionicModal,
         $ionicHistory, $cordovaGeolocation, $cordovaCapture, $cordovaFileTransfer, $cordovaCamera,
          $cordovaBarcodeScanner, $ionicSideMenuDelegate, Samples, AppConst, $ionicPopup, AuthServiceUtilities, dataService) {
              
                              
                   
            
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
        
        $scope.getCountriesList = function(){
                // $scope.countries = AppConst.COUNTRIES; 
                dataService.showLog("getCountriesList called");
                $scope.countriesData1 = AppConst.COUNTRIES;
                        $scope.countriesEdit = [];
                        angular.forEach($scope.countriesData1, function(value, key) {                            
                            $scope.countriesEdit.push({
                                    countryName: value,
                                    value: key,
                                    text:value,
                                    name:value,
                                    id:key,
                                    checked : false
                                });                            
                        });  
            };
            
            $scope.getCountriesListSelect = function(selectedItem){
                // $scope.countries = AppConst.COUNTRIES; 
                dataService.showLog("getCountriesListSelect called");
                $scope.countriesData1 = AppConst.COUNTRIES;
                        $scope.countriesEdit = [];
                        $scope.checkFlag = false;
                        $scope.name1 = [];
                        angular.forEach($scope.countriesData1, function(value, key) {                            
                            for(var i=0;i<selectedItem.length;i++)
                            {
                              //  console.log(key+"=="+selectedItem[i])
                                if (key==selectedItem[i]){
                                    console.log(key+"=="+selectedItem[i]+"=Value = "+value)
                                    console.log("true");
                                    $scope.name1.push(value);
                                    $scope.checkFlag = true;
                                    break;
                                } else {
                                    console.log("false");
                                    $scope.checkFlag = false;
                                }
                            }
                            $scope.countriesEdit.push({
                                    countryName: value,
                                    value: key,
                                    text:value,
                                    name:value,
                                    id:key,
                                    checked : $scope.checkFlag
                                });     
                                $scope.name = $scope.name1;
                        });  
            };
        /*------------------- Start Searvices -------------------------*/      
              
              $scope.$on('$ionicView.beforeEnter', function(){
                dataService.showLog("sampleLogDetailsCtrl beforeEnter");
                 //$scope.getCountriesList();
                
                 dataService.getData("getAllCenters").then(function(result){
                     dataService.showLog("CenterSample "+JSON.stringify(result));
                    if(result.data && result.data.data)
                        $scope.CenterSample = result.data.data;
                });
               
                
                dataService.getData("getAllSamples").then(function(result){
                    dataService.showLog("selectables "+JSON.stringify(result));
                    if(result.data && result.data.data)
                        $scope.selectables = result.data.data;
                });
                $scope.currentMonth = new Date().getMonth()+1;
                dataService.getData("getAllSamples/usertoken/"+AuthServiceUtilities.username()+"/month/"+$scope.currentMonth).then(function(result){
                    dataService.showLog("mySample "+JSON.stringify(result));
                    if(result.data && result.data.data)
                        $scope.mySample = result.data.data;
                });
                
                    $scope.centerId = window.localStorage.getItem('NARMSCenterID');
                   dataService.getData("getAllSamplesByCenter?centerid="+$scope.centerId).then(function(result){
                       dataService.showLog("getAllSamplesByCenter?centerid=========="+JSON.stringify(result));
                    if(result.data && result.data.data)
                        $scope.selectableNames = result.data.data;
                });
                
                  if (localStorage["AllBrands"]) {                          
                   // Get All brands from local storage if exist.
                   $scope.selectableAllBrands = JSON.parse(localStorage.getItem("AllStores"));        
                    } else {
                        // Get All brands from server if not exist.
                        dataService.getData(URLgetAllBrands).then(function (dataResponse) {
                            var data = dataResponse.data.data;
                            dataService.showLog(JSON.stringify(data));    
                            if(typeof dataResponse.data.data !== 'undefined'){
                                 $scope.selectableAllBrands = data; 
                                 //$scope.selectableNames = $scope.dataResponse.data;                    
                                 localStorage.setItem('AllStores', JSON.stringify($scope.selectableNames));
                                 // bind data of source dynamically 
                             } else {
                                 //TOD error message
                             }
                        }, function (error) {
                            dataService.showLog('PUted Data err of downloadDoc' + JSON.stringify(error));
                        });                   
                    }   
                
                
                             
              }); 
     
        /*------------------- End Searvices -------------------------*/             
        
        
              /**-----------------------------------------------------------------------------------------------
    *  Event Name: showSampleModal
    *  Created By: 
    *  Created Date:
    *  Modified By: <Name of modifier>
    *  Modified Date: -
    *  param : store - id, address and locations 
    *  Propose: <open Modal for sample initialize store values in to model.>        
----------------------------------------------------------------------------------------------------*/

   $ionicModal.fromTemplateUrl('edit-sample.html', function (modal) {
                        $scope.sampleModal = modal;
                    }, {
                        scope: $scope
            });
            
            // display modal 
            $scope.showSampleModal = function (item) {
                $scope.getCountriesList();
                        dataService.showLog(JSON.stringify(item));
                          
               $scope.countriesData = item.countries;
               //$scope.countriesEdit = [];
               $scope.val.multiple =null;
               $scope.countriesEdit = [];
                //console.log(item.countries);
                //$scope.getCountriesListSelect(item.countries);
                    if($scope.countriesData){
                        for (var j=0; j < $scope.countriesData.length; j++) {
                            console.log($scope.countriesData[j]);
                        }
                        console.log(JSON.stringify($scope.countriesData));
                        $scope.getCountriesListSelect($scope.countriesData);
                    } else{
                        $scope.getCountriesList();
                    }
                        
                        //var found = $filter('filter')($scope.countries, {countryCode: $scope.countriesData[0]}, true);
                        
                        
                        //dataService.showLog(JSON.stringify($scope.countriesEdit));
                        $scope.meat1 = item.meat;
                        //$scope.sample.meat = item.meat;
                        if(item.datePurchase){
                            $scope.datePurchase = new Date(item.datePurchase);
                        } else {
                            $scope.datePurchase =  "";
                        }
                        if(item.dateSaleBy){
                            $scope.dateSaleBy = new Date(item.dateSaleBy);
                        } else {
                            $scope.dateSaleBy = "";
                        }
                        if(item.dateProcess){                        
                            $scope.dateProcess = new Date(item.dateProcess);
                        } else {
                            $scope.dateProcess = "";
                        }
                        $scope.type1 = item.type;
                        $scope.organic1 = item.organic;
                        $scope.packedInStore1 = item.packedInStore;
                        var found = $filter('filter')($scope.selectableAllBrands, {brandCode: item.brandCode}, true);
                            
                                
                                //console.log(JSON.stringify(found[0]));
               $scope.sample = {
                                            "id":item.id,
                                            "type": [],
                                            "meat": [],
                                            "organic": [],
                                            "packedInStore":[],
                                            "datePurchase": $scope.datePurchase,
                                            "dateSaleBy": $scope.dateSaleBy,
                                            "dateProcess": $scope.dateProcess,
                                            "countries": item.countries,
                                            "barandCode": [{
                                                    "brand" : item.barandCode
                                                }],
                                            "barCode": item.barCode,
                                            "notes": item.note,
                                            "store": item.store,
                                            "brand":[],
                                            "status": true
                                };
                                $scope.sample.brand = found[0];
                                
                                console.log($scope.sample.meat);
                                $scope.chageMeatType(item);
                                
                                $scope.val.multiple = item.countries;
                                $scope.sample.meat = item.meat;
                                $scope.sample.organic = item.organic;
                                //$scope.sample.type = item.type;
                                $scope.sample.packedInStore = item.packedInStore;                                 
                                
             
                $scope.sampleModal.show();
            }

            $scope.chageMeatType = function (sample){
                dataService.showLog("chageMeat called "+sample.meat);
                dataService.showLog("chageMeat called "+sample.type);
                
                $scope.sample.types = [];
                $scope.types = [];
                if (sample.meat == 1){
                    $scope.types = AppConst.TYPES1;
                } else if (sample.meat == 2){
                    $scope.types = AppConst.TYPES2;
                } else if (sample.meat == 3){
                    $scope.types = AppConst.TYPES3;
                } else if (sample.meat == 4){
                    $scope.types = AppConst.TYPES4;
                }  
                $scope.sample.type = sample.type;
            }
            
            $scope.chageMeatType1 = function (sample){
                dataService.showLog("chageMeat1 called "+sample);
                
                $scope.sample.types = [];
                $scope.types = [];
                if (sample == 1){
                    $scope.types = AppConst.TYPES1;
                } else if (sample == 2){
                    $scope.types = AppConst.TYPES2;
                } else if (sample == 3){
                    $scope.types = AppConst.TYPES3;
                } else if (sample == 4){
                    $scope.types = AppConst.TYPES4;
                }  
                $scope.sample.type = sample.type;
            }            
             //close modal
              $scope.closeNewSample = function () {

                dataService.showLog("closeNewSample called");
                $scope.sampleModal.hide();
            }
            

            
/**-----------------------------------------------------------------------------------------------
    *  Event Name: updateSample
    *  Created By: <Ketan>
    *  Created Date:
    *  Modified By: <Name of modifier>
    *  Modified Date: -
    *  param : sample - object of sample with all sample model values.
    *  Propose: <Update Sample.>
----------------------------------------------------------------------------------------------------*/                        
            $scope.updateSample = function (sample) {
                dataService.showLog("updateSample called");

                dataService.showLog(JSON.stringify(sample));
                /*
                 * {"type":"4","meat":"1","organic":"2","packedInStore":"1","datePurchase":"2016-02-18T06:56:01.000Z","dateSaleBy":"2016-02-10T18:30:00.000Z","dateProcess":"2016-02-10T18:30:00.000Z","barandCode":[{}],"barCode":"3fds","notes":"test","status":true}
services.js:133 undefined
                 * 
                 */
                
                //createSample(sample);
                dataService.showLog(sample.type.value);                                

                console.log(sample.type.value +" &&  "+ sample.meat.value +" &&  "+ sample.organic.value +" &&  "+ sample.packedInStore.value +" &&  "+ sample.datePurchase +" &&  "+ sample.dateSaleBy +" &&  "+ sample.dateProcess +" &&  "+ sample.barandCode +" &&  " + sample.notes);
                $scope.sample = sample;                
               /* if(sample.type.value != null && sample.meat.value  != null && sample.organic.value != null && sample.packedInStore.value != null && sample.datePurchase != null && sample.dateSaleBy != null && sample.dateProcess != null && sample.barandCode != null && sample.notes != null){
                    */
                    //TODO need to test with storeAddress
                    // prepare JSON data to create sample
                    //$scope.sample.barCode
                     var data = {
                                    "id": sample.id,
                                    "store": {
                                        "id": sample.store.id
                                    },
                                    "type": sample.type,
                                    "meat": sample.meat,
                                    "organic": sample.organic,
                                    "packedInStore": sample.packedInStore,
                                    "datePurchase": sample.datePurchase,
                                    "dateSaleBy": sample.dateSaleBy,
                                    "dateProcess": sample.dateProcess,
                                    "countries": $scope.val.multiple,
                                    "brandCode": sample.brand.brandCode,
                                    "barCode": sample.barCode,
                                    "note": sample.notes 
                                }; 
                var addSampleURL = URLaddStoreSamples + sample.storeID;
                console.log(JSON.stringify(data));
                //storeID
                var updateSampleURL = URLupdateSample + sample.id;
                dataService.updateData(updateSampleURL, data).then(function (dataResponse) {
                    dataService.showLog("Data " + addSampleURL +"----"+ JSON.stringify(data));
                    dataService.showLog("Data " + JSON.stringify(dataResponse));
                    $scope.dataResponseFromSample  = dataResponse.data.data;                      
                    // $scope.closeNewSample();
                    
                    if(typeof dataResponse.data.data !== 'undefined'){
                        dataService.getData("getAllSamples/usertoken/"+AuthServiceUtilities.username()+"/month/"+$scope.currentMonth).then(function(result){
                            dataService.showLog("mySample "+JSON.stringify(result));
                            if(result.data && result.data.data)
                                $scope.mySample = result.data.data;
                        });
                        var alertPopup = $ionicPopup.alert({
                            title: ' Sample !',
                            template: 'Sample Update successfully!'
                        }); 
                    
                        $scope.sample.id = $scope.dataResponseFromSample.id;
                        $scope.sample.status = false;        
                    } else {                       
                        $scope.dataResponseFromSample  = dataResponse.data;    
                        var alertPopup = $ionicPopup.alert({
                            title: ' Sample !',
                            template:  $scope.dataResponseFromSample.error
                        }); 
                    }
                    //  $scope.showSampleImageModal();
                    //$scope.uploadFrontBackImage($scope.dataResponseFromSample.data[0].id);
                    /*
                    var alertPopup = $ionicPopup.alert({
                        title: ' Sample !',
                        template: 'Sample Update successfully!'
                    });*/
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
                /*}else {
                   // alert('else');
                   var alertPopup = $ionicPopup.alert({
                            title: 'Error!',
                            template: 'Please fill up all details'
                        });
                }               */
            };
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
                $scope.updateSample(sample); 
            };
            
            $scope.minDate = new Date(2016, 0, 15);
            $scope.maxDate = new Date();
            var callbackDateSale;
            $scope.callbackofDatePurchase = function (val) { 
                var startdate = new Date(new Date(val).getFullYear(),new Date(val).getMonth(),new Date(val).getDay());
                var enddate = new Date(new Date(callbackDateSale).getFullYear()+"-"+new Date(callbackDateSale).getMonth()+"-"+new Date(callbackDateSale).getDay());
                                
                //var enddate = new Date(2015,06,21);
                if (enddate>=startdate){
                    //  alert("enddate is greater");                    
                } else {
                    //  alert("startdate is greater");                                        
                } 
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
            
            $scope.getImageFront = function () {
                dataService.showLog("getImageFront called ");
                var options = {
                    quality: 20,
                    destinationType: navigator.camera.DestinationType.FILE_URI,
                    sourceType: navigator.camera.PictureSourceType.DATA_URL
                            //encodingType: navigator.camera.EncodingType.JPEG
                };
                // open camera with plugin factory method of ng-cordova
                $cordovaCamera.getPicture(options).then(function (imageURI) {
                    //console.log(imageURI);
                    $scope.imageFrontURI = imageURI;
                    $scope.imageFront = imageURI.substr(imageURI.lastIndexOf('/') + 1);                    
 

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
                            //encodingType: navigator.camera.EncodingType.JPEG
                };
                // open camera with plugin factory method of ng-cordova
                $cordovaCamera.getPicture(options).then(function (imageURI) {
                    
                    $scope.imageBackURI = imageURI;
                    $scope.imageBack = imageURI.substr(imageURI.lastIndexOf('/') + 1);                     
                      

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
                /*
                  alert("We got a barcode\n" +
                    "Result: " + result.text + "\n" +
                    "Format: " + result.format + "\n" +
                    "Cancelled: " + result.cancelled);
                 */
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
            /**-----------------------------------------------------------------------------------------------
    *  Event Name: uploadFrontBackImage
    *  Created By: <Ketan>
    *  Created Date:
    *  Modified By: <Name of modifier>
    *  Modified Date: --
    *  param : --
    *  Propose: <Upload image.>
----------------------------------------------------------------------------------------------------*/              
            $scope.uploadFrontBackImage = function () {
                dataService.showLog("uploadFrontBackImage");
                //console.log(JSON.stingify(dataResonse));
                //console.log(dataResonse.data.id);
                //$scope.dataResonse = dataResonse.data;
                //console.log($scope.dataResonse.data.data.id);
                var SampleID = $scope.sample.id;
                var imageURI = $scope.imageFrontURI;
                var imageURI1 = $scope.imageBackURI;
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
                var sampleAddURL = AppConst.API_URL + "uploadSampleCapture/front/" + SampleID;

                if($scope.imageFrontURI){                   
                   $scope.frontImage = $scope.fileUpload(sampleAddURL, imageURI, options);
                }
                if($scope.imageBackURI) {                    
                    sampleAddURL = AppConst.API_URL + "uploadSampleCapture/back/" + SampleID;
                    $scope.backImage = $scope.fileUpload(sampleAddURL, imageURI1, options);                    
                    $scope.closeNewSampleImage();
                    //$scope.closeNewSample();
                    var alertPopup = $ionicPopup.alert({
                            title: ' Sample !',
                            template: 'Sample Update successfully!'
                        }); 
                }
                 
                      
            };
            
            // Called to select the given sample
            $scope.selectSample = function (sample, index) {
                $scope.activeSample = sample;
                Samples.setLastActiveIndex(index);
                // $scope.sideMenuController.close();
                //$ionicSideMenuDelegate.toggleRight();
            };

            // Create our modal

            $ionicModal.fromTemplateUrl('edit-sample.html', function (modal) {
                $scope.projectModal = modal;
                /*$scope.sample = {
                    id: $scope.samples.length + 1
                }*/
            }, {
                scope: $scope
            });

            $scope.createTask = function (task) {
                if (!$scope.activeSample || !task) {
                    return;
                }
                $scope.activeSample.tasks.push({
                    title: task.title
                });
                $scope.taskModal.hide();

                // Inefficient, but save all the samples
                Samples.save($scope.samples);

                task.title = "";
            };
 
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


            // Try to create the first sample, make sure to defer
            // this by using $timeout so everything is initialized
            // properly
            $timeout(function () {
                //if ($scope.samples.length == 0) {
                    //while(true) {
                    // $scope.sampleModal.show();
                    //var sampleTitle = prompt('Your first sample title:');
                    //if(sampleTitle) {
                    //createSample(sampleTitle);
                    //break;
                    //}
                    //}
                //}
            });
            $ionicModal.fromTemplateUrl('edit-sample.html', function (modal) {
                $scope.sampleModal = modal;
            }, {
                scope: $scope
            });

            $ionicModal.fromTemplateUrl('edit-sample-image.html', function (modal) {
                $scope.sampleImageModal = modal;
            }, {
                scope: $scope
            });
            /*$ionicModal.fromTemplateUrl('add-edit-store.html', function (modal) {
                $scope.addNewStoreModal = modal;
            }, {
                scope: $scope
            });
            */
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
            $scope.fileUpload = function (server, filePath, options) {
                dataService.showLog("fileUpload called");
                
                dataService.showLog(server);
                // upload file form cordova file-transfer plugin with factoy method of ng-cordova -> scannerApp.plugins.fileTransfer
                $cordovaFileTransfer.upload(server, filePath, options)
                        .then(function (result) {
                            dataService.showLog(JSON.stringify(result));
                            // Success!
                            //alert(JSON.stringify(result));
                             /* var alertPopup = $ionicPopup.alert({
                                title: ' Sample !',
                                template: 'Sample Update successfully!'
                            });*/
                            return true;
                            
                        }, function (err) {
                            // Error
                            dataService.showLog('error fileupload' + JSON.stringify(error));
                            var alertPopup = $ionicPopup.alert({
                                title: 'Sample Update failed!',
                                template: JSON.stringify(error.data.message)
                            });
                            return false;                            
                            
                        }, function (progress) {
                            console.log(progress);
                            // constant progress updates
                        });

            };
            

            
            $scope.getData = function (){
                return fancySelect.getValue();
            };            
            
//------------------------- end services ---------------------            
    
  });      

