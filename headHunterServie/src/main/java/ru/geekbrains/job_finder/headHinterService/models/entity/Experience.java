package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.headHinterService.models.Enum.ExperienceEnum;
import ru.geekbrains.job_finder.routing_lib.dtos.HHUserSummary;

import javax.persistence.*;
@Data
@Entity
@NoArgsConstructor
@Table(name = "experience")
public class Experience {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @Column(name = "experience_id")
    private String id;

    @Column(name = "name")
    private String name;

    public Experience(ExperienceEnum experienceEnum) {
        this.id = experienceEnum.getId();
        this.name = experienceEnum.name();
    }

    @Data
    @Entity
    @NoArgsConstructor
    @AllArgsConstructor
    @Table(name = "users_tokens")
    public static class UserToken {
        @Id
        @Column(name = "id_user")
        private Long id ;
        @Column(name = "access_token")
        private String accessToken;
        @Column(name = "refresh_token")
        private String refreshToken;
    
        public UserToken(HHUserSummary hhUserProperties) {
            this.id = hhUserProperties.getId();
            this.accessToken = hhUserProperties.getAccessToken();
            this.refreshToken = hhUserProperties.getRefreshToken();
        }
    }
}
