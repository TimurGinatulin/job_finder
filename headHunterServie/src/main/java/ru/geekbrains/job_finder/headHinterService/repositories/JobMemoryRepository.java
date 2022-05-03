package ru.geekbrains.job_finder.headHinterService.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import ru.geekbrains.job_finder.headHinterService.models.coposite_key.JobMemoryKey;
import ru.geekbrains.job_finder.headHinterService.models.entity.JobMemory;

import java.util.List;


public interface JobMemoryRepository extends JpaRepository<JobMemory, JobMemoryKey> {
    @Query(
            value = "SELECT j.id_vacancy FROM  jober_memory j WHERE j.id_summary = ?",
            nativeQuery = true)
    List<Long> findIdVacancyListByIdSummary(String idSummary);
}
