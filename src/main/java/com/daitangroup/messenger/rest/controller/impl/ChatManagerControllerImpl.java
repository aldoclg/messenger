package com.daitangroup.messenger.rest.controller.impl;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.*;

import com.daitangroup.messenger.domain.ChatInfo;
import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.rest.component.UserResourceAssembler;
import com.daitangroup.messenger.rest.controller.ChatManagerController;
import com.daitangroup.messenger.service.ChatService;
import com.daitangroup.messenger.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.*;
import java.util.stream.Collectors;

public class ChatManagerControllerImpl implements ChatManagerController {

    private Logger LOGGER = LoggerFactory.getLogger(ChatManagerControllerImpl.class);

    private static final int PAGE = 20;

    private UserService userService;

    private UserResourceAssembler assembler;

    private ChatService chatService;

    public ChatManagerControllerImpl(UserService userService, ChatService chatService, UserResourceAssembler assembler) {
        LOGGER.info("Called constructor");
        this.userService = userService;
        this.chatService = chatService;
        this.assembler = assembler;
    }

    @Override
    public HttpEntity<User> saveUser(@RequestBody User user) {
        LOGGER.info("Called saveUser {}", user);
        if(Objects.isNull(user)|| Strings.isBlank(user.getEmail())  || Strings.isBlank(user.getPassword()) || Strings.isBlank(user.getName())) {
            LOGGER.debug("Returned {}", HttpStatus.BAD_REQUEST.value());
            return new ResponseEntity("User invalid.", HttpStatus.BAD_REQUEST);
        }
        if (userService.findUserByEmail(user.getEmail()).isPresent()) {
            LOGGER.debug("Returned {}", HttpStatus.CONFLICT.value());
            return new ResponseEntity("User already exists.", HttpStatus.CONFLICT);
        }
        userService.save(user);
        return new ResponseEntity(new Resource<>(user,
                linkTo(methodOn(ChatManagerController.class).findUserById(user.getId())).withSelfRel()), HttpStatus.CREATED);
    }

    @Override
    public HttpEntity<User> updateUser(@RequestBody User user,
                                       @PathVariable("id") String id) {
        LOGGER.info("Called updateUser {} {}", user, id);
        userService.update(id, user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public HttpEntity<User> deleteUser(@PathVariable("id") String id) {
        LOGGER.info("Called deleteUser {}", id);
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public HttpEntity<Resource<User>> findUserById(@PathVariable("id") String id) {
        LOGGER.info("Called findUserById {}", id);
        Optional<User> userOptional = userService.find(id);
        if (userOptional.isPresent())
        {
            LOGGER.debug("Returned {}", HttpStatus.OK.value());
            return new ResponseEntity<>(new Resource<>(userOptional.get(),
                    linkTo(methodOn(ChatManagerController.class).findUserById(id)).withSelfRel()), HttpStatus.OK);
        }
        LOGGER.error("Returned {}", HttpStatus.NOT_FOUND.value());
        return new ResponseEntity("User with id " + id + " not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public HttpEntity<Resources<Resource<User>>> findUserByName(@RequestParam(name = "name", required = false) String name,
                                                                @RequestParam(name = "lastName", required = false) String lastName,
                                                                @RequestHeader("Range") String range) {
        LOGGER.info("Called findUserByName {} {}", name, lastName);
        List<User> users  = Collections.emptyList();
        List<Resource<User>> userResources = Collections.emptyList();

        try {

            if (Strings.isBlank(name) && Strings.isBlank(lastName)) {
                LOGGER.debug("Returned {}", HttpStatus.ACCEPTED.value());
                return new ResponseEntity(new Resources<>(userResources), HttpStatus.ACCEPTED);
            }

            Pageable pageable = createPageRequest(validateRange(range));
            users = userService.findUserByNameOrLastName(name, lastName, pageable);
            userResources = users.stream()
                    .map(assembler::toResource)
                    .collect(Collectors.toList());

            if (users.isEmpty()) {
                LOGGER.debug("Returned {}", HttpStatus.ACCEPTED.value());
                return new ResponseEntity(new Resources<>(userResources), HttpStatus.ACCEPTED);
            }
            LOGGER.debug("Returned {}", HttpStatus.PARTIAL_CONTENT.value());
            return new ResponseEntity(new Resources<>(userResources), HttpStatus.PARTIAL_CONTENT);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            LOGGER.error("Returned {}", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value());
            LOGGER.error("An error has occurred", e);
            return new ResponseEntity("Invalid Range header.", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        }
    }

    @Override
    public HttpEntity<Resources<Resource<User>>> findAllUsers(@RequestHeader("Range") String range) {
        LOGGER.info("Called findAllUsers");
        try {
            Pageable pageable = createPageRequest(validateRange(range));

            List<Resource<User>> users = userService.findAll(pageable)
                    .stream()
                    .map(assembler::toResource)
                    .collect(Collectors.toList());
            LOGGER.debug("Returned {}", HttpStatus.PARTIAL_CONTENT.value());
            return new ResponseEntity<>(new Resources<>(users), HttpStatus.PARTIAL_CONTENT);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            LOGGER.error("Returned {}", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE.value());
            LOGGER.error("An error has occurred", e);
            return new ResponseEntity("Invalid Range header.", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        }
    }

    @Override
    public HttpEntity<Resources<Resource<MessageInfo>>> findMessage(@PathVariable("id") String chatId,
                                                                    @RequestHeader("Range") String range) {
        try {
            Pageable pageable = createPageRequest(validateRange(range));
            return null;
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            return new ResponseEntity("Invalid Range header.", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        }
    }

    @Override
    public HttpEntity<ChatInfo> saveChat(@RequestBody ChatInfo[] chats) {
        LOGGER.info("Called saveChat {}", chats);
        try {
            List chatList = Arrays.asList(chats);
            chatService.createChatOneToOne(chatList);
            LOGGER.debug("Returned {}", HttpStatus.CREATED.value());
            return new ResponseEntity(chats, HttpStatus.CREATED);
        }
        catch(Exception e) {
            LOGGER.error("Returned {}", HttpStatus.INTERNAL_SERVER_ERROR.value());
            LOGGER.error("An error has ocurred {}", e);
            return new ResponseEntity<>(HttpStatus.INTERNAL_SERVER_ERROR);
        }
    }

    private int[] validateRange(String range) {
        int[] rangeInt = parseRange(range);
        if (rangeInt.length == 0) {
            throw new IllegalArgumentException("Range not Satisfiable");
        }
        if (rangeInt[1] < rangeInt[0]) {
            throw new IndexOutOfBoundsException("Initial param is bigger than second param");
        }
        return rangeInt;
    }

    private Pageable createPageRequest(int[] range) {
        int page = range[0] / PAGE;
        int size = range[1] - range[0];
        return new PageRequest(page, size);
    }

    private int[] parseRange(String range) {
        LOGGER.info("Range {}", range);
        if (!Strings.isBlank(range)) {
            if (range.matches("(?i)\\w+=\\d+-\\d+")) {
                String subRange = range.replaceFirst("(?i)\\w+=", "");
                String[] units = subRange.split("-");
                int[] unitsInt = new int[2];
                unitsInt[0] = Integer.parseInt(units[0]);
                unitsInt[1] = Integer.parseInt(units[1]);
                return unitsInt;
            }
        }
        return new int[0];
    }
}
