angular.module('app').controller('filterListController', function ($scope, $http, $localStorage) {
  const contextPath = 'http://localhost:5555';
  const cFilterItemElements = document.getElementsByClassName('list-group-item');
  const cFilterGroupElement = document.getElementById('filterListGroup');

  var testResponse = [
  {resume: 'programmer',totalSend: 124,cond: 'true'},{resume: 'sheff',totalSend: 14,cond: 'false'},{resume: 'driver',totalSend: 1243,cond: 'true'}
  ];

  doSmt = function(){
    for(var i = 0; i < cFilterItemElements.length; i++){
      console.log('filterHeader ' + cFilterItemElements[i].getElementsByClassName('filterHeader')[0].textContent
      + 'condition ' + cFilterItemElements[i].getElementsByClassName('condition')[0].textContent
      );
    }
  }
  //doSmt();
  addFilterAtTable = function(filterArray){
    filterArray.forEach(el=>{
    var vAElement = document.createElement('a');
    vAElement.href = '#';
    vAElement.classList.add('list-group-item');
    vAElement.classList.add('list-group-item-action');
    vAElement.ariaCurrent = 'true';

    var vDivElement = document.createElement('div');
    vDivElement.classList.add('d-flex');
    vDivElement.classList.add('w-100');
    vDivElement.classList.add('justify-content-between');

    var vHeaderFilterItem = document.createElement('h5');
    vHeaderFilterItem.classList.add('mb-1');
    vHeaderFilterItem.classList.add('filterHeader');
    vHeaderFilterItem.innerHTML = el.summary;

    var vConditionElement = document.createElement('small');
    vConditionElement.classList.add('condition');
    if(el.isActive == true)
      vConditionElement.innerHTML = '&#128994';
    else
      vConditionElement.innerHTML = '&#128308';

    var vSpendCounter = document.createElement('small');
    vSpendCounter.classList.add('text-muted');
    vSpendCounter.innerHTML = 'Total send\'s ' + el.totalSends;

    vDivElement.appendChild(vHeaderFilterItem);
    vDivElement.appendChild(vConditionElement);
    vAElement.appendChild(vDivElement);
    vAElement.appendChild(vSpendCounter);
    cFilterGroupElement.appendChild(vAElement);
    });
  }
  uploadFilterList = function(){
  console.log($http.defaults.headers.common.Authorization);
    $http.get(contextPath + '/hh_service/filters')
      .then(function (response){
        let filterArray = response.data;
        addFilterAtTable(filterArray);
        console.log(filterArray);
      });
  }
  uploadFilterList();
});