package ru.geekbrains.job_finder.headHinterService.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendResumeRequestBody {
    private Long vacancy_id;
    private String resume_id;
    private String message;
}
