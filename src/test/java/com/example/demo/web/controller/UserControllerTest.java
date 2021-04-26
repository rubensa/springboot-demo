package com.example.demo.web.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

import com.example.demo.common.AbstractControllerTest;
import com.example.demo.entity.User;
import com.example.demo.service.UserService;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;
import org.zalando.problem.ProblemModule;
import org.zalando.problem.violations.ConstraintViolationProblemModule;

@WebMvcTest(UserController.class)
class UserControllerTest extends AbstractControllerTest {

  @MockBean
  private UserService userService;

  private List<User> userList;

  @BeforeEach
  void setUp() {
    this.userList = new ArrayList<>();
    this.userList.add(new User(1L, "user1@example.com", "pwd1", "User1"));
    this.userList.add(new User(2L, "user2@example.com", "pwd2", "User2"));
    this.userList.add(new User(3L, "user3@example.com", "pwd3", "User3"));

    objectMapper.registerModule(new ProblemModule());
    objectMapper.registerModule(new ConstraintViolationProblemModule());
  }

  @Test
  void shouldFetchAllUsers() throws Exception {
    BDDMockito.given(userService.findAllUsers()).willReturn(this.userList);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users")).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(userList.size())));
  }

  @Test
  void shouldFindUserById() throws Exception {
    Long userId = 1L;
    User user = new User(userId, "newuser1@example.com", "pwd", "Name");
    BDDMockito.given(userService.findUserById(userId)).willReturn(Optional.of(user));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(user.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(user.getPassword())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(user.getName())));
  }

  @Test
  void shouldReturn404WhenFetchingNonExistingUser() throws Exception {
    Long userId = 1L;
    BDDMockito.given(userService.findUserById(userId)).willReturn(Optional.empty());

    mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

  }

  @Test
  void shouldCreateNewUser() throws Exception {
    BDDMockito.given(userService.createUser(ArgumentMatchers.any(User.class)))
        .willAnswer((invocation) -> invocation.getArgument(0));

    User user = new User(null, "newuser1@example.com", "pwd", "Name");
    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/users").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(MockMvcResultMatchers.status().isCreated())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(user.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(user.getPassword())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(user.getName())));

  }

  @Test
  void shouldReturn400WhenCreateNewUserWithoutEmail() throws Exception {
    User user = new User(null, null, "pwd", "Name");

    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/users").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.header().string("Content-Type", CoreMatchers.is("application/problem+json")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.type",
            CoreMatchers.is("https://zalando.github.io/problem/constraint-violation")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is("Constraint Violation")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violations", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violations[0].field", CoreMatchers.is("email")))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.violations[0].message", CoreMatchers.is("Email should not be empty")))
        .andReturn();
  }

  @Test
  void shouldReturnSpanishErrorMessageLocalesIsSpanish() throws Exception {
    User user = new User(null, null, "pwd", "Name");
    Locale locale = new Locale.Builder().setLanguage("es").build();

    mockMvc
        .perform(MockMvcRequestBuilders.post("/api/users").contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)).header(HttpHeaders.ACCEPT_LANGUAGE, locale.toLanguageTag()))
        .andExpect(MockMvcResultMatchers.status().isBadRequest())
        .andExpect(MockMvcResultMatchers.header().string("Content-Type", CoreMatchers.is("application/problem+json")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.type",
            CoreMatchers.is("https://zalando.github.io/problem/constraint-violation")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is("Constraint Violation")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(400)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violations", Matchers.hasSize(1)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violations[0].field", CoreMatchers.is("email")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.violations[0].message",
            CoreMatchers.is("Email no debe estar vac\u00edo")))
        .andReturn();
  }

  @Test
  void shouldUpdateUser() throws Exception {
    Long userId = 1L;
    User user = new User(userId, "user1@example.com", "pwd", "Name");
    BDDMockito.given(userService.findUserById(userId)).willReturn(Optional.of(user));
    BDDMockito.given(userService.updateUser(ArgumentMatchers.any(User.class)))
        .willAnswer((invocation) -> invocation.getArgument(0));

    mockMvc
        .perform(MockMvcRequestBuilders.put("/api/users/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(user.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(user.getPassword())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(user.getName())));

  }

  @Test
  void shouldReturn404WhenUpdatingNonExistingUser() throws Exception {
    Long userId = 1L;
    BDDMockito.given(userService.findUserById(userId)).willReturn(Optional.empty());
    User user = new User(userId, "user1@example.com", "pwd", "Name");

    mockMvc.perform(MockMvcRequestBuilders.put("/api/users/{id}", userId).contentType(MediaType.APPLICATION_JSON)
        .content(objectMapper.writeValueAsString(user))).andExpect(MockMvcResultMatchers.status().isNotFound());

  }

  @Test
  void shouldDeleteUser() throws Exception {
    Long userId = 1L;
    User user = new User(userId, "user1@example.com", "pwd", "Name");
    BDDMockito.given(userService.findUserById(userId)).willReturn(Optional.of(user));
    Mockito.doNothing().when(userService).deleteUserById(user.getId());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", user.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(user.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(user.getPassword())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(user.getName())));

  }

  @Test
  void shouldReturn404WhenDeletingNonExistingUser() throws Exception {
    Long userId = 1L;
    BDDMockito.given(userService.findUserById(userId)).willReturn(Optional.empty());

    mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", userId))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

  }

}