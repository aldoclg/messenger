package com.daitangroup.messenger.rest.component;

import com.daitangroup.messenger.domain.MessageInfo;
import com.daitangroup.messenger.rest.controller.ChatManagerController;
import org.springframework.hateoas.Resource;
import org.springframework.hateoas.ResourceAssembler;
import org.springframework.stereotype.Component;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;
import static org.springframework.hateoas.mvc.ControllerLinkBuilder.methodOn;

@Component
public class MessageResourceAssembler implements ResourceAssembler<MessageInfo, Resource<MessageInfo>> {
    @Override
    public Resource<MessageInfo> toResource(MessageInfo messageInfo) {
        return new Resource<>(messageInfo, linkTo(methodOn(ChatManagerController.class).findMessages(messageInfo.getChatId(), "binary=0-10")).withSelfRel(),
                linkTo(methodOn(ChatManagerController.class).findAllUsers(null)).withRel("messages"));
    }
}
