package ru.geekbrains.job_finder.routing_lib.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class AuthResponseDto {
    private String id;
    private String token;
}
