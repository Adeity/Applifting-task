package cz.applifting.task.dao;

import cz.applifting.task.model.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface UserDao extends JpaRepository<User, Integer> {
    Optional<User> findByUserAccessToken(String userAccessToken);
    Optional<User> findByUsername(String username);
}
