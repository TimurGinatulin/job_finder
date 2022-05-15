package ru.geekbrains.job_finder.headHinterService.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Vacancy {
    private Long id;
    private String name;
}
