package com.daitangroup.messenger.repository;

import com.daitangroup.messenger.domain.User;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface UserRepository extends MongoRepository<User, ObjectId> {

     void deleteById(ObjectId id);

     Optional<User> findByEmail(String email);

     Page<User> findByName(String name, Pageable pageable);

     Page<User> findByLastName(String lastName, Pageable pageable);

     Page<User> findByNameAndLastName(String name, String lastName, Pageable pageable);
}
