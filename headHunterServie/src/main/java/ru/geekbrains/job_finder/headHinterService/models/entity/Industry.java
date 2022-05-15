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
@Table(name = "industry")
public class Industry {

    @Id
    @Column(name = "industry_id")
    private Double id;

    @Column(name = "name")
    private String name;
    @JsonIgnore
    @OneToMany(mappedBy = "industry")
    private List<Filter> filters;

    @ManyToOne
    @JoinColumn(name = "parent_id")
    private Industry industry;

}
