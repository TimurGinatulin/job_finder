package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.headHinterService.models.Enum.EmploymentEnum;

import javax.persistence.*;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "empolyment")
public class Employment {

    @Id
    @Column(name = "empolyment_id")
    private String id;

    @Column(name = "name")
    private String name;

    Employment(EmploymentEnum employmentEnum) {
        this.id = employmentEnum.getId();
        this.name = employmentEnum.getName();
    }

}
