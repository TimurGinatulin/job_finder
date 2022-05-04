package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@NoArgsConstructor
@Table(name = "specialization")
public class Specialization {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "specialization_id")
    private Double id;

    @Column(name = "name")
    private String name;

    @Column(name = "laboring")
    private Boolean laboring;

    @OneToMany(mappedBy = "parentSpecialization")
    private List<Specialization> specializations;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Specialization parentSpecialization;

    @OneToMany(mappedBy = "specialization")
    private List<Filter> users;

}
