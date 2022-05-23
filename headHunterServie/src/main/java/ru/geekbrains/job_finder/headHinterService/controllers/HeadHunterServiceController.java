package ru.geekbrains.job_finder.headHinterService.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.job_finder.cor_lib.models.UserInfo;
import ru.geekbrains.job_finder.headHinterService.models.entity.Industry;
import ru.geekbrains.job_finder.headHinterService.models.entity.Specialization;
import ru.geekbrains.job_finder.headHinterService.services.HeadHunterService;
import ru.geekbrains.job_finder.routing_lib.dtos.FilterDto;
import ru.geekbrains.job_finder.routing_lib.dtos.ResumeDTO;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;

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

    @GetMapping("/specializations")
    public List<Specialization> getSpecialization() {
        return service.findAllSpec();
    }

    @GetMapping("/specializations/{id}")
    public List<Specialization> getSpecializationById(@PathVariable Double id) {
        return service.findAllSpec(id);
    }

    @GetMapping("/industry")
    public List<Industry> getIndustry() {
        return service.findAllInd();
    }

    @GetMapping("/industry/{id}")
    public List<Industry> getIndustryById(@PathVariable Double id) {
        return service.findAllInd(id);
    }

    @GetMapping("/filters")
    public List<FilterDto> getFilters() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        return service.findAll(principal.getId());
    }

    @GetMapping("/filters/{id}")
    public FilterDto getFilterById(@PathVariable Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        return service.findById(id, principal.getId());
    }
    @GetMapping("/filters/disable/{id}")
    public void disableFilterById(@PathVariable Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        service.disable(id, principal.getId());
    }

    @GetMapping("/resume")
    public List<ResumeDTO> getResume() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        return service.getResumeList(principal.getId());
    }

    @PostMapping("/filters")
    public void saveFilter(@RequestBody FilterDto dto) {
        UserInfo userInfo = (UserInfo) SecurityContextHolder.getContext().getAuthentication().getPrincipal();
        service.saveFilter(dto, userInfo);
        System.out.println(dto);
    }

    public UserDto faultedGetUserByCode(String code) {
        return UserDto.builder().id(-1L).build();
    }
}
