package ru.geekbrains.job_finder.cor_lib.interfacces;

import ru.geekbrains.job_finder.cor_lib.models.UserInfo;
public interface ITokenService {
    String generateToken(UserInfo user);

    UserInfo parseToken(String token);
}
