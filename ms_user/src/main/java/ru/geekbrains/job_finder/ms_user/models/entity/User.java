package ru.geekbrains.job_finder.ms_user.models.entity;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.ms_user.models.Enum.RoleEnums;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;

import javax.persistence.Entity;
import javax.persistence.*;
import java.util.LinkedList;
import java.util.List;

@Data
@Builder
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Table(name = "user_table")
public class User {
    @Id
    @Column(name = "id")
    private Long id;
    @Column(name = "first_name")
    private String firstName;
    @Column(name = "middle_name")
    private String middleName;
    @Column(name = "last_name")
    private String lastName;
    @Column(name = "email")
    private String email;
    @ManyToMany
    @JoinTable(
            name = "user_roles",
            joinColumns = @JoinColumn(name = "id_user"),
            inverseJoinColumns = @JoinColumn(name = "id_role")
    )
    private List<Role> roles;

    public User(UserDto userDto) {
        this.id = userDto.getId();
        this.firstName = userDto.getFirstName();
        this.middleName = userDto.getMiddleName();
        this.lastName = userDto.getLastName();
        this.roles = new LinkedList<>();
        this.email = userDto.getEmail();
        roles.add(new Role(RoleEnums.ADMIN));
    }
}
