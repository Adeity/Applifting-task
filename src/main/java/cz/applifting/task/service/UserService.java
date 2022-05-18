package cz.applifting.task.service;

import cz.applifting.task.dao.UserDao;
import cz.applifting.task.exceptions.AuthenticationException;
import cz.applifting.task.exceptions.NotFoundException;
import cz.applifting.task.model.User;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Service;

import javax.validation.constraints.Null;
import java.util.Optional;
import java.util.concurrent.atomic.AtomicReference;

@Service
public class UserService {
    private final UserDao dao;

    @Autowired
    public UserService(UserDao dao) {
        this.dao = dao;
    }

    public User getUserById(Integer id) {
        Optional<User> userOpt = dao.findById(id);
        if (userOpt.isPresent()) {
            return userOpt.get();
        }
        throw NotFoundException.create("User", id);
    }

    public User getAuthenticatedUser() {
        String authToken;
        try {
            authToken = SecurityContextHolder.getContext().getAuthentication().getCredentials().toString();
        } catch (NullPointerException e) {
            throw AuthenticationException.create();
        }

        //  get user by
        Optional<User> ownerOpt = dao.findByUserAccessToken(authToken);
        if (ownerOpt.isEmpty()) {
            throw NotFoundException.create("User", authToken);
        }
        return ownerOpt.get();
    }
}
