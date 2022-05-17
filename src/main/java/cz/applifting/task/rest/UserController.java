package cz.applifting.task.rest;

import cz.applifting.task.model.User;
import cz.applifting.task.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.access.prepost.PostAuthorize;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping(value = "/user")
public class UserController {
    private final UserService service;

    @Autowired
    public UserController(UserService service) {
        this.service = service;
    }

    @GetMapping(value = "/{id}")
    @PostAuthorize("returnObject.userAccessToken == authentication.getCredentials()")
    public User getUser(@PathVariable Integer id) {
        return service.getUserById(id);
    }
}
