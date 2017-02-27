/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module('app.controllers.adminsamples', []) 

.controller('adminSamplesCtrl', function($scope, $cordovaGeolocation, dataService, AuthServiceUtilities) {
    
	$scope.fields = { samples:[
		{label:"id",readonly:true},
		{label:"meat"},
		{label:"type"},
		{label:"organic"},
		{label:"packedInStore"},
		{label:"brandCode"},
		{label:"barCode"},
		{label:"country"},
		{label:"note"},
    {label: "sampleid"},
		{label:"datePurchase",type:"date"},
		{label:"dateSaleBy"},
		{label:"dateProcess"},
    {label:"store",readonly:true}
		]};
	  $scope.data = { samples : [] };
	
  $scope.dateFields = {
  	"datePurchase" : { dateFormat: 'dd-MM-yyyy', callback: function(val){ $scope.setDateVal("datePurchase", val);} },
  	"dateSaleBy" : { dateFormat: 'dd-MM-yyyy', callback: function(val){ $scope.setDateVal("dateSaleBy", val);} },
  	"dateProcess" : { dateFormat: 'dd-MM-yyyy', callback: function(val){ $scope.setDateVal("dateProcess", val);} }
  };
  $scope.orderRecordsBy = 'id';
  $scope.selectedRecord = {};
  $scope.formData = {};
  $scope.selectedAction = "";
  $scope.formSubmitLabel = "Update";
  $scope.showRecordForm=false;
  $scope.csvHREF=null;

  $scope.pagination = {
    numPages : 1,
    numPerPage : 20,
    currentPage : 1,
    totalRecords : 0,
    nextPage : function(){
      if($scope.pagination.numPages <= $scope.pagination.currentPage) return;
      $scope.pagination.currentPage++;
    },
    prevPage : function(){
      if($scope.pagination.currentPage<= 1) return;
      $scope.pagination.currentPage--;
    },
    goToPage : function(p){
      if(p>0&&p<=$scope.pagination.numPages) $scope.pagination.currentPage=p;
    },
    createRange : function(){
      var steps = [];
      for(var i=$scope.pagination.currentPage-2;i<=$scope.pagination.currentPage+2;i++){
        if(i>0 && i <= $scope.pagination.numPages) steps.push(i);
      }
      return steps;
    }
  };

  $scope.fetchData = function(){
    dataService.getData('getAllSamples').then(function(results){
        $scope.data.samples = results.data.data;

        $scope.pagination.totalRecords = $scope.data.samples.length;
        $scope.pagination.numPages = Math.ceil($scope.pagination.totalRecords / $scope.pagination.numPerPage);
        if($scope.pagination.currentPage>$scope.pagination.numPages) $scope.pagination.currentPage = $scope.pagination.numPages;

        var csv = dataService.JSON2CSV($scope.data.samples);
        var blob = new Blob(["\ufeff", csv]);
        $scope.csvHREF = URL.createObjectURL(blob);
        
    }, function(err){
      console.log("Couldn't fetch samples", err);
    });
  };
  $scope.fetchData();

  $scope.setDateVal = function(fname, val){
  	console.log("Setting date",fname,val);
  	$scope.selectedRecord[fname] = val;
  };

  $scope.putData = function(){
    console.log("Send current data to update endpoint",$scope.selectedRecord);
    if(!$scope.selectedRecord||!$scope.selectedRecord.id) return;
    var data = $scope.selectedRecord; 
    for(var i=0;i < $scope.fields.samples.length;i++){
      var f = $scope.fields.samples[i];
      if(!data[f.label]) data[f.label]=null;
      else if(f.type && f.type=="date" && data[f.label] instanceof Date){
        data[f.label] = data[f.label].getTime();
      }
    }
    data.sampleID = $scope.selectedRecord.id;
    dataService.requestAdmin('samples','PUT',data).then(function(results){
      $scope.showRecordForm=false;
      $scope.selectedRecord={};
      $scope.fetchData();
    }, function(err){
      console.log("Couldn't updated record",err);
    })
  };

  $scope.deleteRecord = function( recordID ){
    console.log("Delete record ID: "+recordID);
    dataService.requestAdmin('samples','DELETE',{tokenID: localStorage.getItem(LStoken),sampleID:recordID}).then(function(results){
      console.log("Deleted sample, refreshing");
      $scope.fetchData();
    }, function(err){
      console.log("Couldn't delete record", err);
    });
  };

  $scope.addRecord = function(){
    console.log("Send current data as new record",$scope.selectedRecord);
    delete( $scope.selectedRecord['id']);
    dataService.requestAdmin('samples','POST',$scope.selectedRecord).then(function(results){
      console.log("Added sample, refreshing");
      $scope.showRecordForm=false;
      $scope.selectedRecord={};
      $scope.fetchData();
    }, function(err){
      console.log("Couldn't add record", err);
    });
  };
  
  $scope.switchOrder = function( fieldName ) {
    $scope.orderRecordsBy = fieldName;
   };


  $scope.clickedEdit = function(record){
    $scope.selectedRecord = record;
    $scope.showEditForm();
  };

  $scope.clickedDelete = function(record){
    if(confirm("Are you sure you want to delete this record?")){
        $scope.deleteRecord(record.id);
    } 
  };

  $scope.showAddForm = function(){
    $scope.selectedRecord = {};
    $scope.formSubmitLabel = "Add";
  
    $scope.showRecordForm=true;
  };

  $scope.showEditForm = function(){
    if(!$scope.selectedRecord) return;
    $scope.formSubmitLabel = "Update";

    for(var i in $scope.dateFields){
        $scope.dateFields['inputDate'] = new Date($scope.selectedRecord[i]);
      }
    console.log($scope.dateFields);
    $scope.showRecordForm=true;
  };

  $scope.cancelForm = function(){
    $scope.showRecordForm=false;
    $scope.selectedRecord = {};
  };

  $scope.submitRecord = function(){
    if($scope.formSubmitLabel=='Update'){
      $scope.putData();
    } else if($scope.formSubmitLabel=='Add'){
      $scope.addRecord();
    }
  };

  $scope.downloadCSV = function(){
    var csv = dataService.JSON2CSV($scope.data.samples);
    var downloadLink = document.createElement("a");
    var blob = new Blob(["\ufeff", csv]);
    var url = URL.createObjectURL(blob);
    downloadLink.href = url;
    downloadLink.download = "samples.csv";
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
  };


})

    