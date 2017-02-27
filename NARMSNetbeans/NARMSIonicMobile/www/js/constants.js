/* 
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
//var API_URL ="http://54.172.249.63:8080/narms-web/";
//var API_URL ="http://192.192.8.79:8080/narms-web/";
//var API_URL = "http://localhost:8080/narms-web/";
var API_URL = "http://52.36.191.121:8080/narms-web/";
var CENTERLIMIT = {
    CHICKEN : 0,
    BEEF  : 0,
    TURKEY : 0,
    PORK : 0
};
var URLgetAllCenters = "getAllCenters";
var URLgetAllBrands = "getAllBrands";
var URLgetCenterLimitsByStore = "getCenterLimitsByStore";
var URLgetCenterLimits = "getCenterLimits";
var URLgetStoreSamples = "getStoreSamples";
var URLgetAllStores = "getAllStores";
var URLgetAllStoresByCenterID = "getAllStoresByCenterID";
var URLgetUserCenterBySessionToken = "getUserCenterIDBySessionToken";
var URLgetUserCenterBySession = "getUserCenterBySession";
var URLaddStoreSamples = "addStoreSamples/";
var URLaddNewStore = "addStoreToCenter/";
var URLgetactiveuserid = "getactiveuserid";
var URLgetAllCountries = "getAllCountries";
var URLgetActiveUserByToken="getactiveuserbytoken";
var URLgetSampleCountByUser = "getSampleCountByUser";
var URLgetSampleCountByCenterID = "getsamplecountbycenterid";
var URLgetCurrentSampleCountByCenterID = "getcurrentsamplecountbycenterid";
var URLupdateSample = "updateSample/";
var URLgetAllSamples = "getAllSamples";
var URLgetAllSamplesByCenterAndDate = "getAllSamplesByCenterAndDate";
var URLgetAllSamplesByCenter = "getAllSamplesByCenter";
var URLgetDocument ="getDocument/";
var URLuploadSampleCapture = "uploadSampleCapture/";
var DEVELOPEROPTION = 1;
var LStoken = "NARMSIonicToken";
var LSNARMSIonicToken = "NARMSIonicToken";
var LSNARMSCenterID = "NARMSCenterID";
var LSsampleLogDetails = "sampleLogDetails";
var LSAllStores = "AllStores";
var LSAllBrands = "AllBrands";
var LSNARMSuserType = "NARMSuserType";
var GOOGLEAPIKEY = "AIzaSyDPy55DQGXBdbKdRhV3eFj6fCGnRHNWQWA";
/*
getAllSamplesByCenter
getAllSamplesByCenterAndDate 
getAllSamplesByUserID usertoken
 * 
 */
// 14.140.154.162
//https://maps.googleapis.com/maps/api/distancematrix/xml?origins=Vancouver+BC|Seattle&destinations=San+Francisco|Vancouver+BC&mode=bicycling&language=fr-FR&key=AIzaSyDPy55DQGXBdbKdRhV3eFj6fCGnRHNWQWA
//https://developers.google.com/maps/documentation/javascript/examples/distance-matrix
//https://developers.google.com/maps/documentation/javascript/examples/distance-matrix

// Loning //Working
// http://192.192.8.79:8080/narms-web/getactiveuserid?tokenID=c435f94d-ee22-43b0-bfa0-45c6ca14ca48 //get UserId from token Working
//http://192.192.8.79:8080/narms-web/getUserCenterIDBySessionToken?tokenID=c435f94d-ee22-43b0-bfa0-45c6ca14ca48 // Not working
//http://192.192.8.79:8080/narms-web/getAllStores //Working 
// http://192.192.8.79:8080/narms-web/getAllStores?getAllStoresByCenterID?centerID=c435f94d-ee22-43b0-bfa0-45c6ca14ca48 // Need to check 
//getAllStoresByCenterID
