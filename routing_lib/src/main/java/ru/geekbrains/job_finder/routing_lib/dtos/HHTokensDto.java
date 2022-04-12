package ru.geekbrains.job_finder.routing_lib.dtos;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class HHTokensDto {
    private String id_user;
    private String accessToken;
    private String refreshToken;
}
