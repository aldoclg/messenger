package com.daitangroup.messenger.rest.service;
import java.util.List;

import com.daitangroup.messenger.domain.Chat;
import com.daitangroup.messenger.domain.Message;
import com.daitangroup.messenger.domain.User;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.annotation.Secured;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;


@RestController("/api/v1")
public interface ChatManagerService {

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    public void saveUser(User user);

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    public void updateUser(@PathVariable("id") long id, User user);

    @Secured("ROLE_ADMIN")
    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public void deleteUser(long id);

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public Resource<User> findUser(long id, String email);

    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Resources<Resource<User>> findAllUsers();
}
