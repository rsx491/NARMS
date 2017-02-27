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
    'app.controllers.admin','app.controllers.adminusers','app.controllers.adminsamples','app.controllers.adminbrands','app.controllers.adminstores','app.controllers.admincenters',
    'app.controllers.signup','app.controllers.reset','app.controllers.resetcode','app.controllers.menu','app.controllers.activity','app.controllers.samplelogdetails','app.controllers.samplelogdetailsByCenter','app.controllers.samplelogdetailsBySearch','app.controllers.samplelogdetailEdit','ngMessages'])

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
}).config(function($ionicConfigProvider) {
  // back button text always displays "Back"
  $ionicConfigProvider.backButton.previousTitleText(false);
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
}).directive('fancySelect', 
        [
            '$ionicModal',
            function($ionicModal) {
                return {
                    /* Only use as <fancy-select> tag */
                    restrict : 'E',
                    replace: true,
                    scope: false,
                    /* Our template */
                    templateUrl: 'fancy-select.html',

                    /* Attributes to set */
                    scope: {
                        'name' : '=',
                        'items'        : '=', /* Items list is mandatory */
                        'text'         : '=', /* Displayed text is mandatory */
                        'value'        : '=', /* Selected value binding is mandatory */
                        'callback'     : '&',
                        'validate'     : '=validate'
                    },

                    link: function (scope, element, attrs) {

                        /* Default values */
                        scope.multiSelect   = attrs.multiSelect === 'true' ? true : false;
                        scope.allowEmpty    = attrs.allowEmpty === 'false' ? false : true;

                        /* Header used in ion-header-bar */
                        scope.headerText    = attrs.headerText || '';

                        /* Text displayed on label */
                        // scope.text          = attrs.text || '';
                        scope.defaultText   = scope.text || '';

                        /* Notes in the right side of the label */
                        scope.noteText      = attrs.noteText || '';
                        scope.noteImg       = attrs.noteImg || '';
                        scope.noteImgClass  = attrs.noteImgClass || '';

                        /* Optionnal callback function */
                        // scope.callback = attrs.callback || null;

                        /* Instanciate ionic modal view and set params */

                        /* Some additionnal notes here : 
                         * 
                         * In previous version of the directive,
                         * we were using attrs.parentSelector
                         * to open the modal box within a selector. 
                         * 
                         * This is handy in particular when opening
                         * the "fancy select" from the right pane of
                         * a side view. 
                         * 
                         * But the problem is that I had to edit ionic.bundle.js
                         * and the modal component each time ionic team
                         * make an update of the FW.
                         * 
                         * Also, seems that animations do not work 
                         * anymore.
                         * 
                         */
                        $ionicModal.fromTemplateUrl(
                            'fancy-select-items.html',
                              {'scope': scope}
                        ).then(function(modal) {
                            scope.modal = modal;
                        });

                        /* Validate selection from header bar */
                        scope.validate = function (event) {
                            // Construct selected values and selected text
                            if (scope.multiSelect == true) {

                                // Clear values
                                //scope.value = '';
                                scope.value = [];
                                scope.text = '';
                                scope.name = '';

                                // Loop on items
                                //console.log(JSON.stringify(scope.items));
                                jQuery.each(scope.items, function (index, item) {
                                    if (item.checked) {
                                       // scope.value = scope.value + '"'+item.id+'"'+',';                                       
                                        scope.value.push(item.id);
                                        //console.log(JSON.stringify(scope.value));
                                        scope.text = scope.text + item.text+',';
                                        //console.log(JSON.stringify(scope.text));
                                        scope.name = scope.name + item.name+',';                                        
                                        //console.log(JSON.stringify(scope.name));
                                    }
                                });

                                // Remove trailing comma
                                //scope.value = scope.value.substr(0,scope.value.length - 1);
                                scope.text = scope.text.substr(0,scope.text.length - 2);
                                scope.name = scope.name.substr(0,scope.name.length - 2);
//                                console.log(scope.name);
//                                console.log(scope.text);
                                
                                //scope.name = scope.text;
                            }

                            // Select first value if not nullable
                            if (typeof scope.value == 'undefined' || scope.value == '' || scope.value == null ) {
                                if (scope.allowEmpty == false) {
                                    scope.value = scope.items[0].id;
                                    scope.text = scope.items[0].text;
                                    scope.name = scope.items[0].name;

                                    // Check for multi select
                                    scope.items[0].checked = true;
                                } else {
                                    scope.text = scope.defaultText;
                                    scope.name = scope.defaultText;


                                }
                            }

                            // Hide modal
                            scope.hideItems();

                            // Execute callback function
                            if (typeof scope.callback == 'function') {
                                scope.callback (scope.value);
                            }
                        }

                        /* Show list */
                        scope.showItems = function (event) {
                            event.preventDefault();
                            scope.modal.show();
                        }

                        /* Hide list */
                        scope.hideItems = function () {
                            scope.modal.hide();
                        }
                        scope.getValue = function (){
                            return scope.name;
                        }

                        /* Destroy modal */
                        scope.$on('$destroy', function() {
                          scope.modal.remove();
                        });

                        /* Validate single with data */
                        scope.validateSingle = function (item) {

                            // Set selected text
                            scope.text = item.text;
                            scope.name = item.name;


                            // Set selected value
                            scope.value = item.id;

                            // Hide items
                            scope.hideItems();

                            // Execute callback function
                            if (typeof scope.callback == 'function') {
                                scope.callback (scope.value);
                            }
                        }
                    }
                };
            }
        ]
    );
