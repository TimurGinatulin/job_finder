package ru.geekbrains.job_finder.headHinterService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.geekbrains.job_finder.headHinterService.models.entity.Filter;
import java.util.List;

public interface FilterDBRepository extends JpaRepository<Filter, Long> {
    List<Filter> getAllByUserId(Long userId);
}
