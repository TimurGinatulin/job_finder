package ru.geekbrains.job_finder.routing_lib.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthRequestDto {
    private String email;
    private String password;
}
