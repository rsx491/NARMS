angular.module('app.routes', [])

.config(function($stateProvider, $urlRouterProvider) {

  // Ionic uses AngularUI Router which uses the concept of states
  // Learn more here: https://github.com/angular-ui/ui-router
  // Set up the various states which the app can be in.
  // Each state's controller can be found in controllers.js
  $stateProvider
    
      
        
    .state('signup', {
      url: '/signuppage',
      templateUrl: 'templates/signup.html',
      controller: 'signupCtrl'
    })
        
      
    
      
        
    .state('login', {
      url: '/loginpage',
      templateUrl: 'templates/login.html',
      controller: 'loginCtrl'
    })
        
      
    
      
        
    .state('hOME.navigation', {
      url: '/navigation',
      views: {
        'side-menu22': {
          templateUrl: 'templates/navigation.html',
          controller: 'navigationCtrl'
        }
      }
    })
        
      
    
      
        
    .state('captureSampleImage', {
      url: '/captureimage',
      templateUrl: 'templates/captureSampleImage.html',
      controller: 'captureSampleImageCtrl'
    })
        
      
    
      
        
    .state('saveImageSample', {
      url: '/saveimage',
      templateUrl: 'templates/saveImageSample.html',
      controller: 'saveImageSampleCtrl'
    })
        
      
    
      
        
    .state('hOME.homePage', {
      url: '/dashboard',
      views: {
        'side-menu22': {
          templateUrl: 'templates/homePage.html',
          controller: 'homePageCtrl'
        }
      }
    })
        
      
    
      
    .state('hOME', {
      url: '/homepage',
      abstract:true,
      templateUrl: 'templates/hOME.html'
    })
      
    
      
        
    .state('sampleLog', {
      url: '/samplelog',
      templateUrl: 'templates/sampleLog.html',
      controller: 'sampleLogCtrl'
    })
        
      
    
      
        
    .state('reviewSample', {
      url: '/reviewsample',
      templateUrl: 'templates/reviewSample.html',
      controller: 'reviewSampleCtrl'
    })
        
      
    
      
        
    .state('barCode', {
      url: '/page22',
      templateUrl: 'templates/barCode.html',
      controller: 'barCodeCtrl'
    })
        
      
    ;

  // if none of the above states are matched, use this as the fallback
  $urlRouterProvider.otherwise('/loginpage');

});