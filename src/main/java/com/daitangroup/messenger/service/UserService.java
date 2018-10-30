package com.daitangroup.messenger.service;

import com.daitangroup.messenger.domain.User;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public interface UserService extends CrudService<User> {

    public User findUserByEmail(String email);

    public List<User> findUserByRegex(String name, String lastName);
}
