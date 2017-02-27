/* 
 * To change thi license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module('app.controllers.samplelog', []) 
.controller('sampleLogCtrl', function($scope, $timeout, $ionicModal, $ionicSideMenuDelegate, Samples, AppConst) {
        $scope.selectedCountry = {}
        $scope.meats = AppConst.MEATS;         
        $scope.types = AppConst.TYPES;
        $scope.organics = AppConst.ORGANICS;
        $scope.packedInStores = AppConst.PACKEDINSTORE;
        $scope.selectedCountry = {}
        $scope.countries = AppConst.COUNTRIES;
    // A utility function for creating a new sample
  // with the given sampleTitle
    var createSample = function(sampleTitle) {
        //console.log(sampleTitle);
        var newSample = Samples.newSample(sampleTitle);
        console.log(newSample);
        console.log(newSample.sampleTitle);
        $scope.samples.push(newSample.sampleTitle);
        console.log($scope.samples);
        Samples.save($scope.samples);
        $scope.selectSample(newSample, $scope.samples.length-1);
        
        $scope.sample = {
            id : Samples.getLastActiveIndex() + 1
        };
        $scope.sampleModal.hide();
        
        //after save into database save image of that sample
        $scope.sampleImageModal.show();
        
    }


    // Load or initialize samples
    $scope.samples = Samples.all();

    // Grab the last active, or the first sample
    $scope.activesample = $scope.samples[Samples.getLastActiveIndex()];
        
    $scope.showSampleModal = function(){
            //console.log('showSampleModal');
            //$scope.sample = [];
            $scope.sample.id = Samples.getLastActiveIndex() + 1 ;
            $scope.sampleModal.show();
    };
    
    $scope.showSampleImageModal = function(){
             $scope.sampleImageModal.show();
    };
        
    // Called to create a new sample
    $scope.newSample = function(sample) {
        //var sampleTitle = prompt('Sample name');
        //var sampleTitle = sample.title;
        
        if(sample) {
            //$scope.samplesAll = Samples.all();
            //sample.id = $scope.samplesAll.length+1;
            //console.log(sample.id);
            createSample(sample);
        }
    };

    // Called to select the given sample
    $scope.selectSample = function(sample, index) {
        $scope.activeSample = sample;
        Samples.setLastActiveIndex(index);
        // $scope.sideMenuController.close();
        //$ionicSideMenuDelegate.toggleRight();
    };
    
    // Create our modal
    
    $ionicModal.fromTemplateUrl('new-sample.html', function(modal) {    
        $scope.projectModal = modal;
        $scope.sample = {
            id : $scope.samples.length+1
        }
    }, {
        scope: $scope
    });

    $scope.createTask = function(task) {
        if(!$scope.activeSample || !task) {
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

        $scope.newTask = function() {
            $scope.taskModal.show();
        };

        $scope.closeNewTask = function() {
            $scope.taskModal.hide();
        }
        
        $scope.closeNewSample = function(){
            $scope.sampleModal.hide();
        }
          
        // Try to create the first sample, make sure to defer
        // this by using $timeout so everything is initialized
        // properly
        $timeout(function() {
            if($scope.samples.length == 0) {
            //while(true) {
                $scope.sampleModal.show();
                //var sampleTitle = prompt('Your first sample title:');
                //if(sampleTitle) {
                //createSample(sampleTitle);
                //break;
                //}
            //}
            }
        });
        $ionicModal.fromTemplateUrl('new-sample.html', function(modal) {
            $scope.sampleModal = modal;            
        }, {
            scope: $scope
        });  
        
        $ionicModal.fromTemplateUrl('new-sample-image.html', function(modal) {
            $scope.sampleImageModal = modal;            
        }, {
            scope: $scope
        });        
       
})

