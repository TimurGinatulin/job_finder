angular.module('app').controller('filterController', function ($scope, $http, $localStorage) {
    var vSpecializations;
    var vIndustry;

    const contextPath = 'http://localhost:5555';
    const cJobTitleInput = document.getElementById('jobTitleInput');
    const cCountrySelector = document.getElementById('countrySelector');
    const cRegionSelector = document.getElementById('regionSelector');
    const cCitySelector = document.getElementById('citySelector');
    const cExperienceSelector = document.getElementById('experienceSelector');
    const cEmploymentSelector = document.getElementById('employmentSelector');
    const cScheduleSelector = document.getElementById('scheduleSelector');
    const cSpecializationSelector = document.getElementById('specializationSelector');
    const cSubSpecializationSelector = document.getElementById('subSpecializationSelector');
    const cIndustrySelector = document.getElementById('industrySelector');
    const cSubIndustrySelector = document.getElementById('subIndustrySelector');
    const cSalary = document.getElementById('salary');

    cSubIndustrySelector.addEventListener('change', updateFields);

    cIndustrySelector.addEventListener('change', updateFields);

    cSubSpecializationSelector.addEventListener('change', updateFields);

    cSpecializationSelector.addEventListener('change', updateFields);

    cScheduleSelector.addEventListener('change', updateFields);

    cJobTitleInput.addEventListener('input', updateFields);

    cExperienceSelector.addEventListener('change', updateFields);

    cEmploymentSelector.addEventListener('change', updateFields);

    cSalary.addEventListener('input', updateFields);

    if($localStorage.apsToken){
        $http.defaults.headers.common.Authorization = $localStorage.apsToken;
    }

    function updateFields(jobTitleInp) {
        checkFilterFields();
        if (cSpecializationSelector.value == 'Undefined')
            cSubSpecializationSelector.disabled = true;
        else {
            cSubSpecializationSelector.disabled = false;
            vSpecializations.forEach(mainSpec => {
                if (mainSpec.id == cSpecializationSelector.value) {
                    var subSpec = mainSpec.specializations;
                    subSpec.forEach(subSpec => {
                        var opt = document.createElement('option');
                        opt.value = subSpec.id;
                        opt.innerHTML = subSpec.name;
                        cSubSpecializationSelector.appendChild(opt);
                    });
                }
            });
        }
        if (cIndustrySelector.value == 'Undefined')
            cSubIndustrySelector.disabled = true;
        else {
            cSubIndustrySelector.disabled = false;
            vIndustry.forEach(mainInd => {
                if (mainInd.id == cIndustrySelector.value) {
                    var subInd = mainInd.industries;
                    subInd.forEach(subInd => {
                        var opt = document.createElement('option');
                        opt.value = subInd.id;
                        opt.innerHTML = subInd.name;
                        cSubIndustrySelector.appendChild(opt);
                    });
                }
            });
        }
    }

    cCountrySelector.addEventListener('change', changeCountry);

    function changeCountry(el) {
        checkFilterFields();
        if (cCountrySelector.value == 'Undefined')
            cRegionSelector.disabled = true;
        else {
            $http.get('https://api.hh.ru/areas/' + cCountrySelector.value)
                .then(function (response) {
                    var cityArray = response.data.areas;
                    cityArray.forEach(country => {
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

    function changeRegion(el) {
        checkFilterFields();
        if (cRegionSelector.value == 'Undefined')
            cCitySelector.disabled = true;
        else {
            $http.get('https://api.hh.ru/areas/' + cRegionSelector.value)
                .then(function (response) {
                    var cityArray = response.data.areas;
                    cityArray.forEach(country => {
                        var opt = document.createElement('option');
                        opt.value = country.id;
                        opt.innerHTML = country.name;
                        cCitySelector.appendChild(opt);
                    });
                });
            cCitySelector.disabled = false;
        }
    }

    cCitySelector.addEventListener('change', changeCity);

    function changeCity(el) {
        checkFilterFields();
    }

    checkFilterFields = function () {
        var requestParam = '?';
        var vAreaId = '';
        var vSpecId = '';
        var vIndId = '';
        if (cJobTitleInput.value.length > 0)
            requestParam = requestParam + 'text=' + cJobTitleInput.value + '&';

        if (cCountrySelector.value != 'Undefined')
            vAreaId = cCountrySelector.value;

        if (cRegionSelector.value != 'Undefined')
            vAreaId = cRegionSelector.value;

        if (cCitySelector.value != 'Undefined')
            vAreaId = cCitySelector.value;

        if (vAreaId.length > 0)
            requestParam = requestParam + 'area=' + vAreaId + '&';

        if (cSpecializationSelector.value != 'Undefined')
            vSpecId = cSpecializationSelector.value;

        if (cSubSpecializationSelector.value != 'Undefined')
            vSpecId = cSubSpecializationSelector.value;

        if (vSpecId.length > 0)
            requestParam = requestParam + 'specialization=' + vSpecId + '&';

        if (cIndustrySelector.value != 'Undefined')
            vIndId = cIndustrySelector.value;

        if (cSubIndustrySelector.value != 'Undefined') {
            vIndId = '';
            vIndId = cSubIndustrySelector.value;
        }

        if (vIndId.length > 0)
            requestParam = requestParam + 'industry=' + vIndId + '&';

        if (cExperienceSelector.value != 'Undefined')
            requestParam = requestParam + 'experience=' + cExperienceSelector.value + '&';

        if (cEmploymentSelector.value != 'Undefined')
            for (var i = 0; i < cEmploymentSelector.value.length; i++) {
                //todo
                requestParam = requestParam + 'employment=' + cEmploymentSelector.value[i] + '&';
            }

        if (cScheduleSelector.value != 'Undefined')
            requestParam = requestParam + 'schedule=' + cScheduleSelector.value + '&';

        if (cSalary.value.length > 0)
            requestParam = requestParam + 'salary=' + cSalary.value + '&';

        document.getElementById('test').innerHTML = '  ' + requestParam;

        $http.get('https://api.hh.ru/vacancies' + requestParam)
            .then(function (response) {
                document.getElementById('findVacancyLabel').innerHTML = 'Найдено ' + response.data.found + ' вакансий';
            });
    }

    getCountryDic = function () {
        const countrySelector = document.getElementById('countrySelector');
        $http.get('https://api.hh.ru/areas/countries')
            .then(function (response) {
                var countryArray = response.data;
                countryArray.forEach(country => {
                    var opt = document.createElement('option');
                    opt.value = country.id;
                    opt.innerHTML = country.name;
                    countrySelector.appendChild(opt);
                });
            });
    }

    receiveDictionaryForSelectors = function () {
        $http.get('https://api.hh.ru/specializations')
            .then(function (response) {
                vSpecializations = response.data;
                vSpecializations.forEach(mainSpec => {
                    var opt = document.createElement('option');
                    opt.value = mainSpec.id;
                    opt.innerHTML = mainSpec.name;
                    cSpecializationSelector.appendChild(opt);
                });
            });
        $http.get('https://api.hh.ru/industries')
            .then(function (response) {
                vIndustry = response.data;
                vIndustry.forEach(mainInd => {
                    var opt = document.createElement('option');
                    opt.value = mainInd.id;
                    opt.innerHTML = mainInd.name;
                    cIndustrySelector.appendChild(opt);
                });
            });

        $http.get('https://api.hh.ru/dictionaries')
            .then(function (response) {
                var experienceArray = response.data.experience;
                experienceArray.forEach(exp => {
                    var opt = document.createElement('option');
                    opt.value = exp.id;
                    opt.innerHTML = exp.name;
                    cExperienceSelector.appendChild(opt);
                });
                var employmentArray = response.data.employment;
                employmentArray.forEach(emp => {
                    var opt = document.createElement('option');
                    opt.value = emp.id;
                    opt.innerHTML = emp.name;
                    cEmploymentSelector.appendChild(opt);
                });
                var scheduleArray = response.data.schedule;
                scheduleArray.forEach(sch => {
                    var opt = document.createElement('option');
                    opt.value = sch.id;
                    opt.innerHTML = sch.name;
                    cScheduleSelector.appendChild(opt);
                });
            });
    }

    receiveDictionaryForSelectors();
    getCountryDic();
});