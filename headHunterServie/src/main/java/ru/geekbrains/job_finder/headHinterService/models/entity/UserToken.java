package ru.geekbrains.job_finder.headHinterService.models.entity;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.routing_lib.dtos.HHUserSummary;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "users_tokens")
public class UserToken {
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
