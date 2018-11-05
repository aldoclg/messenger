package com.daitangroup.messenger.service.impl;

import static org.mockito.Mockito.*;
import static java.util.Objects.isNull;

import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.repository.UserRepository;
import com.daitangroup.messenger.rest.controller.impl.AbstractTest;
import com.daitangroup.messenger.service.UserService;
import org.bson.types.ObjectId;
import org.junit.Before;
import org.junit.Test;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.query.Query;
import org.springframework.data.mongodb.core.query.Update;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.*;

import static org.junit.Assert.*;

@SpringBootTest
public class UserServiceImplTest extends AbstractTest {

    @Autowired
    PasswordEncoder passwordEncoder;

    UserService userService;

    MongoTemplate mongoTemplateMock;

    UserRepository userRepositoryMock;

    User user;

    ObjectId objectId;

    Pageable pageable;

    @Before
    public void setup() {
        super.setUp();
        objectId = isNull(objectId) ? new ObjectId() : objectId;
        pageable = isNull(pageable) ?  new PageRequest(1,10) : pageable;
        mongoTemplateMock = isNull(mongoTemplateMock) ? mock(MongoTemplate.class) : mongoTemplateMock ;
        userRepositoryMock = isNull(userRepositoryMock) ? mock(UserRepository.class) : userRepositoryMock;
        userService = isNull(userService) ? new UserServiceImpl() : userService;
        user = isNull(user) ? new User("Jose", "Silva", "blabla", "jose@email.com", "USER") : user;
        ((UserServiceImpl) userService).userRepository = userRepositoryMock;
        ((UserServiceImpl) userService).mongoTemplate = mongoTemplateMock;
        ((UserServiceImpl) userService).passwordEncoder = passwordEncoder;
    }

    @Test
    public void findUserByEmail() {
        when(userRepositoryMock.findByEmail(anyString())).thenReturn(Optional.of(user));
        Optional<User> myOptionalUser = userService.findUserByEmail(anyString());
        assertTrue("Optional did not return user object", myOptionalUser.isPresent());
        assertEquals(myOptionalUser.get(), user);
    }

    @Test
    public void findUserByNameOrLastName() {
        List<User> expectedUsers = new ArrayList();
        expectedUsers.add(user);

        when(userRepositoryMock.findByName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<User>(expectedUsers));
        when(userRepositoryMock.findByLastName(anyString(), any(Pageable.class))).thenReturn(new PageImpl<User>(expectedUsers));
        when(userRepositoryMock.findByNameAndLastName(anyString(), anyString(), any(Pageable.class))).thenReturn(new PageImpl<User>(expectedUsers));

        List<User> actualUsers = userService.findUserByNameOrLastName("test", "test", pageable);
        checkList(expectedUsers, actualUsers);

        actualUsers = userService.findUserByNameOrLastName("test", null, pageable);
        checkList(expectedUsers, actualUsers);

        actualUsers = userService.findUserByNameOrLastName(null, "test", pageable);
        checkList(expectedUsers, actualUsers);

        actualUsers = userService.findUserByNameOrLastName(null, null, pageable);
        assertEquals("Found " + actualUsers.size(), 0, actualUsers.size());
    }

    @Test
    public void save() {
        userService.save(user);
        verify(userRepositoryMock, times(1)).save(any());
    }

    @Test
    public void update() {
        userService.update(objectId.toStringMongod(), user);
        verify(mongoTemplateMock, times(1)).findAndModify(any(Query.class), any(Update.class), any());
    }

    @Test
    public void find() {
        when(userRepositoryMock.findById(any())).thenReturn(Optional.of(user));
        Optional myOptionaluser = userService.find(objectId.toStringMongod());
        assertTrue(myOptionaluser.isPresent());
        assertEquals(myOptionaluser.get(), user);
    }

    @Test
    public void findAll() {
        List<User> expectedUsers = new ArrayList();
        expectedUsers.add(user);
        when(userRepositoryMock.findAll(any(Pageable.class))).thenReturn(new PageImpl<User>(expectedUsers));
        List<User> actualUsers = userService.findAll(pageable);
        checkList(expectedUsers, actualUsers);
    }

    private void checkList(List<User> expected, List<User> actual) {
        assertEquals("Found " + actual.size(), expected.size(), actual.size());
        assertEquals(expected.get(0), actual.get(0));
    }

    @Test
    public void delete() {
        userService.delete(objectId.toStringMongod());
        verify(userRepositoryMock).deleteById(any());
    }
}