angular.module('app').controller('homeController', function ($scope, $http, $localStorage) {
  const contextPath = 'http://localhost:5555';

  checkUser = function(){
    hideElement("starterPage", $localStorage.currentUser);
    if($localStorage.currentUser){
      hideElement("greetingPage", !$localStorage.currentUser.is_new);
      hideElement("homePage", $localStorage.currentUser.is_new);
      $localStorage.currentUser.is_new = false;

      $scope.first_name = $localStorage.currentUser.first_name != 'null'
        ? $localStorage.currentUser.first_name
        : '';
      $scope.middle_name = $localStorage.currentUser.middle_name != 'null'
        ? $localStorage.currentUser.middle_name
        : '';
      $scope.last_name = $localStorage.currentUser.last_name != 'null'
      ? $localStorage.currentUser.last_name
      : "";
    }
  }

  hideElement = function(elmId, hide){
    var elem = document.getElementById(elmId);
    if(typeof elem !== 'undefined' && elem !== null) {
      elem.hidden = hide;
    }
  }

  $scope.accept = function(){
    $localStorage.currentUser.is_new = false;
    window.location.href = "http://localhost:8080";
  }
  hideElement("greetingPage", true);
  hideElement("homePage", true);
  checkUser();
});