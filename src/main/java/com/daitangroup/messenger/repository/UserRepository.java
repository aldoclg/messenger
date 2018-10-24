package com.daitangroup.messenger.repository;

import com.daitangroup.messenger.domain.User;
import org.springframework.data.mongodb.repository.MongoRepository;

public interface UserRepository extends MongoRepository<User, Long> {
    public void deleteById(long id);
    public User findByEmail(String email);
}
