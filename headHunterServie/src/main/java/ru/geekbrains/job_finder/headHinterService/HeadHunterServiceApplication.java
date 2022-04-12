package ru.geekbrains.job_finder.headHinterService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.openfeign.EnableFeignClients;

@EnableFeignClients(basePackages = "ru.geekbrains")
@SpringBootApplication(scanBasePackages = "ru.geekbrains")
public class HeadHunterServiceApplication {
    public static void main(String[] args) {
        SpringApplication.run(HeadHunterServiceApplication.class, args);
    }
}
