package ru.geekbrains.job_finder.cor_lib.models;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;

import java.util.List;
import java.util.StringJoiner;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserInfo {
    private Long id;
    private String userEmail;
    private List<String> role;

    public UserInfo(UserDto dto) {
        this.id = dto.getId();
        StringJoiner joiner = new StringJoiner(" ");
        joiner.add(dto.getLastName());
        joiner.add(dto.getFirstName());
        joiner.add(dto.getMiddleName());
        this.userEmail = joiner.toString();
        this.role = dto.getRoles();
    }

}
