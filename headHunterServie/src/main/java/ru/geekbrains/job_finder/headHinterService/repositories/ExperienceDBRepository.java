package ru.geekbrains.job_finder.headHinterService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.job_finder.headHinterService.models.entity.Experience;

public interface ExperienceDBRepository extends JpaRepository<Experience, String> {
}
