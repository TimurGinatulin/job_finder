package ru.geekbrains.job_finder.headHinterService.models.entity;

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
@Table(name = "area")
public class Area {

    @Id
    @Column(name = "area_id")
    private Long id;

    @Column(name = "name")
    private String name;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Area parentArea;

    @OneToMany(mappedBy = "parentArea")
    private List<Area> areas;

    @OneToMany(mappedBy = "area")
    private List<Filter> users;

}
