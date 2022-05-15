package ru.geekbrains.job_finder.headHinterService.models.Enum;

public enum ExperienceEnum {
    NO_EXPERIENCE("noExperience", "Нет опыта"),
    BETWEEN_1_AND_3("between1And3", "От 1 года до 3 лет"),
    BETWEEN_3_AND_6("between3And6", "От 3 до 6 лет"),
    MORE_THEN_6("moreThan6", "Более 6 лет");

    private String id;

    private String name;

    ExperienceEnum(String id, String name) {
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
