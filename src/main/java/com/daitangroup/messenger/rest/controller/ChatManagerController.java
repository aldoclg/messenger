package com.daitangroup.messenger.rest.controller;

import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.domain.User;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import org.springframework.http.HttpEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public interface ChatManagerController {

    public HttpEntity<User> saveUser(User user);

    public HttpEntity<User> updateUser(User user, String id);

    public HttpEntity<User> deleteUser(String id);

    public HttpEntity<Resource<User>> findUserById(String id);

    public HttpEntity<Resources<Resource<User>>> findUserByName(String name, String lastName, String range);

    public HttpEntity<Resources<Resource<User>>> findAllUsers(String range);

    public HttpEntity<Resources<Resource<MessageInfo>>> findMessage(String chatId, String range);
}
