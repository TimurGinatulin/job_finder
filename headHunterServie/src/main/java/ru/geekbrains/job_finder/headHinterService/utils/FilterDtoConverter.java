package ru.geekbrains.job_finder.headHinterService.utils;

import org.springframework.core.convert.converter.Converter;
import org.springframework.stereotype.Component;
import ru.geekbrains.job_finder.headHinterService.models.entity.*;
import ru.geekbrains.job_finder.routing_lib.dtos.FilterDto;

import java.util.Collections;
import java.util.Optional;
import java.util.stream.Collectors;

@Component
public class FilterDtoConverter implements Converter<Filter, FilterDto> {
    @Override
    public FilterDto convert(Filter filter) {
        return FilterDto.builder()
                .id(filter.getIdFilter())
                .filterName(filter.getFilterName())
                .area(Optional.ofNullable(filter.getArea()).map(Area::getId).orElse(null))
                .text(filter.getText())
                .experience(filter.getExperience() != null ? filter.getExperience().stream().map(Experience::getId).collect(Collectors.toList()) : Collections.emptyList())
                .salary(filter.getSalary())
                .currencyCode(Optional.ofNullable(filter.getCurrency()).map(Currency::getCode).orElse(null))
                .schedule(filter.getSchedule() != null ? filter.getSchedule().stream().map(Schedule::getId).collect(Collectors.toList()) : Collections.emptyList())
                .industry(Optional.ofNullable(filter.getIndustry()).map(ind -> String.valueOf(ind.getId())).orElse(null))
                .specializations(Optional.ofNullable(filter.getSpecialization()).map(spec -> String.valueOf(spec.getId())).orElse(null))
                .isActive(filter.getDeletedAt() == null)
                .build();
    }
}
