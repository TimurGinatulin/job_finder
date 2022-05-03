package ru.geekbrains.job_finder.headHinterService.models.entity;


import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Table;
@Data
@NoArgsConstructor
@Table(name = "currency")
public class Currency {
    @Column(name = "id")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "rate")
    private Double rate;
}
