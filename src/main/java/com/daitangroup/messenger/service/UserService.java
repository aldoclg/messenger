package com.daitangroup.messenger.service;

import com.daitangroup.messenger.domain.User;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public interface UserService extends CrudService<User> {

    Optional<User> findUserByEmail(String email);

    List<User> findUserByNameOrLastName(String name, String lastName, Pageable pageable);
}
