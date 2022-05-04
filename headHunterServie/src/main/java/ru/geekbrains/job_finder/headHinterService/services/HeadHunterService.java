package ru.geekbrains.job_finder.headHinterService.services;

import com.fasterxml.jackson.core.JsonProcessingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.job_finder.headHinterService.models.Vacancy;
import ru.geekbrains.job_finder.headHinterService.models.entity.Filter;
import ru.geekbrains.job_finder.headHinterService.models.entity.UserToken;
import ru.geekbrains.job_finder.headHinterService.repositories.HeadHunterApiRepository;
import ru.geekbrains.job_finder.headHinterService.repositories.HeadHunterDBRepository;
import ru.geekbrains.job_finder.headHinterService.repositories.JobMemoryRepository;
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
    private HeadHunterDBRepository dbRepository;
    @Autowired
    private UserFeignClient userFeignClient;
    @Autowired
    private JobMemoryRepository jobMemoryRepository;

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
}
