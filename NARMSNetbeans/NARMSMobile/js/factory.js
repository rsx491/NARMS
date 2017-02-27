//Angular services are substitutable objects that are wired together using dependency injection (DI). You can use services to organize and share code across your app.
/**------------------------------------------------------------------------------------------
 *  File Name: factory.js
 *  Created By: <Ketan>
 *  Created Date:
 *  Modified By: <Name of modifier>
 *  Modified Date: -
 *  Purpose:
 // Common methods it can use in whole application.
 ------------------------------------------------------------------------------------------*/
angular.module('app.factory', [])
  // To store controller's scope and user it from another.
  .factory('Scopes', function ($rootScope) {
      var mem = {};
      return {
          store: function (key, value) {
            $rootScope.$emit('scope.stored', key);
            mem[key] = value;
          },
        get: function (key) {
          return mem[key];
          }
        };
    }
  ).factory('cdvPlugin', ['$q',function ($rootScope,$q) {
  // cdvpdf417.m file have written function of pdf417Scanner.scan which have open camera interface. When camera have detecte specific barcode type of pdf417 than it will send back response with JSON fromat.

      var types = ["USDL", "QR Code"];
      /**
       * Initiate scan with options
       * NOTE: Some features are unavailable without a license
       * Obtain your key at http://pdf417.mobi
       */
      var options = {
        beep: true, // Beep on
        noDialog: true, // Skip confirm dialog after scan
        uncertain: false, //Recommended
        quietZone: false, //Recommended
        highRes: false, //Recommended
        inverseScanning: false,
        frontFace: false
      };

      // Note that each platform requires its own license key

      // This license key allows setting overlay views for this application ID: mobi.pdf417.demo
      var licenseiOs = "XHY626KS-NKTLAF4V-BW3WCC42-7A27S6XI-DFBZPTMI-G6TKPU6E-GQDR36L2-5AMRFTWO";

      // This license is only valid for package name "mobi.pdf417.demo"
      var licenseAndroid = "XHY626KS-NKTLAF4V-BW3WCC42-7A27S6XI-DFBZPTMI-G6TKPU6E-GQDR36L2-5AMRFTWO";

      // This license is only valid for Product ID "e2994220-6b3d-11e5-a1d6-4be717ee9e23"
      var licenseWP8 = "5JKGDHZK-5WN4KMQO-6TZU3KDQ-I4YN67V5-XSN4FFS3-OZFAXHK7-EMETU6XD-EY74TM4T";

    return {
      pdf417: function () {
        cordova.plugins.pdf417Scanner.scan(
          // Register the callback handler
          function callback(scanningResult) {

            // handle cancelled scanning
            if (scanningResult.cancelled == true) {
              $scope.$apply();
              alert('cancelled');
              return false;
            }

            // Obtain list of recognizer results
            return scanningResult.resultList;

          },
          // Register the error callback
          function errorHandler(err) {
            alert('Error');
          },
          types, options, licenseiOs, licenseAndroid
        );

      },
      getPicture: function(options) {
        var q = $q.defer();

        navigator.camera.getPicture(function(result) {
          // Do any magic you need
          q.resolve(result);
        }, function(err) {
          q.reject(err);
        }, options);

        return q.promise;
      },
      get: function (key) {
        return mem[key];
      }
    };
  }]
)
.factory('Samples', function() {
  return {
    all: function() {
      var sampleString = window.localStorage['samples'];
      if(sampleString) {
        return angular.fromJson(sampleString);
      }
      return [];
    },
    save: function(samples) {        
        console.log(angular.toJson(samples));
        console.log(angular.toJson(samples).sampleTitle);
        
      window.localStorage['samples'] = angular.toJson(samples);
    },
    newSample: function(sampleTitle) {
      // Add a new sample      
      return {
         sampleTitle
      };
    },
    getLastActiveIndex: function() {
      return parseInt(window.localStorage['lastActiveSample']) || 0;
    },
    setLastActiveIndex: function(index) {
      window.localStorage['lastActiveSample'] = index;
    }
  }
});

