package com.daitangroup.messenger.repository;

import com.daitangroup.messenger.domain.User;
import org.bson.types.ObjectId;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.stereotype.Repository;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {
    public void deleteById(ObjectId id);
    public User findByEmail(String email);
    default public void updateById(ObjectId id, User user) {

    }
}
