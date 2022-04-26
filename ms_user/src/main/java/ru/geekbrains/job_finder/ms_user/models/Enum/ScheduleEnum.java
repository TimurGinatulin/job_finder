package ru.geekbrains.job_finder.ms_user.models.Enum;

public enum ScheduleEnum {
    FULL_DAY("fullDay", "Полный день"),
    SHIFT("shift", "Сменный график"),
    FLEXIBLE("flexible", "Гибкий график"),
    REMOTE("remote", "Удаленная работа"),
    FLY_IN_FLY_OUT("flyInFlyOut", "Вахтовый метод");

    private String id;

    private String name;

    ScheduleEnum(String id, String name) {
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
