package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "area")
@NoArgsConstructor
@Data
public class Area {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
