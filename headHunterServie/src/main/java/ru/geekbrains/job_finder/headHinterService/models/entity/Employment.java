package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.headHinterService.models.Enum.EmploymentEnum;

import javax.persistence.*;
@Data
@Entity
@NoArgsConstructor
@Table(name = "empolyment")
public class Employment {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "empolyment_id")
    private String id;

    @Column(name = "name")
    private String name;

    Employment(EmploymentEnum employmentEnum) {
        this.id = employmentEnum.getId();
        this.name = employmentEnum.getName();
    }

}
