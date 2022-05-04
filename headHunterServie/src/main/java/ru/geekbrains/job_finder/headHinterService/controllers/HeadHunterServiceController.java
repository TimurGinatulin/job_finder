package ru.geekbrains.job_finder.headHinterService.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import ru.geekbrains.job_finder.cor_lib.models.UserInfo;
import ru.geekbrains.job_finder.headHinterService.services.HeadHunterService;
import ru.geekbrains.job_finder.routing_lib.dtos.FilterDto;
import ru.geekbrains.job_finder.routing_lib.dtos.ResumeDTO;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;

import java.security.Principal;
import java.security.Security;
import java.util.Collections;
import java.util.List;

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

    @GetMapping("/filters")
    public List<FilterDto> getFilters() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        return List.of(FilterDto.builder().summary("Programmer").totalSends(123).isActive(true).build(),
                FilterDto.builder().summary("Sheff").totalSends(13).isActive(false).build(),
                FilterDto.builder().summary("driver").totalSends(999).isActive(true).build());
    }
    @GetMapping("/resume")
    public List<ResumeDTO> getResume(){
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        return service.getResumeList(principal.getId());
    }

    public UserDto faultedGetUserByCode(String code) {
        return UserDto.builder().id(-1L).build();
    }
}
