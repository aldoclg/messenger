package com.daitangroup.messenger.rest.impl;

import com.daitangroup.messenger.domain.Chat;
import com.daitangroup.messenger.domain.Message;
import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.repository.UserRepository;
import com.daitangroup.messenger.rest.service.ChatManagerService;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.List;

public class ChatManagerServiceImpl implements ChatManagerService {

    @Autowired
    UserRepository userRepository;

    @Override
    public void saveUser(User user) {
        userRepository.save(user);
    }

    @Override
    public void deleteUser(long id) {
        userRepository.deleteById(id);
    }

    @Override
    public User findUser(long id, String email) {
        return null;
    }

    @Override
    public List<User> findAllUsers() {
        return null;
    }

    @Override
    public List<Chat> findAllChats() {
        return null;
    }

    @Override
    public void saveMessage(Message message) {

    }

    @Override
    public List<Message> findMessageByChat(long chatId) {
        return null;
    }
}
