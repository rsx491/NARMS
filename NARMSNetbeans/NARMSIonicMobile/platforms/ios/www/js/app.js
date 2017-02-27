// Ionic Starter App

// angular.module is a global place for creating, registering and retrieving Angular modules
// 'starter' is the name of this angular module example (also set in a <body> attribute in index.html)
// the 2nd parameter is an array of 'requires'
// 'starter.services' is found in services.js
// 'starter.controllers' is found in controllers.js
angular.module('app', ['ionic', 'ngCordova' ,'ionic-datepicker','ionic-modal-select', 'ion-autocomplete','app.controllers', 'app.routes', 'app.services','app.dataServices',  'app.directives',
    //'app.plugins','app.plugins.camera', 'app.plugins.fileTransfer' ,'app.plugins.geolocation' , 'app.plugins.barcodeScanner' ,
    'app.factory','app.config',
    'app.controllers.login','app.controllers.samplelog','app.controllers.homepage',
    'app.controllers.signup','app.controllers.reset','app.controllers.resetcode','app.controllers.menu','app.controllers.activity','app.controllers.samplelogdetails','ngMessages'])

.run(function($ionicPlatform) {
  $ionicPlatform.ready(function() {
    // Hide the accessory bar by default (remove this to show the accessory bar above the keyboard
    // for form inputs)
    if(window.cordova && window.cordova.plugins.Keyboard) {
      cordova.plugins.Keyboard.hideKeyboardAccessoryBar(true);
    }
    if(window.StatusBar) {
      // org.apache.cordova.statusbar required
      StatusBar.styleColor('white');
    }
  });
})


.run(function ($rootScope, $state, AuthServiceUtilities) {
  $rootScope.$on('$stateChangeStart', function (event,next, nextParams, fromState) {
    console.log(next);
    if(DEVELOPEROPTION == 0){
        if ('data' in next && 'authorizedRoles' in next.data) {
          var authorizedRoles = next.data.authorizedRoles;
          if (!AuthServiceUtilities.isAuthorized(authorizedRoles)) {
            event.preventDefault();
            $state.go($state.current, {}, {reload: true});
            $rootScope.$broadcast('auth-not-authenticated');
          }
        }

        if (!AuthServiceUtilities.isAuthenticated()) {
          if (next.name !== 'login' && next.name != 'signup' && next.name != 'reset' && next.name !='resetcode' ) {
            console.log("not authenticated", next.name);
            event.preventDefault();
            $state.go('login');
          }
        }
    }
  });
});
