/*!
 * ngCordova
 * v0.1.23-alpha
 * Copyright 2015 Drifty Co. http://drifty.com/
 * See LICENSE in this repository for license information
 */
(function(){

angular.module('app.plugins', [

]);

// install   :   cordova plugin add cordova-plugin-camera
// link      :   https://github.com/apache/cordova-plugin-camera

  angular.module('app.plugins.camera', [])

    .factory('$cordovaCamera', ['$q', function ($q) {

      return {
        getPicture: function (options) {
          var q = $q.defer();

          if (!navigator.camera) {
            q.resolve(null);
            return q.promise;
          }

          navigator.camera.getPicture(function (imageData) {
            q.resolve(imageData);
          }, function (err) {
            q.reject(err);
          }, options);

          return q.promise;
        },

        cleanup: function () {
          var q = $q.defer();

          navigator.camera.cleanup(function () {
            q.resolve();
          }, function (err) {
            q.reject(err);
          });

          return q.promise;
        }
      };
    }]);

  // install  :    cordova plugin add https://github.com/wildabeast/BarcodeScanner.git
// link     :    https://github.com/wildabeast/BarcodeScanner

  angular.module('app.plugins.barcodeScanner', [])

    .factory('$cordovaBarcodeScanner', ['$q', function ($q) {

      return {
        scan: function (config) {
          var q = $q.defer();

          cordova.plugins.barcodeScanner.scan(function (result) {
            q.resolve(result);
          }, function (err) {
            q.reject(err);
          }, config);

          return q.promise;
        },

        encode: function (type, data) {
          var q = $q.defer();
          type = type || 'TEXT_TYPE';

          cordova.plugins.barcodeScanner.encode(type, data, function (result) {
            q.resolve(result);
          }, function (err) {
            q.reject(err);
          });

          return q.promise;
        }
      };
    }]);

  /* globals FileTransfer: true */
  angular.module('app.plugins.fileTransfer', [])

    .factory('$cordovaFileTransfer', ['$q', '$timeout', function ($q, $timeout) {
      return {
        download: function (source, filePath, options, trustAllHosts) {
          var q = $q.defer();
          var ft = new FileTransfer();
          var uri = (options && options.encodeURI === false) ? source : encodeURI(source);

          if (options && options.timeout !== undefined && options.timeout !== null) {
            $timeout(function () {
              ft.abort();
            }, options.timeout);
            options.timeout = null;
          }

          ft.onprogress = function (progress) {
            q.notify(progress);
          };

          q.promise.abort = function () {
            ft.abort();
          };

          ft.download(uri, filePath, q.resolve, q.reject, trustAllHosts, options);
          return q.promise;
        },

        upload: function (server, filePath, options, trustAllHosts) {
          var q = $q.defer();
          var ft = new FileTransfer();
          var uri = (options && options.encodeURI === false) ? server : encodeURI(server);

          if (options && options.timeout !== undefined && options.timeout !== null) {
            $timeout(function () {
              ft.abort();
            }, options.timeout);
            options.timeout = null;
          }

          ft.onprogress = function (progress) {
            q.notify(progress);
          };

          q.promise.abort = function () {
            ft.abort();
          };

          ft.upload(filePath, uri, q.resolve, q.reject, options, trustAllHosts);
          return q.promise;
        }
      };
    }]);


// install   :     cordova plugin add cordova-plugin-geolocation
// link      :     https://github.com/apache/cordova-plugin-geolocation

  angular.module('app.plugins.geolocation', [])

    .factory('$cordovaGeolocation', ['$q', function ($q) {

      return {
        getCurrentPosition: function (options) {
          var q = $q.defer();

          navigator.geolocation.getCurrentPosition(function (result) {
            q.resolve(result);
          }, function (err) {
            q.reject(err);
          }, options);

          return q.promise;
        },

        watchPosition: function (options) {
          var q = $q.defer();

          var watchID = navigator.geolocation.watchPosition(function (result) {
            q.notify(result);
          }, function (err) {
            q.reject(err);
          }, options);

          q.promise.cancel = function () {
            navigator.geolocation.clearWatch(watchID);
          };

          q.promise.clearWatch = function (id) {
            navigator.geolocation.clearWatch(id || watchID);
          };

          q.promise.watchID = watchID;

          return q.promise;
        },

        clearWatch: function (watchID) {
          return navigator.geolocation.clearWatch(watchID);
        }
      };
    }]);

})();
