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

    private static final int PAGE = 20;

    private UserService userService;

    private UserResourceAssembler assembler;

    private ChatService chatService;

    public ChatManagerControllerImpl(UserService userService, ChatService chatService, UserResourceAssembler assembler) {
        this.userService = userService;
        this.chatService = chatService;
        this.assembler = assembler;
    }

    @Override
    public HttpEntity<User> saveUser(@RequestBody User user) {
        if(Objects.isNull(user)|| Strings.isBlank(user.getEmail())  || Strings.isBlank(user.getPassword()) || Strings.isBlank(user.getName())) {
            return new ResponseEntity("User invalid.", HttpStatus.BAD_REQUEST);
        }
        if (userService.findUserByEmail(user.getEmail()).isPresent()) {
            return new ResponseEntity("User already exists.", HttpStatus.CONFLICT);
        }
        userService.save(user);
        return new ResponseEntity(new Resource<>(user,
                linkTo(methodOn(ChatManagerController.class).findUserById(user.getId())).withSelfRel()), HttpStatus.CREATED);
    }

    @Override
    public HttpEntity<User> updateUser(@RequestBody User user,
                                       @PathVariable("id") String id) {
        userService.update(id, user);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public HttpEntity<User> deleteUser(@PathVariable("id") String id) {
        userService.delete(id);
        return new ResponseEntity<>(HttpStatus.ACCEPTED);
    }

    @Override
    public HttpEntity<Resource<User>> findUserById(@PathVariable("id") String id) {
        Optional<User> userOptional = userService.find(id);
        if (userOptional.isPresent())
        {
            return new ResponseEntity<>(new Resource<>(userOptional.get(),
                    linkTo(methodOn(ChatManagerController.class).findUserById(id)).withSelfRel()), HttpStatus.OK);
        }
        return new ResponseEntity("User with id " + id + " not found.", HttpStatus.NOT_FOUND);
    }

    @Override
    public HttpEntity<Resources<Resource<User>>> findUserByName(@RequestParam(name = "name", required = false) String name,
                                                                @RequestParam(name = "lastName", required = false) String lastName,
                                                                @RequestHeader("Range") String range) {
        List<User> users  = Collections.emptyList();
        List<Resource<User>> userResources = Collections.emptyList();

        try {

            if (Strings.isBlank(name) && Strings.isBlank(lastName)) {
                return new ResponseEntity(new Resources<>(userResources), HttpStatus.ACCEPTED);
            }

            Pageable pageable = createPageRequest(validateRange(range));
            users = userService.findUserByNameOrLastName(name, lastName, pageable);
            userResources = users.stream()
                    .map(assembler::toResource)
                    .collect(Collectors.toList());

            if (users.isEmpty()) {
                return new ResponseEntity(new Resources<>(userResources), HttpStatus.ACCEPTED);
            }

            return new ResponseEntity(new Resources<>(userResources), HttpStatus.PARTIAL_CONTENT);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e) {
            return new ResponseEntity("Invalid Range header.", HttpStatus.REQUESTED_RANGE_NOT_SATISFIABLE);
        }
    }

    @Override
    public HttpEntity<Resources<Resource<User>>> findAllUsers(@RequestHeader("Range") String range) {
        try {
            Pageable pageable = createPageRequest(validateRange(range));

            List<Resource<User>> users = userService.findAll(pageable)
                    .stream()
                    .map(assembler::toResource)
                    .collect(Collectors.toList());
            return new ResponseEntity<>(new Resources<>(users), HttpStatus.PARTIAL_CONTENT);
        }
        catch (IllegalArgumentException | IndexOutOfBoundsException e) {
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
        try {
            List chatList = Arrays.asList(chats);
            chatService.createChatOneToOne(chatList);
            return new ResponseEntity(chats, HttpStatus.CREATED);
        }
        catch(Exception e) {
            e.printStackTrace();
            System.err.println(e);
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
