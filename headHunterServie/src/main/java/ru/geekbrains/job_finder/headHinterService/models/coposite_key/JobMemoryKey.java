package ru.geekbrains.job_finder.headHinterService.models.coposite_key;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;
import javax.persistence.Embeddable;
import java.io.Serializable;

@Data
@Embeddable
@NoArgsConstructor
@AllArgsConstructor
public class JobMemoryKey implements Serializable {
    @Column(name = "id_summary")
    private String idSummary;
    @Column(name = "id_vacancy")
    private Long idVacancy;
}
