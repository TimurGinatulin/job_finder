package ru.geekbrains.job_finder.ms_user.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.ms_user.models.Enum.RoleEnums;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Table;

@Data
@Entity
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "roles_table")
public class Role {
    @Id
    @Column(name = "id")
    private Integer id;
    @Column(name = "title")
    private String title;

    public Role(RoleEnums role) {
        switch (role) {
            case ADMIN:
                this.id = 1;
                this.title = RoleEnums.ADMIN.name();
                break;
            default:
                this.id = 2;
                this.title = RoleEnums.USER.name();
                break;
        }
    }
}
