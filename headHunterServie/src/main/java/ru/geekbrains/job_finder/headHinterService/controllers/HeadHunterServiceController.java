package ru.geekbrains.job_finder.headHinterService.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.job_finder.headHinterService.services.HeadHunterService;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;

@RestController
@RequestMapping("/hh_service")
public class HeadHunterServiceController {
    @Autowired
    private HeadHunterService service;

    @GetMapping("/get_by_code")
    @HystrixCommand(fallbackMethod = "faultedGetUserByCode")
    public UserDto getUserByCode(@RequestParam(name = "code") String code) {
        return service.getUserByCode(code);
    }

    public UserDto faultedGetUserByCode(String code) {
        return UserDto.builder().id(-1L).build();
    }
}
