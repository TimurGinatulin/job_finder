package ru.geekbrains.job_finder.ms_user.services;

import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;
import ru.geekbrains.job_finder.cor_lib.interfacces.ITokenService;
import ru.geekbrains.job_finder.cor_lib.models.UserInfo;
import ru.geekbrains.job_finder.ms_user.models.Enum.RoleEnums;
import ru.geekbrains.job_finder.ms_user.models.entity.User;
import ru.geekbrains.job_finder.ms_user.repositories.UserRepository;
import ru.geekbrains.job_finder.routing_lib.dtos.UserDto;
import ru.geekbrains.job_finder.routing_lib.feigns.HeadHunterFeignClient;

import java.util.LinkedList;
import java.util.List;

@Service
@AllArgsConstructor
public class UserService {
    private UserRepository userRepository;
    private HeadHunterFeignClient headHunterFeignClient;
    private ITokenService iTokenService;

    public void addUser(UserDto userDto) {
        userRepository.save(new User(userDto));
    }

    public UserDto login(String code) {
        UserDto userByCode = headHunterFeignClient.getUserByCode(code);
        if (userByCode.getIsNew())
            userByCode.setRoles(List.of(RoleEnums.USER.name()));
        else {
            User userFromDB = userRepository.findById(userByCode.getId()).orElse(null);
            userByCode.setRoles(new LinkedList<>());
            assert userFromDB != null;
            userFromDB.getRoles().forEach(role -> userByCode.getRoles().add(role.getTitle()));
        }
        userByCode.setApsToken(iTokenService.generateToken(new UserInfo(userByCode)));
        return userByCode;
    }
}
