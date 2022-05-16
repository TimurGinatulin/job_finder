package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.headHinterService.models.Enum.ExperienceEnum;

import javax.persistence.*;
@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "experience")
public class Experience {

    @Id
    @Column(name = "experience_id")
    private String id;

    @Column(name = "name")
    private String name;

    public Experience(ExperienceEnum experienceEnum) {
        this.id = experienceEnum.getId();
        this.name = experienceEnum.name();
    }
}
