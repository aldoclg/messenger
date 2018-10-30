package com.daitangroup.messenger.rest.controller.impl;

import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.rest.component.UserResourceAssembler;
import com.daitangroup.messenger.rest.controller.ChatManagerController;
import com.daitangroup.messenger.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.Resources;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.stream.Collectors;

@PreAuthorize("hasRole('USER')")
public class ChatManagerControllerImpl implements ChatManagerController {

    private static final int PAGE = 20;

    @Autowired
    UserService userService;

    @Autowired
    private UserResourceAssembler assembler;

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.PUT)
    public void saveUser(@RequestBody User user) {
        userService.save(user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users/{id}", method = RequestMethod.POST)
    public void updateUser(@RequestBody User user,
                           @PathVariable("id") String id) {
        userService.update(id, user);
    }

    @Override
    @PreAuthorize("hasRole('ADMIN')")
    @RequestMapping(value = "/users", method = RequestMethod.DELETE)
    public void deleteUser(String id) {
        userService.delete(id);
    }

    @Override
    @RequestMapping(value = "/users", method = RequestMethod.POST)
    public Resources<Resource<User>> findUserByName(@RequestParam("name") String name,
                                                    @RequestParam("lastName") String lastName,
                                                    @RequestHeader("Range") String range) {
        return null;
    }

    @Override
    @RequestMapping(value = "/users", method = RequestMethod.GET)
    public Resources<Resource<User>> findAllUsers(@RequestHeader("Range") String range) {

        Pageable pageable = createPageRequest(validateRange(range));

        List<Resource<User>> users = userService.findAll(pageable)
                .stream()
                .map(assembler::toResource)
                .collect(Collectors.toList());
        return new Resources<>(users);
    }

    @Override
    @RequestMapping(value = "/chats/{chatId}/messages", method = RequestMethod.GET)
    public Resources<Resource<MessageInfo>> findMessage(@PathVariable("id") String chatId,
                                                        @RequestHeader("Range") String range) {
        return null;
    }

    private int[] validateRange(String range) {
        int[] rangeInt = parseRange(range);
        if (rangeInt.length == 0) {

        }
        if (rangeInt[1] < rangeInt[0]) {

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
