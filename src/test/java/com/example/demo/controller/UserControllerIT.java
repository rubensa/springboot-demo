package com.example.demo.controller;

import java.util.ArrayList;
import java.util.List;

import com.example.demo.common.AbstractIntegrationTest;
import com.example.demo.entity.User;
import com.example.demo.repository.UserRepository;

import org.hamcrest.CoreMatchers;
import org.hamcrest.Matchers;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.BeforeEach;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class UserControllerIT extends AbstractIntegrationTest {
  @Autowired
  private UserRepository userRepository;

  private List<User> userList = null;

  @BeforeEach
  void setUp() {
    userRepository.deleteAll();

    userList = new ArrayList<>();
    this.userList.add(new User(1L, "user1@gmail.com", "pwd1", "User1"));
    this.userList.add(new User(2L, "user2@gmail.com", "pwd2", "User2"));
    this.userList.add(new User(3L, "user3@gmail.com", "pwd3", "User3"));

    userList = userRepository.saveAll(userList);
  }

  @Test
  void shouldFetchAllUsers() throws Exception {
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users")).andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.size()", CoreMatchers.is(userList.size())));
  }

  @Test
  void shouldFindUserById() throws Exception {
    User user = userList.get(0);
    Long userId = user.getId();

    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/users/{id}", userId))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(user.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(user.getPassword())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(user.getName())));
  }

  @Test
  void shouldCreateNewUser() throws Exception {
    User user = new User(null, "user@gmail.com", "pwd", "name");
    this.mockMvc
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

    this.mockMvc
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
  void shouldUpdateUser() throws Exception {
    User user = userList.get(0);
    user.setPassword("newpwd");
    user.setName("NewName");

    this.mockMvc
        .perform(MockMvcRequestBuilders.put("/api/users/{id}", user.getId()).contentType(MediaType.APPLICATION_JSON)
            .content(objectMapper.writeValueAsString(user)))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(user.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(user.getPassword())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(user.getName())));

  }

  @Test
  void shouldDeleteUser() throws Exception {
    User user = userList.get(0);

    this.mockMvc.perform(MockMvcRequestBuilders.delete("/api/users/{id}", user.getId()))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.email", CoreMatchers.is(user.getEmail())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.password", CoreMatchers.is(user.getPassword())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(user.getName())));

  }

}