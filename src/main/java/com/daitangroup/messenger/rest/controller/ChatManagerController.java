package com.daitangroup.messenger.rest.controller;

import com.daitangroup.messenger.domain.ChatInfo;
import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.domain.User;

import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;

import org.springframework.http.HttpEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@PreAuthorize("hasRole('USER') OR hasRole('ADMIN')")
public interface ChatManagerController {

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    HttpEntity<User> saveUser(User user);

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    HttpEntity<User> updateUser(User user, String id);

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.DELETE)
    HttpEntity<User> deleteUser(String id);

    @RequestMapping(value = "/users/{id}", method = RequestMethod.GET)
    HttpEntity<Resource<User>> findUserById(String id);

    @RequestMapping(value = "/users", method = RequestMethod.POST)
    HttpEntity<Resources<Resource<User>>> findUserByName(String name, String lastName, String range);

    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    HttpEntity<Resources<Resource<User>>> findAllUsers(String range);

    @RequestMapping(value = "/chats/{chatId}/messages", method = RequestMethod.GET)
    HttpEntity<Resources<Resource<MessageInfo>>> findMessage(String chatId, String range);

    @RequestMapping(value = "/chats", method = RequestMethod.PUT)
    HttpEntity<ChatInfo> saveChat(ChatInfo[] chats);
}
