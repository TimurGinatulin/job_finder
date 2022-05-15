package ru.geekbrains.job_finder.headHinterService.models;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class SendResumeRequestBody {
    private Long vacancyId;
    private String resumeId;
    private String message;
}
