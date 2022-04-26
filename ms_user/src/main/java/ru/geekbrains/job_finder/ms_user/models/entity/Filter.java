package ru.geekbrains.job_finder.ms_user.models.entity;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;
import java.time.LocalDateTime;
import java.util.List;

@Entity
@Table(name = "filter")
@NoArgsConstructor
@Data
public class Filter {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "filter_id")
    private Long idFilter;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;

    @Column(name = "name")
    private String filterName;

    @Column(name = "text")
    private String text;

    @Column(name = "salary")
    private Long salary;

    @ManyToOne
    @JoinColumn(name = "currency_id")
    private Salary currency;

    @ManyToOne
    @JoinColumn(name = "area_id")
    private Area area;

    @ManyToOne
    @JoinColumn(name = "specialization_id")
    private Specialization specialization;

    @Column(name = "coverLetter")
    private String coverLetter;

    @Column(name = "timeStamp")
    private LocalDateTime timeStamp;

    @Column(name = "updatedAt")
    private LocalDateTime updatedAt;

    @Column(name = "deletedAt")
    private LocalDateTime deletedAt;

    @ManyToMany
    @JoinTable(
            name = "filter_employment",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "employment_id")
    )
    private List<Employment> employment;

    @ManyToMany
    @JoinTable(
            name = "filter_expirience",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "expirience_id")
    )
    private List<Experience> experience;

    @ManyToMany
    @JoinTable(
            name = "filter_schedule",
            joinColumns = @JoinColumn(name = "filter_id"),
            inverseJoinColumns = @JoinColumn(name = "schedule_id")
    )
    private List<Schedule> schedule;

}
