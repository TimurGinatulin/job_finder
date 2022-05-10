package ru.geekbrains.job_finder.headHinterService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import lombok.AllArgsConstructor;
import org.json.JSONArray;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpMethod;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;
import ru.geekbrains.job_finder.headHinterService.models.Vacancy;
import ru.geekbrains.job_finder.headHinterService.models.entity.*;
import ru.geekbrains.job_finder.headHinterService.repositories.*;
import ru.geekbrains.job_finder.routing_lib.dtos.HHUserSummary;
import ru.geekbrains.job_finder.routing_lib.dtos.ResumeDTO;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;
import ru.geekbrains.job_finder.routing_lib.feigns.UserFeignClient;

import java.util.List;

@Service
public class HeadHunterService {
    @Autowired
    private HeadHunterApiRepository apiRepository;
    @Autowired
    private AreaDBRepository areaDBRepository;
    @Autowired
    private IndustryDBRepository industryDBRepository;
    @Autowired
    private SpecializationDBRepository specializationDBRepository;
    @Autowired
    private CurrencyDBRepository currencyDBRepository;
    @Autowired
    private EmploymentDBRepository employmentDBRepository;
    @Autowired
    private ExperienceDBRepository experienceDBRepository;
    @Autowired
    private ScheduleDBRepository scheduleDBRepository;
    @Autowired
    private HeadHunterDBRepository dbRepository;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private JobMemoryRepository jobMemoryRepository;
    @Autowired
    private RestTemplate restTemplate;

    public UserDto getUserByCode(String code) {
        HHUserSummary hhUserProperties = apiRepository.getHHUserPropertiesByCode(code);
        UserToken userToken = dbRepository.findById(hhUserProperties.getId()).orElse(null);
        UserDto userDto = UserDto.builder()
                .id(hhUserProperties.getId())
                .firstName(hhUserProperties.getFirstName())
                .middleName(hhUserProperties.getMiddleName())
                .email(hhUserProperties.getEmail())
                .lastName(hhUserProperties.getLastName())
                .build();
        if (userToken != null) {
            userDto.setIsNew(false);
        } else {
            userDto.setIsNew(true);
            userFeignClient.addUser(userDto);
        }
        dbRepository.save(new UserToken(hhUserProperties));
        return userDto;
    }

    public void sendResumeResponse(Filter filter) {
        List<Vacancy> filteredVacancy = apiRepository.getFilteredVacancy(filter);
        UserToken userToken = dbRepository.findById(filter.getUserId()).orElse(null);
        List<Long> idVacancyListByIdSummary =
                jobMemoryRepository.findIdVacancyListByIdSummary(filter.getSummaryId());
        if (userToken != null) {
            List<Vacancy> newVacancy = filteredVacancy.stream()
                    .filter(vacancy -> idVacancyListByIdSummary.contains(vacancy.getId()))
                    .toList();
            newVacancy.forEach(vacancy -> {
                try {
                    apiRepository.sendResumeResponse(userToken.getAccessToken(), vacancy, filter);
                } catch (JsonProcessingException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }

    public List<ResumeDTO> getResumeList(Long id) {
        UserToken userToken = dbRepository.findById(id).orElse(null);
        if (userToken != null) {
            return apiRepository.getResumeList(userToken.getAccessToken());
        }
        return null;
    }

    @Scheduled(fixedRate = 24 * 60 * 60 * 1000)
    public void updateDictionaryInDB() {
        String url = "https://api.hh.ru/areas/";
        ResponseEntity<String> response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        JSONArray jsonResponse = new JSONArray(response.getBody());
        addAreas(jsonResponse);
        System.out.println("Updated areas");
        url = "https://api.hh.ru/industries";
        response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        jsonResponse = new JSONArray(response.getBody());
        for (Object o : jsonResponse) {
            JSONObject indJSON = (JSONObject) o;
            industryDBRepository.save(Industry.builder()
                    .id(indJSON.getDouble("id"))
                    .name(indJSON.getString("name"))
                    .industry(null)
                    .build());
            JSONArray subIndustries = indJSON.getJSONArray("industries");
            for (Object s : subIndustries) {
                JSONObject subIndJSON = (JSONObject) s;
                industryDBRepository.save(Industry.builder()
                        .id(subIndJSON.getDouble("id"))
                        .name(subIndJSON.getString("name"))
                        .industry(industryDBRepository.findById(indJSON.getDouble("id")).orElse(null))
                        .build());
            }
        }
        System.out.println("Updated Industry");
        url = "https://api.hh.ru/specializations";
        response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        jsonResponse = new JSONArray(response.getBody());
        for (Object o : jsonResponse) {
            JSONObject indJSON = (JSONObject) o;
            specializationDBRepository.save(Specialization.builder()
                    .id(indJSON.getDouble("id"))
                    .name(indJSON.getString("name"))
                    .laboring(indJSON.has("laboring") ? indJSON.getBoolean("laboring") : false)
                    .parentSpecialization(null)
                    .build());
            JSONArray subIndustries = indJSON.getJSONArray("specializations");
            for (Object s : subIndustries) {
                JSONObject subIndJSON = (JSONObject) s;
                specializationDBRepository.save(Specialization.builder()
                        .id(subIndJSON.getDouble("id"))
                        .name(subIndJSON.getString("name"))
                        .laboring(subIndJSON.has("laboring") ? subIndJSON.getBoolean("laboring") : false)
                        .parentSpecialization(specializationDBRepository.findById(indJSON.getDouble("id")).orElse(null))
                        .build());
            }
        }
        System.out.println("Updated specializations");
        url = "https://api.hh.ru/dictionaries";
        response = restTemplate.exchange(url, HttpMethod.GET, null, String.class);
        JSONObject dictionaryJson = new JSONObject(response.getBody());
        for (Object curObj : dictionaryJson.getJSONArray("currency")) {
            JSONObject curJson = (JSONObject) curObj;
            currencyDBRepository.save(Currency.builder()
                    .code(curJson.getString("code"))
                    .rate(curJson.getDouble("rate"))
                    .name(curJson.getString("name"))
                    .build());
        }
        System.out.println("Currency dictionary update");
        for (Object empObj : dictionaryJson.getJSONArray("employment")) {
            JSONObject epJson = (JSONObject) empObj;
            employmentDBRepository.save(Employment.builder()
                    .name(epJson.getString("name"))
                    .id(epJson.getString("id"))
                    .build());
        }
        System.out.println("Employment dictionary update");
        for (Object expObj : dictionaryJson.getJSONArray("experience")) {
            JSONObject expJson = (JSONObject) expObj;
            experienceDBRepository.save(Experience.builder()
                    .name(expJson.getString("name"))
                    .id(expJson.getString("id"))
                    .build());
        }
        System.out.println("Experience dictionary update");
        for (Object schObj : dictionaryJson.getJSONArray("schedule")) {
            JSONObject schJson = (JSONObject) schObj;
            scheduleDBRepository.save(Schedule.builder()
                    .name(schJson.getString("name"))
                    .id(schJson.getString("id"))
                    .build());
        }
        System.out.println("Schedule dictionary update");
    }

    private void addAreas(JSONArray areas) {
        for (Object o : areas) {
            JSONObject areaJson = (JSONObject) o;
            areaDBRepository.save(Area.builder()
                    .id(areaJson.getLong("id"))
                    .name(areaJson.getString("name"))
                    .parentArea(areaDBRepository.findById(!areaJson.get("parent_id").equals(null) ? areaJson.getLong("parent_id") : -1L).orElse(null))
                    .build());
            if (areaJson.getJSONArray("areas").length() > 0) {
                addAreas(areaJson.getJSONArray("areas"));
            }
        }
    }

}
