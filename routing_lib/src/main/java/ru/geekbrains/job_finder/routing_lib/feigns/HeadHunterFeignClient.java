package ru.geekbrains.job_finder.routing_lib.feigns;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;

@FeignClient("headHunter-ms")
public interface HeadHunterFeignClient {
    @GetMapping("/hh_service/get_by_code")
    UserDto getUserByCode(@RequestParam(name = "code") String code);
}
