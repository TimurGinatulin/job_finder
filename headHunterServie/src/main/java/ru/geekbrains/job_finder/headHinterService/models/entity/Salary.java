package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "salary")
@NoArgsConstructor
@Data
public class Salary {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "salary_id")
    private Long id;

    @Column(name = "code")
    private String code;

    @Column(name = "abbr")
    private String abbr;

    @Column(name = "name")
    private String name;

    @Column(name = "default_flag")
    private Double isDefault;

    @Column(name = "rate")
    private Double rate;

    @Column(name = "in_use")
    private Boolean isInUse;

    @OneToMany(mappedBy = "currency")
    private List<Filter> users;

}
