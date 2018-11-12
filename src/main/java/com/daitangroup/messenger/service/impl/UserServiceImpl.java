package com.daitangroup.messenger.service.impl;

import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.repository.UserRepository;
import com.daitangroup.messenger.service.UserService;
import org.apache.logging.log4j.util.Strings;
import org.bson.types.ObjectId;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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

    private Logger LOGGER = LoggerFactory.getLogger(UserServiceImpl.class);

    private UserRepository userRepository;

    private MongoTemplate mongoTemplate;

    private PasswordEncoder passwordEncoder;

    public UserServiceImpl(UserRepository userRepository, MongoTemplate mongoTemplate, PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.mongoTemplate = mongoTemplate;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public Optional<User> findUserByEmail(String email) {
        LOGGER.info("Called findUserByEmail method {}", email);
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> findUserByNameOrLastName(String name, String lastName, Pageable pageable) {
        LOGGER.info("Called findUserByNameOrLastName method {} {}", name, lastName);
        if (Strings.isNotBlank(name) && Strings.isNotBlank(lastName)) {
            LOGGER.debug("The 'name' and 'lastname' is not blank.");
            return convertPageToList(userRepository.findByNameAndLastName(name, lastName, pageable));

        } else if (Strings.isNotBlank(name)) {
            LOGGER.debug("The 'name' is not blank.");
            return convertPageToList(userRepository.findByName(name, pageable));

        } else if (Strings.isNotBlank(lastName)) {
            LOGGER.debug("The 'lastname' is not blank.");
            return convertPageToList(userRepository.findByLastName(lastName, pageable));
        }
        LOGGER.debug("Returned empty");
        return Collections.emptyList();
    }

    private List<User> convertPageToList(Page<User> page) {
        return page.stream().collect(Collectors.toList());
    }

    @Override
    public void save(User user) {
        LOGGER.info("Called save method");
        user.setPassword(passwordEncoder.encode(user.getPassword()));
        userRepository.save(user);
    }

    @Override
    public void update(String id, User user) {
        LOGGER.debug("Called update method");
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
        LOGGER.info("Called find method");
        return userRepository.findById(new ObjectId(id));
    }

    @Override
    public List<User> findAll(Pageable pageable) {
        LOGGER.info("Called findAll method");
        return userRepository.findAll(pageable)
                .stream()
                .collect(Collectors.toList());
    }

    @Override
    public void delete(String id) {
        LOGGER.info("Called delete method {}", id);
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
