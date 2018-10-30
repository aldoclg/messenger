package com.daitangroup.messenger.rest.component;

import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.rest.controller.ChatManagerController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

    @Override
    public Resource<User> toResource(User user) {
        return new Resource<>(user);
    }
}
