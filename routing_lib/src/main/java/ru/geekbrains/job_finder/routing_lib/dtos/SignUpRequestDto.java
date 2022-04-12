package ru.geekbrains.job_finder.routing_lib.dtos;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class SignUpRequestDto {
    private String email;
    private String telegramId;
    private String phone;
    private String password;
}
