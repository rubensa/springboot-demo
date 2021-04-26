package com.example.demo.web.controller;

import java.net.URI;

import com.example.demo.common.AbstractControllerTest;
import com.example.demo.model.GithubUser;
import com.example.demo.problem.NotFoundProblem;
import com.example.demo.service.GithubUserService;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

@WebMvcTest(GithubUserController.class)
class GithubUserControllerTest extends AbstractControllerTest {

  @MockBean
  private GithubUserService githubUserService;

  @Test
  void shouldGetGithubUserProfile() throws Exception {
    String username = "user";
    GithubUser user = new GithubUser(1L, username, "url", "name", 1, 0, 0);
    BDDMockito.given(githubUserService.getGithubUserProfile(username)).willReturn(user);

    mockMvc.perform(MockMvcRequestBuilders.get("/api/github/users/{username}", username))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.login", CoreMatchers.is(user.getLogin())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is(user.getName())))
        .andExpect(MockMvcResultMatchers.jsonPath("$.public_repos", CoreMatchers.is(user.getPublicRepos())));
  }

  @Test
  void shouldReturn404WhenFetchingNonExistingUser() throws Exception {
    String username = "user";
    BDDMockito.given(githubUserService.getGithubUserProfile(username))
        .willThrow(new NotFoundProblem(username, new URI("http://localhost/api/github/users/" + username)));

    mockMvc.perform(MockMvcRequestBuilders.get("/api/github/users/{username}", username))
        .andExpect(MockMvcResultMatchers.status().isNotFound());

  }

}