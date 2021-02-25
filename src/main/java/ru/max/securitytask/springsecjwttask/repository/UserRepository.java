package ru.max.securitytask.springsecjwttask.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import ru.max.securitytask.springsecjwttask.model.entity.UserEntity;

/**
 * Created by maxxii on 22.02.2021.
 */
@Repository
public interface UserRepository extends JpaRepository<UserEntity,Long> {

    UserEntity findByLogin(String login);
}
