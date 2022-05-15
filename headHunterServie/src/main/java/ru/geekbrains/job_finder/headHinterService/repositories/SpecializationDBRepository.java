package ru.geekbrains.job_finder.headHinterService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.geekbrains.job_finder.headHinterService.models.entity.Specialization;

import java.util.List;

public interface SpecializationDBRepository extends JpaRepository<Specialization, Double> {
    @Query(
            value = "SELECT * FROM specialization WHERE parent_id is null",
            nativeQuery = true)
    List<Specialization> findMainSpec();
    @Query(
            value = "SELECT * FROM specialization WHERE parent_id = ?",
            nativeQuery = true)
    List<Specialization> findByParent(Double id);
}
