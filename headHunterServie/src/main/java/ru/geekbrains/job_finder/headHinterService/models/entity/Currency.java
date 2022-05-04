package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.List;
@Entity
@Setter
@NoArgsConstructor
@Table(name = "currency")
public class Currency {
    @Id
    @Column(name = "id")
    private String code;
    @Column(name = "name")
    private String name;
    @Column(name = "rate")
    private Double rate;
    @ManyToMany(mappedBy = "currency")
    private List<Filter> filters;

    public String getCode() {
        return code;
    }

    public String getName() {
        return name;
    }

    public Double getRate() {
        return rate;
    }
}
