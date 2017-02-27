angular.module('app.services', [])

        .factory('BlankFactory', [function () {

            }])

        .service('AuthServiceUtilities', function ($q, $http) {
            var LOCAL_TOKEN_KEY = 'NARMSIonicToken';
            var username = '';
            var isAuthenticated = false;
            var role = '';
            var authToken;
            var isAdmin = false;
            var usertype = null;


            var loadUserCredentials = function () {
                var token = window.localStorage.getItem(LOCAL_TOKEN_KEY);

                if (token) {
                    var tokenAdmin = window.localStorage.getItem(LOCAL_TOKEN_KEY+"Admin");
                    var tokenType = window.localStorage.getItem(LOCAL_TOKEN_KEY+"Usertype");
                    console.log("loaded token from local storage:",token,tokenAdmin);
                    useCredentials({token:token,usertype:tokenType});
                }
            };

            var storeUserCredentials = function (userData) {
                useCredentials(userData);
                console.log("store token: " + userData.token, userData, isAuthenticated);
                window.localStorage.setItem(LOCAL_TOKEN_KEY, userData.token);
                window.localStorage.setItem(LOCAL_TOKEN_KEY+"Admin", isAdmin);
                window.localStorage.setItem(LOCAL_TOKEN_KEY+"Usertype", usertype);
                
            };

            function isAuthorized(authorizedRoles) {
                if (!angular.isArray(authorizedRoles)) {
                    authorizedRoles = [authorizedRoles];
                }
                return (isAuthenticated && authorizedRoles.indexOf(role) !== -1);

            }

            function getIsAdmin(){
                return isAdmin;
            }


           function useCredentials(userData) {
                var token = userData.token;
                //username = token.split('.')[0];
                isAuthenticated = true;
                authToken = token;
                if(userData.usertype=="admin") {
                    isAdmin = true;
                    console.log("isAdmin true");
                }
                usertype = userData.usertype;
                username = authToken;
                console.log("logininfo",userData, authToken, isAdmin);

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
                getIsAdmin : getIsAdmin,
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


            //admin crud requests
            this.requestAdmin = function(endpoint, method, data){
                if(!data) data = {};
                for(var k in data){
                    if(typeof(data[k])=="object" && data[k] instanceof Date ){
                        data[k] = data[k].getTime();
                    }
                }
                if(!data.tokenID) data.tokenID = localStorage.getItem(LStoken);
                var requestURL = API_URL+'admin/'+endpoint;
                if(method=="GET"){
                    requestURL+="?";
                    for(var key in data){
                        requestURL += encodeURIComponent(key) + "=" + encodeURIComponent(data[key]) +"&";
                    }
                }
                console.log(method,data);
                return $http({
                    method: method,
                    url: requestURL,
                    data: data,
                    params: (method=="DELETE"||method=="PUT")?data:undefined,
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
                                    else if(obj[key]==null) {
                                        str.push(encodeURIComponent(key) + "=");
                                    }
                                    else {
                                        str.push(encodeURIComponent(key) + "=" + encodeURIComponent(obj[key]));
                                    }
                                }
                                console.log(str, str.join("&"));
                                return str.join("&");
                            }, 
                    contentType: (method=='GET')?'application/json; charset=utf-8;':'application/x-www-form-urlencoded',
                    headers: (method=='GET')?{'Content-Type': 'application/json; charset=utf-8;','tokenID': localStorage.getItem(LStoken)}:{'Content-Type': 'application/x-www-form-urlencoded','tokenID': localStorage.getItem(LStoken)}
                });
            },

            this.JSON2CSV = function(objArray) {
                var array = typeof objArray != 'object' ? JSON.parse(objArray) : objArray;
                var str = '';
                var line = '';
                var hasLabels = true;
                var useQuotes = false;

                if (hasLabels) {
                    var head = array[0];
                    if (useQuotes) {
                        for (var index in array[0]) {
                            var value = index + "";
                            line += '"' + value.replace(/"/g, '""') + '",';
                        }
                    } else {
                        for (var index in array[0]) {
                            line += '"'+index + '",';
                        }
                    }

                    line = line.slice(0, -1);
                    str += line + '\r\n';
                }

                for (var i = 0; i < array.length; i++) {
                    var line = '';

                    if (useQuotes) {
                        for (var index in array[i]) {
                            var value = array[i][index] + "";
                            if(typeof(array[i][index])=="object"||typeof(array[i][index]=="array")){
                                value = JSON.stringify(array[i][index]);
                            }
                            
                            line += '"' + value.replace(/"/g, '""') + '",';
                        }
                    } else {
                        for (var index in array[i]) {
                            var value = array[i][index] + "";
                            if(typeof(array[i][index])=="object"||typeof(array[i][index]=="array")){
                                value = JSON.stringify(array[i][index]);
                                value = value.replace(/^"/,'').replace(/"$/,'');
                                value = value.replace(/"/g,'\\"');
                            } else {
                                value = value.replace(/^"/,'').replace(/"$/,'');
                            }
                            

                            line += '"'+value + '",';
                        }
                    }

                    line = line.slice(0, -1);
                    str += line + '\r\n';
                }
                return str;
            },

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


