package ru.geekbrains.job_finder.routing_lib.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;

@FeignClient("user-ms")
public interface UserFeignClient {
    @PostMapping("/auth/add")
    void addUser(@RequestBody UserDto userDto);
}
