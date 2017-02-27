angular.module('app.routes', [])

        .config(function ($stateProvider, $urlRouterProvider) {

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
        
                    .state('resetcode', {
                        url: '/reset_code',
                        templateUrl: 'templates/reset.html',
                        controller: 'resetCodeCtrl'
                    })
                    .state('reset', {
                        url: '/reset',
                        templateUrl: 'templates/reset.html',
                        controller: 'resetCtrl'
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


                    .state('hOME', {
                        url: '/homepage',
                        abstract: true,
                        templateUrl: 'templates/hOME.html',
                        controller: 'menuCtrl'
                    })
                     .state('hOME.navigation', {
                        url: '/navigation',
                        views: {
                            'hOME': {
                                templateUrl: 'templates/navigation.html',
                                controller: 'navigationCtrl'
                            }
                        }
                    })
                    .state('hOME.homePage', {
                        url: '/dashboard',
                        views: {
                            'hOME': {
                                templateUrl: 'templates/homePage.html',
                                controller: 'homePageCtrl'
                            }
                        }
                    })

                    .state('hOME.sampleLog', {
                        url: '/samplelog',
                        views: {
                            'hOME': {
                                templateUrl: 'templates/sampleLog.html',
                                controller: 'sampleLogCtrl'
                            }
                        }
                    })

                    .state('hOME.sampleLogDetails', {
                        url: '/sampleLogDetails',
                        //cache: false,
                        views: {
                            'hOME': {
                                templateUrl: 'templates/sampleLogDetails.html',
                                controller: 'sampleLogDetailsCtrl'
                            }
                        }
                    })
                    
                    .state('hOME.sampleLogDetailsByCenter', {
                        url: '/sampleLogDetailsByCenter',
                        //cache: false,
                        views: {
                            'hOME': {
                                templateUrl: 'templates/sampleLogDetailsByCenter.html',
                                controller: 'sampleLogDetailsByCenterCtrl'
                            }
                        }
                    })
                    
                    .state('hOME.sampleLogDetailsBySearch', {
                        url: '/sampleLogDetailsBySearch',
                        //cache: false,
                        views: {
                            'hOME': {
                                templateUrl: 'templates/sampleLogDetailsBySearch.html',
                                controller: 'sampleLogDetailsBySearchCtrl'
                            }
                        }
                    })
                    
                    .state('hOME.sampleLogDetailEdit', {
                        url: '/sampleLogDetailEdit',
                        cache: false,
                        //param: ["params"],
                        views: {
                            'hOME': {
                                templateUrl: 'templates/sampleLogDetailEdit.html',
                                controller: 'sampleLogDetailEditCtrl'
                            }
                        }
                    })

                    .state('hOME.activityLog', {
                        url: '/activityLog',
                        cache: false,
                        views: {
                            'hOME': {
                                templateUrl: 'templates/activityLog.html',
                                controller: 'activityLogCtrl'
                            }
                        }
                    })


                    .state('reviewSample', {
                        url: '/reviewsample',
                        templateUrl: 'templates/reviewSample.html',
                        controller: 'reviewSampleCtrl'
                    })


                    .state('admin', {
                      url: '/admin',
                      abstract:true,
                      templateUrl: 'templates/admin.html',
                      controller: 'adminCtrl'
                    })

                    .state('admin.users', {
                      url : '/users',
                      views: {
                        'side-menu22': {
                          templateUrl: 'templates/adminUsers.html',
                          controller: 'adminUsersCtrl'
                        }
                      }
                    })
                    .state('admin.samples', {
                      url : '/samples',
                      views: {
                        'side-menu22': {
                          templateUrl: 'templates/adminSamples.html',
                          controller: 'adminSamplesCtrl'
                        }
                      }
                    })
                    .state('admin.stores', {
                      url : '/stores',
                      views: {
                        'side-menu22': {
                          templateUrl: 'templates/adminStores.html',
                          controller: 'adminStoresCtrl'
                        }
                      }
                    })
                    .state('admin.brands', {
                      url : '/brands',
                      views: {
                        'side-menu22': {
                          templateUrl: 'templates/adminBrands.html',
                          controller: 'adminBrandsCtrl'
                        }
                      }
                    })
                    .state('admin.centers', {
                      url : '/centers',
                      views: {
                        'side-menu22': {
                          templateUrl: 'templates/adminCenters.html',
                          controller: 'adminCentersCtrl'
                        }
                      }
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