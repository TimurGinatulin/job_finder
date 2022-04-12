angular.module('app').controller('homeController', function ($scope, $http, $localStorage) {
  const contextPath = 'http://localhost:5555';

  checkUser = function(){
    hideElement("starterPage", $localStorage.currentUser);
    if($localStorage.currentUser){
      hideElement("greetingPage", !$localStorage.currentUser.is_new);
      hideElement("homePage", $localStorage.currentUser.is_new);
    }
  }

  hideElement = function(elmId, hide){
    var elem = document.getElementById(elmId);
    if(typeof elem !== 'undefined' && elem !== null) {
      elem.hidden = hide;
    }
  }
  hideElement("greetingPage", true);
  hideElement("homePage", true);
  checkUser();
});