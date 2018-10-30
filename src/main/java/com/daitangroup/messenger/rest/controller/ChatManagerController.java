package com.daitangroup.messenger.rest.controller;

import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.domain.User;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public interface ChatManagerController {

    public void saveUser(User user);

    public void updateUser(User user, String id);

    public void deleteUser(String id);

    public Resource<User> findUser(long name, long lastName,String email);

    public Resources<Resource<User>> findAllUsers(String range);

    public Resources<Resource<MessageInfo>> findMessage(String chatId, String range);
}
