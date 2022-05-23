(function ($localStorage) {
  'use strict';

  angular
    .module('app', ['ngRoute', 'ngStorage'])
    .config(config)
    .run(run);

  function config($routeProvider, $httpProvider) {
    $routeProvider
      .when('/', {
        templateUrl: 'home/home.html',
        controller: 'homeController'
      })
      .when('/filter', {
        templateUrl: 'filter/filter.html',
        controller: 'filterController'
      })
      .when('/filter_list', {
        templateUrl: 'filterList/filterList.html',
        controller: 'filterListController'
      })
      .otherwise({
        redirectTo: '/'
      });
  }

  const contextPath = 'http://localhost:5555';

  function run($rootScope,$http,$localStorage){
    if($localStorage.currentUser){
      $http.defaults.headers.common.Authorization = $localStorage.currentUser.token;
    }
  }
})();

angular.module('app').controller('indexController', function ($scope, $http, $localStorage, $location) {

  const contextPath = 'http://localhost:5555';
  var vCode;
  const cUrlParams = new URLSearchParams(window.location.search);

  if($localStorage.apsToken){
    $http.defaults.headers.common.Authorization = $localStorage.apsToken;
  }

  HHAuthComplete = function() {
    vCode = cUrlParams.get("code");
    if(vCode != null){
      $localStorage.hh_auth_code = vCode;
    }
  }

  login = function(){
    if($localStorage.hh_auth_code && !$localStorage.currentUser){
      $http.get(contextPath + '/auth/login/' + $localStorage.hh_auth_code )
        .then(function successCallback (response){
          $localStorage.apsToken = response.data.apsToken;
          $http.defaults.headers.common.Authorization = $localStorage.apsToken;
          $localStorage.currentUser = {
            id: response.data.id,
            first_name: response.data.firstName,
            middle_name: response.data.middleName,
            last_name: response.data.lastName,
            email: response.data.email,
            is_new: response.data.isNew
          };
          delete $localStorage.hh_auth_code;
          window.location.href = "http://localhost:8080";
        }, function errorCallback(response) {});
    }
  }

  checkUser =function(){
    if ($localStorage.currentUser) {
      return true;
    } else {
      return false;
    }
  }

  $scope.tryToLogout = function () {
    $scope.clearUser();
    window.location.href = "http://localhost:8080";
  };

  $scope.clearUser = function () {
    delete $localStorage.currentUser;
    delete $localStorage.hh_auth_code;
    $http.defaults.headers.common.Authorization = '';
  };

  $scope.isUserLoggedIn = function () {
    if ($localStorage.currentUser) {
      return true;
    } else {
      return false;
    }
  };

  HHAuthComplete();
  login();
  checkUser();
});