package ru.geekbrains.job_finder.headHinterService.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContext;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.job_finder.cor_lib.exception.models.Forbidden;
import ru.geekbrains.job_finder.cor_lib.exception.models.NotFoundException;
import ru.geekbrains.job_finder.cor_lib.models.UserInfo;
import ru.geekbrains.job_finder.headHinterService.models.entity.Filter;
import ru.geekbrains.job_finder.headHinterService.services.HeadHunterService;
import ru.geekbrains.job_finder.headHinterService.utils.FilterDtoConverter;
import ru.geekbrains.job_finder.routing_lib.dtos.FilterDto;
import ru.geekbrains.job_finder.routing_lib.dtos.ResumeDTO;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;
import java.util.List;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/hh_service")
public class HeadHunterServiceController {

    @Autowired
    private HeadHunterService service;
    @Autowired
    private FilterDtoConverter filterDtoConverter;

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

    @GetMapping("/hh_service/filter")
    public List<FilterDto> getUserFilters() {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        List<Filter> filters = service.getUserFilters(principal.getId());
        return filters.stream()
                .map(filter -> filterDtoConverter.convert(filter))
                .collect(Collectors.toList());
    }

    @GetMapping("/hh_service/filter/{id}")
    public FilterDto getFilterById(@PathVariable Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        Filter filter = service.getFilterById(id);
        if (filter == null) {
            throw new NotFoundException("Фильтр не найден");
        }
        if (!filter.getUserId().equals(principal.getId())) {
            throw new Forbidden("У пользователя нет доступа к данному фильтру");
        }
        return filterDtoConverter.convert(filter);
    }

    @PostMapping("/hh_service/filter")
    public void saveFilter(@RequestBody FilterDto filter) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        service.create(filter, principal.getId());
    }

    @PutMapping("/hh_service/filter")
    public void updateFilter(@RequestBody FilterDto filter) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        service.update(filter, principal.getId());
    }

    @DeleteMapping("/hh_service/filter/{id}")
    public void deleteFilter(@PathVariable Long id) {
        SecurityContext securityContext = SecurityContextHolder.getContext();
        UserInfo principal = (UserInfo) securityContext.getAuthentication().getPrincipal();
        service.delete(id, principal.getId());
    }

    public UserDto faultedGetUserByCode(String code) {
        return UserDto.builder().id(-1L).build();
    }
}
