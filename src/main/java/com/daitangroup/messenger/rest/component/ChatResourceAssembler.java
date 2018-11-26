package com.daitangroup.messenger.rest.component;

import com.daitangroup.messenger.domain.ChatInfo;
import com.daitangroup.messenger.rest.controller.ChatManagerController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class ChatResourceAssembler implements ResourceAssembler<ChatInfo, Resource<ChatInfo>> {
    @Override
    public Resource<ChatInfo> toResource(ChatInfo chatInfo) {
        return new Resource<>(chatInfo, linkTo(methodOn(ChatManagerController.class).findUserById(chatInfo.getChatId())).withSelfRel(),
                linkTo(methodOn(ChatManagerController.class).findAllUsers(null)).withRel("chats"));
    }
}
