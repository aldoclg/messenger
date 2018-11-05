package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.repository.UserRepository;
import com.daitangroup.messenger.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.bson.types.ObjectId;
import org.springframework.data.domain.Page;
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

    UserRepository userRepository;

    MongoTemplate mongoTemplate;

    PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findUserByNameOrLastName(String name, String lastName, Pageable pageable) {

        if (Strings.isNotBlank(name) && Strings.isNotBlank(lastName)) {

            return convertPageToList(userRepository.findByNameAndLastName(name, lastName, pageable));

        } else if (Strings.isNotBlank(name)) {

            return convertPageToList(userRepository.findByName(name, pageable));

        } else if (Strings.isNotBlank(lastName)) {

            return convertPageToList(userRepository.findByLastName(lastName, pageable));
        }
        return Collections.emptyList();
    }

    private List<User> convertPageToList(Page<User> page) {
        return page.stream().collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(String id, User user) {

        final Query query = createQuery(id);

        final Update update = createUpdate(user);

        mongoTemplate.findAndModify(query, update, User.class);
    }

    private Query createQuery(String id) {
        final Criteria criteria = new Criteria()
                .orOperator(Criteria.where("_id").is(new ObjectId(id)));
        return new Query(criteria);
    }

    private Update createUpdate(User user) {
        return MapperUpdate.newInstance()
                .withField("name", user.getName())
                .withField("lastName", user.getLastName())
                .withField("email", user.getEmail())
                .withField("role", user.getRole())
                .withField("password", user.getPassword() == null ? null : passwordEncoder.encode(user.getPassword()))
                .build();
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

        private MapperUpdate() {
        }

        public static final MapperUpdate newInstance() {
            return mappeObject;
        }

        /**
         * Adds field if it is not null
         *
         * @param key   the key
         * @param value the value
         * @return {@link MapperUpdate}
         */
        public MapperUpdate withField(String key, Object value) {
            if (Objects.nonNull(value)) {
                update.set(key, value);
            }
            return this;
        }

        public Update build() {
            return update;
        }
    }
}
