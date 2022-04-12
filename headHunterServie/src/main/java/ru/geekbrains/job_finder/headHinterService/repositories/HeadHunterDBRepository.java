package ru.geekbrains.job_finder.headHinterService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.job_finder.headHinterService.models.entity.UserToken;

public interface HeadHunterDBRepository extends JpaRepository<UserToken, Long> {
}
