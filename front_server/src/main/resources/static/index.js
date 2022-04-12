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
      .when('/groups', {
        templateUrl: 'groups/groups.html',
        controller: 'groupsController'
      })
      .when('/job_finder', {
        templateUrl: 'job_finder/job_finder.html',
        controller: 'groupsController'
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

  HHAuthComplete = function() {
    vCode = cUrlParams.get("code");
    if(vCode != null){
      $localStorage.hh_auth_code = vCode;
      window.location.href = "http://localhost:8080";
    }
  }

  login = function(){
    if($localStorage.hh_auth_code && !$localStorage.currentUser){
      $http.get(contextPath + '/auth/login/' + $localStorage.hh_auth_code )
        .then(function (response){
          $http.defaults.headers.common.Authorization = response.data.apsToken;
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
        });
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