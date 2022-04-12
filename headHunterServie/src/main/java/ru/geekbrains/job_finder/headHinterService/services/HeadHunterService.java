package ru.geekbrains.job_finder.headHinterService.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.geekbrains.job_finder.headHinterService.models.entity.UserToken;
import ru.geekbrains.job_finder.headHinterService.repositories.HeadHunterApiRepository;
import ru.geekbrains.job_finder.headHinterService.repositories.HeadHunterDBRepository;
import ru.geekbrains.job_finder.routing_lib.dtos.HHUserSummary;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;
import ru.geekbrains.job_finder.routing_lib.feigns.UserFeignClient;

@Service
public class HeadHunterService {
    @Autowired
    private HeadHunterApiRepository apiRepository;
    @Autowired
    private HeadHunterDBRepository dbRepository;
    @Autowired
    private UserFeignClient userFeignClient;

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

}
