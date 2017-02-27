/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */

angular.module('app.controllers.adminstores', []) 

.controller('adminStoresCtrl', function($scope, $cordovaGeolocation, dataService, AuthServiceUtilities) {
    
  $scope.fields = {stores:[
    {label:"id", readonly:true},
    {label:"storeName"},
    {label:"storeAddress"}
    ]};
  $scope.dateFields = {};
  $scope.data = { stores : [] };
  $scope.orderRecordsBy = 'id';
  $scope.selectedRecord = {};
  $scope.formData = {};
  $scope.selectedAction = "";
  $scope.formSubmitLabel = "Update";
  $scope.showRecordForm=false;
  $scope.csvHREF = false;

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
    dataService.getData('getAllStores','GET',null).then(function(results){
      console.log("got stores: ",results);
        $scope.data.stores = results.data.data;
        $scope.dateFields = [];

        $scope.pagination.totalRecords = $scope.data.stores.length;
        $scope.pagination.numPages = Math.ceil($scope.pagination.totalRecords / $scope.pagination.numPerPage);
        if($scope.pagination.currentPage>$scope.pagination.numPages) $scope.pagination.currentPage = $scope.pagination.numPages;

        var csv = dataService.JSON2CSV($scope.data.stores);
        var blob = new Blob(["\ufeff", csv]);
        $scope.csvHREF = URL.createObjectURL(blob);
        
    }, function(err){
      console.log("Couldn't fetch stores", err);
    });
  };
  $scope.fetchData();


  $scope.putData = function(){
    console.log("Send current data to update endpoint",$scope.selectedRecord);
    if(!$scope.selectedRecord||!$scope.selectedRecord.id) return;
    var data = $scope.selectedRecord; data.storeID = $scope.selectedRecord.id;
    dataService.requestAdmin('stores','PUT',data).then(function(results){
      $scope.showRecordForm=false;
      $scope.selectedRecord={};
      $scope.fetchData();
    }, function(err){
      console.log("Couldn't updated record",err);
    })
  };

  $scope.deleteRecord = function( recordID ){
    console.log("Delete record ID: "+recordID);
    dataService.requestAdmin('stores','DELETE',{tokenID: localStorage.getItem(LStoken),storeID:recordID}).then(function(results){
      console.log("Deleted store, refreshing");
      $scope.fetchData();
    }, function(err){
      console.log("Couldn't delete record", err);
    });
  };

  $scope.addRecord = function(){
    console.log("Send current data as new record",$scope.selectedRecord);
    delete( $scope.selectedRecord['id']);
    dataService.requestAdmin('stores','POST',$scope.selectedRecord).then(function(results){
      console.log("Added store, refreshing");
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
    var csv = dataService.JSON2CSV($scope.data.stores);
    var downloadLink = document.createElement("a");
    var blob = new Blob(["\ufeff", csv]);
    var url = URL.createObjectURL(blob);
    downloadLink.href = url;
    downloadLink.download = "stores.csv";
    document.body.appendChild(downloadLink);
    downloadLink.click();
    document.body.removeChild(downloadLink);
  };

})

    