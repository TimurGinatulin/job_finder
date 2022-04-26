package ru.geekbrains.job_finder.ms_user.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.ms_user.models.Enum.ExperienceEnum;
import ru.geekbrains.job_finder.ms_user.models.Enum.RoleEnums;

import javax.persistence.*;

@Entity
@Table(name = "experience")
@NoArgsConstructor
@Data
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "experience_id")
    private String id;

    @Column(name = "name")
    private String name;

    public Experience(ExperienceEnum experienceEnum) {
        this.id = experienceEnum.getId();
        this.name = experienceEnum.name();
    }
}
