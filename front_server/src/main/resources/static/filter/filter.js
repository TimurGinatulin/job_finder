angular.module('app').controller('filterController', function ($scope, $http, $localStorage) {
  const contextPath = 'http://localhost:5555';
  const cJobTitleInput = document.getElementById('jobTitleInput');
  const cCountrySelector = document.getElementById('countrySelector');
  const cRegionSelector = document.getElementById('regionSelector');
  const cCitySelector = document.getElementById('citySelector');

  cJobTitleInput.addEventListener('input', updateJobTitle);
  function updateJobTitle(jobTitleInp){
    checkFilterFields();
  }

  cCountrySelector.addEventListener('change', changeCountry);
  function changeCountry(el){
    if(cCountrySelector.value == 'Undefined')
      cRegionSelector.disabled = true;
    else{
      $http.get('https://api.hh.ru/areas/' + cCountrySelector.value)
        .then(function (response){
          var cityArray = response.data.areas;
          cityArray.forEach(country =>{
            var opt = document.createElement('option');
            opt.value = country.id;
            opt.innerHTML = country.name;
            cRegionSelector.appendChild(opt);
          });
        });
      cRegionSelector.disabled = false;
    }
  }

  cRegionSelector.addEventListener('change', changeRegion);
  function changeRegion(el){
    if(cRegionSelector.value == 'Undefined')
      cCitySelector.disabled = true;
    else{
      $http.get('https://api.hh.ru/areas/' + cRegionSelector.value)
        .then(function (response){
          var cityArray = response.data.areas;
          cityArray.forEach(country =>{
            var opt = document.createElement('option');
            opt.value = country.id;
            opt.innerHTML = country.name;
            cCitySelector.appendChild(opt);
          });
        });
      cCitySelector.disabled = false;
    }
  }

  checkFilterFields = function(){
    var requestParam = '?';
    if(cJobTitleInput.value.length > 0){
      requestParam = requestParam + 'text=' + cJobTitleInput.value;
    }
    $http.get('https://api.hh.ru/vacancies' + requestParam)
      .then(function (response){
        document.getElementById('findVacancyLabel').innerHTML = 'Найдено ' + response.data.found + ' вакансий';
      });
  }

  getCountryDic = function(){
    const countrySelector = document.getElementById('countrySelector');
    $http.get('https://api.hh.ru/areas/countries')
      .then(function (response){
        var countryArray = response.data;
        countryArray.forEach(country =>{
          var opt = document.createElement('option');
          opt.value = country.id;
          opt.innerHTML = country.name;
          countrySelector.appendChild(opt);
        });
      });
  }

  getCountryDic();
});