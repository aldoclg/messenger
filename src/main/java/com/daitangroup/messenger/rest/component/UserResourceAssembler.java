package com.daitangroup.messenger.rest.component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.rest.controller.ChatManagerController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

@Component
public class UserResourceAssembler implements ResourceAssembler<User, Resource<User>> {

    @Override
    public Resource<User> toResource(User user) {
        return new Resource<>(user, linkTo(methodOn(ChatManagerController.class).findUserById(user.getId())).withSelfRel(),
                linkTo(methodOn(ChatManagerController.class).findAllUsers(null)).withRel("users"));
    }
}
