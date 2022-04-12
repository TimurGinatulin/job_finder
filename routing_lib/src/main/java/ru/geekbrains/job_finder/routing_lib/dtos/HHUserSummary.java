package ru.geekbrains.job_finder.routing_lib.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class HHUserSummary {
    private Long id;
    private String firstName;
    private String middleName;
    private String lastName;
    private String email;
    private String accessToken;
    private String refreshToken;
}
