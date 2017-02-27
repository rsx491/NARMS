

angular.module('app.controllers.samplelogdetailEdit', [])
        .controller('sampleLogDetailEditCtrl', function ($state, $scope, $rootScope, $filter, $timeout, $http, $ionicModal,
                $ionicHistory, $cordovaGeolocation, $cordovaCapture, $cordovaFileTransfer, $cordovaCamera,
                $cordovaBarcodeScanner, $ionicSideMenuDelegate, $ionicTabsDelegate, Samples, AppConst, $ionicPopup, AuthServiceUtilities, dataService) {

            /*------------------- Start Searvices -------------------------*/

            $scope.samplewihtid;
            $scope.stores = AppConst.STORES;
            $scope.meats = AppConst.MEATS;
            $scope.types = AppConst.TYPES;
            $scope.organics = AppConst.ORGANICS;
            $scope.packedInStores = AppConst.PACKEDINSTORE;
            $scope.growths  = AppConst.GROWTH;
            $scope.genuss = AppConst.GENUS;
            $scope.sources = AppConst.SOURCE;
            $scope.genusNames = AppConst.GENUSNAME;
            $scope.countries_text_multiple = 'Choose countries';
            $scope.val = {single: null, multiple: null};
            
            $scope.$on('$ionicView.beforeEnter', function () {
                dataService.showLog("sampleLogDetailsCtrl beforeEnter");                
                // Load AllBrands from Local storage
                if (localStorage["AllBrands"]) {
                    // Get All brands from local storage if exist.
                    $scope.selectableAllBrands = JSON.parse(localStorage.getItem(LSAllBrands));
                    
                    var sampleDetail = JSON.parse(localStorage.getItem(LSsampleLogDetails));
                
                    var attchURL = URLgetAllSamples + "/"+ sampleDetail.id;
                    $scope.getSampleDetail(attchURL);
                   
                    
                } else {
                    // Get All brands from server if not exist in local.
                    dataService.getData(URLgetAllBrands).then(function (dataResponse) {
                        var data = dataResponse.data.data;
                        dataService.showLog(JSON.stringify(data));
                        if (typeof dataResponse.data.data !== 'undefined') {
                            $scope.selectableAllBrands = data;
                            //$scope.selectableNames = $scope.dataResponse.data;                    
                            localStorage.setItem(LSAllBrands, JSON.stringify($scope.selectableAllBrands));
                            dataService.showLog(localStorage.getItem(LSsampleLogDetails));
                            
                            var sampleDetail = JSON.parse(localStorage.getItem(LSsampleLogDetails));
                            
                            dataService.showLog("In sampleLogDetailEditCtrl we have sampleLogDetails BBBB := " + sampleDetail );
                            var attchURL = URLgetAllSamples + "/"+ sampleDetail.id;
                            $scope.getSampleDetail(attchURL);                                                     
                            // bind data of source dynamically 
                        } else {
                            //TOD error message
                        }
                    }, function (error) {
                        dataService.showLog('PUted Data err of downloadDoc' + JSON.stringify(error));
                    });
                }
                dataService.showLog("sampleLogDetailsCtrl afterEnter");
                //$scope.getCountriesList();                
            });
            
            $scope.getSampleDetail = function (attachURL){
                dataService.showLog('getSampleDetail' + attachURL);
                var getSampleURL = attachURL;
                dataService.getData(getSampleURL).then(function(result){
                    dataService.showLog("got sample from server "+JSON.stringify(result));
                    var newsampleDetail = "";
                    if(result.data && result.data.data){
                        $scope.sampleDetail = result.data.data;
                       newsampleDetail = result.data.data;
                    }
                    $scope.showSampleModal(newsampleDetail);
                });                      
            };

            /* Get list countries from constants */
            $scope.getCountriesList = function () {
                // $scope.countries = AppConst.COUNTRIES; 
                dataService.showLog("getCountriesList called");
                $scope.countriesData1 = AppConst.COUNTRIES;
                $scope.countriesEdit = [];
                angular.forEach($scope.countriesData1, function (value, key) {
                    $scope.countriesEdit.push({
                        countryName: value,
                        value: key,
                        text: value,
                        name: value,
                        id: key,
                        checked: false
                    });
                });
            };
            /*set selected multiple values in select list */
            $scope.getCountriesListSelect = function (selectedItem) {
                // $scope.countries = AppConst.COUNTRIES; 
                dataService.showLog("getCountriesListSelect called");
                $scope.countriesData1 = AppConst.COUNTRIES;
                $scope.countriesEdit = [];
                $scope.checkFlag = false;
                $scope.name1 = [];
                $scope.value = [];
                $scope.text = "";
                $scope.name = "";
                
                angular.forEach($scope.countriesData1, function (value, key) {
                    for (var i = 0; i < selectedItem.length; i++)
                    {                        
                        if (key == selectedItem[i]) {                            
                            $scope.name1.push(value);
                            $scope.value.push(key);
                            
                            $scope.text = $scope.text + value + ',';                            
                            $scope.name = $scope.name + value + ','; 
                            $scope.checkFlag = true;
                            break;
                        } else {                            
                            $scope.checkFlag = false;
                        }
                    }

                    $scope.countriesEdit.push({
                        countryName: value,
                        value: key,
                        text: value,
                        name: value,
                        id: key,
                        checked: $scope.checkFlag
                    });
                    //$scope.name = $scope.name1;
                });
                if ($scope.name) {
                    $scope.text = $scope.text.substr(0, $scope.text.length - 2);
                    $scope.name = $scope.name.substr(0, $scope.name.length - 2); 
                }
            };
            /* Initially load sample data */
            $scope.showSampleModal = function (item) {
                //$scope.getCountriesList();
                dataService.showLog( "In Model " + JSON.stringify(item));
                $scope.userType = window.localStorage.getItem(LSNARMSuserType);
                if($scope.userType == "labuser"){
                    $scope.labUser = true;
                } else {
                    $scope.labUser = false;
                }
                $scope.countriesData = item.countries;                
                $scope.val.multiple = null;
                $scope.countriesEdit = [];
                
                if ($scope.countriesData) {                    
                    $scope.getCountriesListSelect($scope.countriesData);
                } else {
                    $scope.getCountriesList();
                }
                
                $scope.meat1 = item.meat;
                
                if (item.datePurchase) {
                    $scope.datePurchase = new Date(item.datePurchase);
                } else {
                    $scope.datePurchase = "";
                }
                if (item.dateSaleBy) {
                    $scope.dateSaleBy = new Date(item.dateSaleBy);
                } else {
                    $scope.dateSaleBy = "";
                }
                if (item.dateProcess) {
                    $scope.dateProcess = new Date(item.dateProcess);
                } else {
                    $scope.dateProcess = "";
                }
                $scope.type1 = item.type;
                $scope.organic1 = item.organic;
                $scope.packedInStore1 = item.packedInStore;
                var found = $filter('filter')($scope.selectableAllBrands, {brandCode: item.brandCode}, true);
                /* Lab User editable fields
                 "genusName":"Campylobacter",
                    "accessionNumber":"123",
                    "growth":"YES",
                    "species":"species name",
                    "serotype":"serotype name",
                    "antigenicFormula": "antigenic formula",
                    "isolateId":"1",
                    "source":"Retail",
                    "sourceSpeciesInfo":"test info"
                 */
                /* model of sample for updating values from response */
                $scope.sample = {
                    "id": item.id,
                    "type": {
                        "value": item.type
                    },
                    "meat": {
                        "value": item.meat
                    },
                    "organic": {
                        "value": item.organic
                    },
                    "packedInStore": {
                        "value": item.packedInStore
                    },
                    "datePurchase": $scope.datePurchase,
                    "dateSaleBy": $scope.dateSaleBy,
                    "dateProcess": $scope.dateProcess,
                    "countries": item.countries,
                    "barandCode": {
                            "brand": item.barandCode
                        },
                    "barCode": item.barCode,
                    "notes": item.note,
                    "store": item.store,
                    "genus": {
                        "name":item.genusName, 
                        "value":item.genus
                    },
                    "genusName":item.genusName,
                    "accessionNumber":item.accessionNumber,
                    "growth":{
                        "value" : item.growth
                    },
                    "species":item.species,
                    "serotype":item.serotype,
                    "antigenicFormula": item.antigenicFormula,
                    "isolateId":item.isolateId,
                    "source":{
                        "value" : item.source
                    },
                    "sourceSpeciesInfo":item.sourceSpeciesInfo,
                    "brand": [],
                    "status": true
                };
                $scope.sample.brand = found[0];

                dataService.showLog($scope.sample.meat);
                $scope.chageMeatType($scope.sample);
                //$scope.sample.type.value = item.type;
               /* $scope.sample.genus = {                    
                        "name":$scope.sample.genus, 
                        "value":$scope.sample.genusName                   
                };*/
                /*
                $scope.sample.genus = {                    
                        "name":$scope.sample.genusName, 
                        "value":$scope.sample.genus
                };*/
                // load dropdown data
                $scope.val.multiple = item.countries;
                //$scope.sample.meat.value = item.meat;
                //$scope.sample.organic = item.organic;
                //$scope.sample.type = item.type;
                //$scope.sample.packedInStore = item.packedInStore;  
            }
            /* change type on change dropdown of MeatType */
            $scope.chageMeatType = function (sample) {
                dataService.showLog("chageMeat called " + sample.meat.value);
                dataService.showLog("chageMeat called " + JSON.stringify(sample)); 
                //dataService.showLog("chageMeat called " + sample.type.value);

                $scope.sample.types = [];
                $scope.types = [];
                if (sample.meat.value == 1) {
                    $scope.types = AppConst.TYPES1;
                } else if (sample.meat.value == 2) {
                    $scope.types = AppConst.TYPES2;
                } else if (sample.meat.value == 3) {
                    $scope.types = AppConst.TYPES3;
                } else if (sample.meat.value == 4) {
                    $scope.types = AppConst.TYPES4;
                }                
            }

            $scope.chageMeatType1 = function (sample) {
                dataService.showLog("chageMeat1 called " + sample);
                $scope.sample.types = [];
                $scope.types = [];
                if (sample == 1) {
                    $scope.types = AppConst.TYPES1;
                } else if (sample == 2) {
                    $scope.types = AppConst.TYPES2;
                } else if (sample == 3) {
                    $scope.types = AppConst.TYPES3;
                } else if (sample == 4) {
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
                //createSample(sample);
                $scope.sample = sample;
                /* if(sample.type.value != null && sample.meat.value  != null && sample.organic.value != null && sample.packedInStore.value != null && sample.datePurchase != null && sample.dateSaleBy != null && sample.dateProcess != null && sample.barandCode != null && sample.notes != null){
                 */
                
                // prepare JSON data to create sample
                //$scope.sample.barCode
                var data = {
                    "id": sample.id, 
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
                    "note": sample.notes,
                    "genus":sample.genus.value,
                    "genusName":sample.genus.name,
                    "accessionNumber":sample.accessionNumber,
                    "growth":sample.growth.value,
                    "species":sample.species,
                    "serotype":sample.serotype,
                    "antigenicFormula": sample.antigenicFormula,
                    "isolateId":sample.isolateId,
                    "source":sample.source.value,
                    "sourceSpeciesInfo":sample.sourceSpeciesInfo
                };
               
                //var addSampleURL = URLaddStoreSamples + sample.id;
                dataService.showLog("============");
                
                dataService.showLog(JSON.stringify(data));
                //storeID
                var updateSampleURL = URLupdateSample + sample.id;
                dataService.updateData(updateSampleURL, data).then(function (dataResponse) {
                    dataService.showLog("Data " + updateSampleURL + "----" + JSON.stringify(data));
                    dataService.showLog("Data " + JSON.stringify(dataResponse));
                    $scope.dataResponseFromSample = dataResponse.data.data;
                    // $scope.closeNewSample();

                    if (typeof dataResponse.data.data !== 'undefined') {
                        /*
                        dataService.getData("getAllSamples/usertoken/" + AuthServiceUtilities.username() + "/month/" + $scope.currentMonth).then(function (result) {
                            dataService.showLog("mySample " + JSON.stringify(result));
                            if (result.data && result.data.data)
                                $scope.mySample = result.data.data;
                        });*/
                        var alertPopup = $ionicPopup.alert({
                            title: ' Sample !',
                            template: 'Sample Update successfully!'
                        });

                        $scope.sample.id = $scope.dataResponseFromSample.id;
                        $scope.sample.status = false;
                    } else {
                        $scope.dataResponseFromSample = dataResponse.data;
                        var alertPopup = $ionicPopup.alert({
                            title: ' Sample !',
                            template: $scope.dataResponseFromSample.error
                        });
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
               
            };
            /**-----------------------------------------------------------------------------------------------
             *  Event Name: updateSample
             *  Created By: <Ketan>
             *  Created Date:
             *  Modified By: <Name of modifier>
             *  Modified Date: -
             *  param : sample : object of sample with all sample model values.
             *  Propose: <updateSample.>
             ----------------------------------------------------------------------------------------------------*/
            $scope.updateModalSample = function (sample) {
                dataService.showLog("updateModalSample called");
                $scope.updateSample(sample);
            };

            $scope.minDate = new Date(2016, 0, 15);
            $scope.maxDate = new Date();
            var callbackDateSale;
            $scope.callbackofDatePurchase = function (val) {
                // calll backof Date Purchase..
            };

            $scope.callbackofDateSaleBy = function (val) {                
                // call back of datesaleby
            };

            $scope.callbackofDateProcess = function (val) {
                // Call back of date process
            };
            /* open camera for capture front image */
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
                    $scope.imageFrontURI = imageURI;
                    $scope.imageFront = imageURI.substr(imageURI.lastIndexOf('/') + 1);
                    $scope.uploadImage($scope.imageFrontURI,"front") ;

                }, function (err) {
                    // error
                });
            };
            /* open camera for capture back image */            
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
                    $scope.uploadImage($scope.imageBackURI,"back") ;


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
                    dataService.showLog("captureBarcode called ");
                    $cordovaBarcodeScanner
                            .scan()
                            .then(function (barcodeData) {
                                // Success! Barcode data is here
                                /*
                                 alert("We got a barcode\n" +
                                 "Result: " + result.text + "\n" +
                                 "Format: " + result.format + "\n" +
                                 "Cancelled: " + result.cancelled);
                                 */
                                $scope.sample.barCode = barcodeData.text;
                                //alert(JSON.stringify(barcodeData));
                            }, function (error) {
                                // An error occurred
                                var alertPopup = $ionicPopup.alert({
                                    title: 'Barcode !',
                                    template: 'Error in barcode scan !'
                                });
                                //alert(JSON.stringify(error));
                            });
                } else {
                    
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
                if($scope.sampleDetail.frontCapture){
                    $scope.imageFrontURI = AppConst.API_URL+ URLgetDocument +$scope.sampleDetail.frontCapture.id;
                }
                if($scope.sampleDetail.backCapture){
                    $scope.imageBackURI = AppConst.API_URL+ URLgetDocument +$scope.sampleDetail.backCapture.id;               
                }
                
                $scope.sampleImageModal.show();
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
                                template: 'Sample Update successfully!'
                            });
                            //return true;
                        }, function (err) {
                            // Error
                            dataService.showLog('error fileupload' + JSON.stringify(error));
                            var alertPopup = $ionicPopup.alert({
                                title: 'Sample Update failed!',
                                template: JSON.stringify(error.data.message)
                            });
                            return false;

                        }, function (progress) {
                            dataService.showLog(progress);
                            // constant progress updates
                        }); 
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
  
            $ionicModal.fromTemplateUrl('edit-sample-image.html', function (modal) {
                $scope.sampleImageModal = modal;
            }, {
                scope: $scope
            });
           
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

            $scope.getData = function () {
                return fancySelect.getValue();
            };
            $scope.onTabSelected = function () {                
                $scope.modal.show();
            };

            $ionicModal.fromTemplateUrl('templates/modal.html', {
                scope: $scope
            }).then(function (modal) {
                $scope.modal = modal;
            });
 
            $scope.createContact = function (u) {
                $scope.contacts.push({name: u.firstName + ' ' + u.lastName});
                $scope.modal.hide();
            };
            $scope.currentEndDate = new Date();
            $scope.currentStartDate = new Date();

            $scope.callbackSearchStartDate = function (date) {
                if (!date) {                    
                    $scope.currentStartDate = date;
                } else {                    
                    $scope.currentStartDate = date;
                }
            };
            $scope.callbackSearchEndDate = function (date) {
                if (!date) {
                    $scope.currentEndDate = date;
                } else {
                    $scope.currentEndDate = date;
                }
            };             
//------------------------- end services ---------------------            

        });

