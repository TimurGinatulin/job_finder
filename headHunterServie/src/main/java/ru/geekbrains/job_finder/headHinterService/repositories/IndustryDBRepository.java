package ru.geekbrains.job_finder.headHinterService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.geekbrains.job_finder.headHinterService.models.entity.Industry;

import java.util.List;

public interface IndustryDBRepository extends JpaRepository<Industry,Double> {
    @Query(
            value = "SELECT * FROM industry WHERE parent_id is null",
            nativeQuery = true)
    List<Industry> findMainInd();
    @Query(
            value = "SELECT * FROM industry WHERE parent_id = ?",
            nativeQuery = true)
    List<Industry> findByParent(Double id);
}
