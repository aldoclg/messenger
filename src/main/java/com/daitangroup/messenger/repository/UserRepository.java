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

    public void deleteById(ObjectId id);

    public Optional<User> findByEmail(String email);

    public Page<User> findByName(String name, Pageable pageable);

    public Page<User> findByLastName(String lastName, Pageable pageable);

    public Page<User> findByNameAndLastName(String name, String lastName, Pageable pageable);
}
