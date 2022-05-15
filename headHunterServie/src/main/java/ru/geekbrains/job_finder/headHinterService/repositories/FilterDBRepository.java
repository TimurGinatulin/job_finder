package ru.geekbrains.job_finder.headHinterService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.geekbrains.job_finder.headHinterService.models.entity.Filter;

import java.util.List;

public interface FilterDBRepository extends JpaRepository<Filter, Long> {
    @Query(
            value = "SELECT * FROM  filter WHERE user_id = ?",
            nativeQuery = true)
    List<Filter> findByIdUser(Long idUser);
}
