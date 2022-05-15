package ru.geekbrains.job_finder.headHinterService.models.Enum;

public enum EmploymentEnum {
    FULL("full", "Полная занятость"),
    PART("part", "Частичная занятость"),
    PROJECT("project", "Проектная работа"),
    VOLUNTEER("volunteer", "Волонтерство"),
    PROBATION("probation", "Стажировка");

    private String id;

    private String name;

    EmploymentEnum(String id, String name) {
        this.id = id;
        this.name = name;
    }

    public String getId() {
        return id;
    }

    public String getName() {
        return name;
    }
}
