package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.headHinterService.models.Enum.ScheduleEnum;

import javax.persistence.*;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "schedule")
public class Schedule {

    @Id
    private String id;

    @Column(name = "name")
    private String name;

    public Schedule(ScheduleEnum scheduleEnum) {
        this.id = scheduleEnum.getId();
        this.name = scheduleEnum.getName();
    }
}
