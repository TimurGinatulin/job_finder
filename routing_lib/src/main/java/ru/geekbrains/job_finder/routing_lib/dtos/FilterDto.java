package ru.geekbrains.job_finder.routing_lib.dtos;

import lombok.Builder;
import lombok.Getter;

import java.util.List;

@Builder
@Getter
public class FilterDto {
    private Long id;
    private String summary;
    private String filterName;
    private String text;
    private List<String> experience;
    private Long salary;
    private String currencyCode;
    private Long area;
    private List<String> employment;
    private List<String> schedule;
    private String specializations;
    private String industry;
    private String coverLetter;
    private Boolean isActive;
    private Integer totalSends;
}
