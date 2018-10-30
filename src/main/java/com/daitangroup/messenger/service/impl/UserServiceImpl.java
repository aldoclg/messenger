package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.repository.UserRepository;
import com.daitangroup.messenger.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.stream.Collectors;

public class UserServiceImpl implements UserService {

    @Autowired
    UserRepository userRepository;

    @Autowired
    MongoTemplate mongoTemplate;

    @Autowired
    PasswordEncoder passwordEncoder;

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findUserByNameOrLastName(String name, String lastName, Pageable pageable) {
        if (Strings.isNotBlank(name) && Strings.isNotBlank(lastName)) {
            return userRepository.findByNameAndLastName(name, lastName, pageable)
                    .stream()
                    .collect(Collectors.toList());
        } else if (Strings.isNotBlank(name)) {
            return userRepository.findByName(name, pageable)
                    .stream()
                    .collect(Collectors.toList());
        } else if (Strings.isNotBlank(lastName)) {
            return userRepository.findByLastName(lastName, pageable)
                    .stream()
                    .collect(Collectors.toList());
        }
        return Collections.emptyList();
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(String id, User user) {
        final Criteria criteria = new Criteria().orOperator(Criteria.where("_id").is(new ObjectId(id)));

        final Update update = MapperUpdate.newInstance()
                .withField("name", user.getName())
                .withField("lastName", user.getLastName())
                .withField("email", user.getEmail())
                .withField("role", user.getRole())
                .withField("password", user.getPassword() == null ? null : passwordEncoder.encode(user.getPassword()))
                .build();

       mongoTemplate.findAndModify(new Query(criteria), update, User.class);
    }



    @Override
    public Optional<User> find(String id) {
        return userRepository.findById(new ObjectId(id));
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        return userRepository.findAll(pageable)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        userRepository.deleteById(new ObjectId(id));
    }

    static class MapperUpdate {

        private static final MapperUpdate mappeObject = new MapperUpdate();

        private Update update = new Update();

        private MapperUpdate() {}

        public static final MapperUpdate newInstance() {
            return mappeObject;
        }

        /**
         * Adds field if it is not null
         * @param key
         * @param value
         * @return
         */
        public MapperUpdate withField(String key, Object value) {
            if(Objects.nonNull(value)) {
                update.set(key, value);
            }
            return this;
        }

        public Update build() {
            return update;
        }
    }
}