angular.module('app.services', [])

.factory('BlankFactory', [function(){

}])

.service('AuthServiceUtilities', function($q, $http) {
  var LOCAL_TOKEN_KEY = 'yourTokenKey';
  var username = '';
  var isAuthenticated = false;
  var role = '';
  var authToken;


  loadUserCredentials = function () {
    var token = window.localStorage.getItem(LOCAL_TOKEN_KEY);
    if (token) {
        console.log(token);
      useCredentials(token);
    }
  }

  storeUserCredentials = function(token) {
    window.localStorage.setItem(LOCAL_TOKEN_KEY, token);
    useCredentials(token);
  }

  function useCredentials(token) {
    username = token.split('.')[0];
    isAuthenticated = true;
    authToken = token;

    if (username == 'admin') {
      role = USER_ROLES.admin
    }
    if (username == 'user') {
      role = USER_ROLES.public
    }

    // Set the token as header for your requests!
    $http.defaults.headers.common['X-Auth-Token'] = token;
  }

  function destroyUserCredentials() {
    authToken = undefined;
    username = '';
    isAuthenticated = false;
    $http.defaults.headers.common['X-Auth-Token'] = undefined;
    window.localStorage.removeItem(LOCAL_TOKEN_KEY);
  }

  var getIP = function(cb){
    $http.get("http://ipinfo.io/geo").then(cb);
  };

  loadUserCredentials();

  return {
    getIP: getIP,
    isAuthenticated: function() {return isAuthenticated;},
    username: function() {return username;},
    role: function() {return role;}
  };
})