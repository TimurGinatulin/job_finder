package ru.geekbrains.job_finder.headHinterService.models.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "specialization")
public class Specialization {

    @Id
    @Column(name = "specialization_id")
    private Double id;

    @Column(name = "name")
    private String name;

    @Column(name = "laboring")
    private Boolean laboring;
    @JsonIgnore
    @OneToMany(mappedBy = "parentSpecialization")
    private List<Specialization> specializations;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Specialization parentSpecialization;
    @JsonIgnore
    @OneToMany(mappedBy = "specialization")
    private List<Filter> users;

}
