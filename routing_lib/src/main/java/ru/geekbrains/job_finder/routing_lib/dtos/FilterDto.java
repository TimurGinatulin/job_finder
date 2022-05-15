package ru.geekbrains.job_finder.routing_lib.dtos;

import lombok.*;

import java.util.List;

@Setter
@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class FilterDto {
    private Long idFilter;
    private String summary;
    private String filterName;
    private String text;
    private List<String> experience;
    private Long salary;
    private String currencyCode;
    private Long area;
    private List<String> employment;
    private List<String> schedule;
    private Double specializations;
    private Double industry;
    private String coverLetter;
    private Boolean isActive;
    private Integer totalSends;
}
