package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.util.List;

@Entity
@Table(name = "industry")
@NoArgsConstructor
@Data
public class Industry {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "industry_id")
    private Double id;

    @Column(name = "name")
    private String name;

    @OneToMany(mappedBy = "industry")
    private List<Industry> industries;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Industry industry;

}
