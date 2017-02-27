angular.module('app.services', [])

        .factory('BlankFactory', [function () {

            }])

        .service('AuthServiceUtilities', function ($q, $http) {
            var LOCAL_TOKEN_KEY = 'NARMSIonicToken';
            var username = '';
            var isAuthenticated = false;
            var role = '';
            var authToken;


            var loadUserCredentials = function () {
                var token = window.localStorage.getItem(LOCAL_TOKEN_KEY);
                if (token) {
                    console.log(token);
                    useCredentials(token);
                }
            };

            var storeUserCredentials = function (token) {
                console.log("store token: " + token, isAuthenticated);
                window.localStorage.setItem(LOCAL_TOKEN_KEY, token);
                useCredentials(token);
            };

            function isAuthorized(authorizedRoles) {
                if (!angular.isArray(authorizedRoles)) {
                    authorizedRoles = [authorizedRoles];
                }
                return (isAuthenticated && authorizedRoles.indexOf(role) !== -1);

            }


            function useCredentials(token) {
                username = token.split('.')[0];
                isAuthenticated = true;
                authToken = token;
                console.log(token, username, authToken);

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

            var getIP = function (cb) {
                $http.get("http://ipinfo.io/geo").then(cb);
            };

            loadUserCredentials();

            return {
                getIP: getIP,
                isAuthorized: function () {
                    return isAuthorized;
                },
                storeUserCredentials: storeUserCredentials,
                useCredentials: function () {
                    return useCredentials;
                },
                logout: destroyUserCredentials,
                destroyUserCredentials: function () {
                    return destroyUserCredentials;
                },
                isAuthenticated: function () {
                    return isAuthenticated;
                },
                username: function () {
                    return username;
                },
                role: function () {
                    return role;
                }
            };
        })




/** Data Service
 *  for HTTP request
 *      getData and refreshData
 *  Show Log Method
 *  Show Alert Method
 * */
angular.module('app.dataServices', [])
        .service('dataService', function ($http, $q) {
            $http.defaults.useXDomain = true;
            delete $http.defaults.headers.common['X-Requested-With'];

            /**------------------------------------------------------------------------------------------
             *  Service Name: getData
             *  Created By: <Ketan>
             *  Created Date:
             *  Modified By: <Name of modifier>
             *  Modified Date: -
             *  param: atturl - Contains url value of api call
             *  Purpose: <Service method for GET web api call ,where ever required GET method call its called  >
             ------------------------------------------------------------------------------------------*/
            this.getData = function (atturl) {
                console.log("dataService.getdata " + atturl);
                dataServiceURL = API_URL + atturl;
                // $http() returns a $promise that we can add handlers with .then()
                return $http({
                    method: 'GET',
                    url: dataServiceURL,
                    headers: {'Content-Type': 'application/json; charset=utf-8;','token': localStorage.getItem(LStoken)}
                });
            },
                    /*Common service method to alert some values in app*/
                    this.showAlert = function (alertmsg) {
                        //alert(alertmsg);
                    },
                    /*Common service method to log some values in app*/
                    this.showLog = function (logmsg) {
                        console.log(logmsg);
                    },
                    /**------------------------------------------------------------------------------------------
                     *  Service Name: postLogin
                     *  Created By: <Ketan>
                     *  Created Date:
                     *  Modified By: <Name of modifier>
                     *  Modified Date: -
                     *  param: atturl - Contains url value of api call
                     *  sendnewData- Contains json data passed at time of login api call
                     *  Purpose: <Service method for POST web api call specific to login because in login no header values passed >
                     ------------------------------------------------------------------------------------------*/
                    this.postLogin = function (atturl, sendnewData) {
                        console.log("dataService.postdata " + atturl + " " + JSON.stringify(sendnewData));
                        // $http() returns a $promise that we can add handlers with .then()
                        dataServiceURL = API_URL + atturl;
                        //$http.defaults.headers.post['Content-Type'] = 'application/x-www-form-urlencoded';

                        return $http({
                            method: 'POST',
                            //                             dataType: 'json',
                            url: dataServiceURL,
                            data: sendnewData,
                            transformRequest: function (obj) {
                                var str = [];
                                for (var key in obj) {
                                    if (obj[key] instanceof Array) {
                                        for (var idx in obj[key]) {
                                            var subObj = obj[key][idx];
                                            for (var subKey in subObj) {
                                                str.push(encodeURIComponent(key) + "[" + idx + "][" + encodeURIComponent(subKey) + "]=" + encodeURIComponent(subObj[subKey]));
                                            }
                                        }
                                    }
                                    else {
                                        str.push(encodeURIComponent(key) + "=" + encodeURIComponent(obj[key]));
                                    }
                                }
                                return str.join("&");
                            },
                            contentType: 'application/x-www-form-urlencoded',
                            headers: {'Content-Type': 'application/x-www-form-urlencoded'}

                        });
                    },
                    /**------------------------------------------------------------------------------------------
                     *  Service Name: postData
                     *  Created By: <Ketan>
                     *  Created Date:
                     *  Modified By: <Name of modifier>
                     *  Modified Date: -
                     *  param: atturl - Contains url value of api call
                     *  sendnewData- Contains json data passed at time of post method api call
                     *  Purpose: <Service method for POST web api call ,where ever POST method call required its called >
                     ------------------------------------------------------------------------------------------*/
                    this.postData = function (atturl, sendnewData) {
                        console.log("dataService.postdata " + atturl + " " + JSON.stringify(sendnewData));
                        dataServiceURL = API_URL + atturl;
                        // $http() returns a $promise that we can add handlers with .then()
                        return $http({
                            method: 'POST',
                            dataType: 'json',
                            url: dataServiceURL,
                            data: JSON.stringify(sendnewData),
                            headers: {'Content-Type': 'application/json; charset=utf-8;','token': localStorage.getItem(LStoken)},
                            contentType: 'application/json;charset=utf-8'
                        });
                    },
                    /**------------------------------------------------------------------------------------------
                     *  Service Name: UpdateData
                     *  Created By: <Ketan>
                     *  Created Date:
                     *  Modified By: <Name of modifier>
                     *  Modified Date: -
                     *  param: atturl - Contains url value of api call
                     *  sendnewData- Contains json data passed at time of PUT method api call
                     *  Purpose: <Service method for PUT web api call ,where ever PUT method call required its called >
                     ------------------------------------------------------------------------------------------*/
                    this.updateData = function (atturl, sendnewData) {
                        // console.log("dataService.updatedata put method "+url+atturl + " "+JSON.stringify(sendnewData));
                        dataServiceURL = API_URL + atturl;
                        // $http() returns a $promise that we can add handlers with .then()
                        return $http({
                            method: 'PUT',
                            dataType: 'json',
                            url: dataServiceURL,
                            contentType: 'application/json;charset=utf-8',
                            data: sendnewData,
                            headers: {'Content-Type': 'application/json; charset=utf-8;','token': localStorage.getItem(LStoken)}
                        });
                    },
                    /**------------------------------------------------------------------------------------------
                     *  Service Name: deleteData
                     *  Created By: <Ketan>
                     *  Created Date:
                     *  Modified By: <Name of modifier>
                     *  Modified Date: -
                     *  param: atturl - Contains url value of api call
                     *  Purpose: <Service method for DELETE web api call ,where ever DELETE method call required its called >
                     ------------------------------------------------------------------------------------------*/
                    this.deleteData = function (atturl) {
                        console.log("dataService.delete data " + url + atturl);
                        dataServiceURL = API_URL + atturl;
                        // $http() returns a $promise that we can add handlers with .then()
                        return $http({
                            method: 'DELETE',
                            dataType: 'json',
                            url: dataServiceURL,
                            contentType: 'application/json;charset=utf-8',
                            headers: {'Content-Type': 'application/json; charset=utf-8;','token': localStorage.getItem(LStoken)}
                        });
                    }
        });


