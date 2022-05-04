package ru.geekbrains.job_finder.routing_lib.dtos;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class ResumeDTO {
    private String id;
    private String title;
}
