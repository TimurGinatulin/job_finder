package ru.geekbrains.job_finder.ms_user.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.geekbrains.job_finder.ms_user.models.entity.User;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {
}
