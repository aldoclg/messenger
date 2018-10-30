package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.repository.UserRepository;
import com.daitangroup.messenger.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.bson.types.ObjectId;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;

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
    public User findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findUserByRegex(String name, String lastName) {
        return null;
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(String id, User user) {
        System.out.println(user);
        final Criteria criteria = new Criteria().orOperator(Criteria.where("_id").is(new ObjectId(id)));

        final Update update = MapperObject.newInstance()
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

    static class MapperObject {

        private static final MapperObject mappeObject = new MapperObject();

        private Update update = new Update();

        private MapperObject() {}

        public static final MapperObject newInstance() {
            return mappeObject;
        }

        /**
         * Adds field if it is not null
         * @param key
         * @param value
         * @return
         */
        public MapperObject withField(String key, Object value) {
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
