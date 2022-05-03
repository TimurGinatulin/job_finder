package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.ms_user.models.Enum.ScheduleEnum;

import javax.persistence.*;

@Entity
@Table(name = "schedule")
@NoArgsConstructor
@Data
public class Schedule {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    private String id;

    @Column(name = "name")
    private String name;

    public Schedule(ScheduleEnum scheduleEnum) {
        this.id = scheduleEnum.getId();
        this.name = scheduleEnum.getName();
    }
}
