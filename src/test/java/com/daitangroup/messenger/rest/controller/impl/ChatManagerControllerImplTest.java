package com.daitangroup.messenger.rest.controller.impl;

import static org.junit.Assert.*;
import static org.mockito.Mockito.*;
import static org.springframework.http.HttpStatus.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;

import com.daitangroup.messenger.domain.User;
import com.daitangroup.messenger.repository.UserRepository;
import com.daitangroup.messenger.rest.component.UserResourceAssembler;
import com.daitangroup.messenger.rest.controller.ChatManagerController;
import com.daitangroup.messenger.service.UserService;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.bson.types.ObjectId;

import org.junit.Before;
import org.junit.Test;

import org.mockito.Mock;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.boot.test.mock.mockito.SpyBean;
import org.springframework.data.mongodb.core.MongoTemplate;

import org.springframework.http.MediaType;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

@WebMvcTest({ChatManagerController.class})
public class ChatManagerControllerImplTest extends AbstractTest {

    static final String URI_BASE = "/api/v1/users";

    ChatManagerController chatManagerController;

    @MockBean
    UserService userService;

    @MockBean
    UserResourceAssembler userResourceAssembler;

    MongoTemplate mongoTemplate;

    @MockBean
    UserRepository userRepository;

    private User user;
    private List<User> users;

    @Before
    public void setUp() {
        users = new ArrayList();
        super.setUp();
        user = new User("Jose", "Silva", "blabla", "jose@email.com", "USER");
        User user2 = this.user = new User("João", "Silva", "blabla", "joao@email.com", "USER");
        users.add(user);
        users.add(user2);
        mongoTemplate = mock(MongoTemplate.class);
        chatManagerController = spy(ChatManagerController.class);
    }

    @Test
    public void shouldSaveUser() throws Exception {
        String uri = "/api/v1/users";

        user.setId(new ObjectId());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .content(mapToJson(user))
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(CREATED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldNotSaveExistingUser() throws Exception {
        String uri = "/api/v1/users";

        user.setId(new ObjectId());

        when(userService.findUserByEmail(anyString())).thenReturn(Optional.of(user));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .content(mapToJson(user)).contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(user.toString(), CONFLICT.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldNotSaveInvalidUser() throws Exception {
        String uri = "/api/v1/users";

        user.setId(new ObjectId());
        user.setEmail(null);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.put(uri)
                .content(mapToJson(user)).contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                .andDo(print())
                .andReturn();
        assertEquals(user.toString(), BAD_REQUEST.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldUpdateUser() throws Exception {

        String uri = "/api/v1/users/sflçjlfasçjas";

        user.setId(new ObjectId());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .content(mapToJson(user)).contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(ACCEPTED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldDeleteUser() throws Exception {
        String uri = "/api/v1/users/sflçjlfasçjas";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.delete(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(ACCEPTED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldFindUserById() throws Exception {
        String uri = "/api/v1/users/sflçjlfasçjas";

        when(userService.find(anyString())).thenReturn(Optional.of(user));

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(OK.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldNotFindUserById() throws Exception {
        String uri = "/api/v1/users/sflçjlfasçjas";

        when(userService.find(anyString())).thenReturn(Optional.empty());

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(NOT_FOUND.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldFindUserByName() throws Exception {
        String uri = "/api/v1/users";

        when(userService.findUserByNameOrLastName(anyString(), anyString(), any())).thenReturn(users);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .param("name","Jose")
                .content("{}")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Range", "binary=0-10")
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(ACCEPTED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldFindUserByLastName() throws Exception {
        String uri = "/api/v1/users";

        when(userService.findUserByNameOrLastName(anyString(), anyString(), any())).thenReturn(users);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .content("{}")
                .param("lastName", "Silva")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Range", "binary=0-10")
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(ACCEPTED.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldNotFindUserByNameWithInvalidRange() throws Exception {
        String uri = "/api/v1/users";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .content("{}")
                .param("name", "Jose")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Range", new Object[]{"binary=10-0"})
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(REQUESTED_RANGE_NOT_SATISFIABLE.value(), mvcResult.getResponse().getStatus());

        mvcResult = mvc.perform(MockMvcRequestBuilders.post(uri)
                .content("{}")
                .param("name", "Jose")
                .contentType(MediaType.APPLICATION_JSON)
                .header("Range", "range")
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(REQUESTED_RANGE_NOT_SATISFIABLE.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldFindAllUsers() throws Exception {
        String uri = "/api/v1/users";

        when(userService.findAll(any())).thenReturn(users);

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Range", "binary=0-10")
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(PARTIAL_CONTENT.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void shouldNotFindAllUsersWithInvalidRange() throws Exception {
        String uri = "/api/v1/users";

        MvcResult mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Range", "binary=10-0")
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(REQUESTED_RANGE_NOT_SATISFIABLE.value(), mvcResult.getResponse().getStatus());

        mvcResult = mvc.perform(MockMvcRequestBuilders.get(uri)
                .contentType(MediaType.APPLICATION_JSON)
                .header("Range", new Object[]{"range"})
                .accept("application/json"))
                .andDo(print())
                .andReturn();

        assertEquals(REQUESTED_RANGE_NOT_SATISFIABLE.value(), mvcResult.getResponse().getStatus());
    }

    @Test
    public void findMessage() {
    }
}