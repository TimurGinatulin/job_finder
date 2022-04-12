package ru.geekbrains.job_finder.ms_user.controllers;

import com.netflix.hystrix.contrib.javanica.annotation.HystrixCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import ru.geekbrains.job_finder.ms_user.services.UserService;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;

@RestController
@RequestMapping("/auth")
public class AuthenticationController {
    @Autowired
    private UserService service;

    @GetMapping("/login/{code}")
    public UserDto login(@PathVariable(name = "code") String code) {
        return service.login(code);
    }

    @PostMapping("/add")
    @ResponseStatus(HttpStatus.CREATED)
    @HystrixCommand(fallbackMethod = "faultedAddUser")
    public void addUser(@RequestBody UserDto userDto) {
        service.addUser(userDto);
    }

    @ResponseStatus(HttpStatus.LOCKED)
    public void faultedAddUser(UserDto userDto) {
    }
}
