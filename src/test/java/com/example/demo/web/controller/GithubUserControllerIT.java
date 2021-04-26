package com.example.demo.web.controller;

import com.example.demo.common.AbstractExternalIntegrationTest;

import org.hamcrest.CoreMatchers;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockserver.model.Header;
import org.mockserver.model.HttpRequest;
import org.mockserver.model.HttpResponse;
import org.mockserver.model.JsonBody;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

public class GithubUserControllerIT extends AbstractExternalIntegrationTest {

  @BeforeEach
  void setup() {
    mockServerClient.reset();
  }

  @Test
  void shouldGetGithubUserProfile() throws Exception {
    String username = "user";
    mockGetUserFromGithub(username);
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/github/users/{username}", username))
        .andExpect(MockMvcResultMatchers.status().isOk())
        .andExpect(MockMvcResultMatchers.jsonPath("$.login", CoreMatchers.is(username)))
        .andExpect(MockMvcResultMatchers.jsonPath("$.name", CoreMatchers.is("GitHub User")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.public_repos", CoreMatchers.is(100)));
    verifyMockServerHttpRequest("GET", "/users/.*", 1);
  }

  @Test
  void shouldReturn404WhenFetchingNonExistingUserFromGithub() throws Exception {
    mockUserNotFoundFromGithub();
    this.mockMvc.perform(MockMvcRequestBuilders.get("/api/github/users/{username}", "dummy"))
        .andExpect(MockMvcResultMatchers.status().isNotFound())
        .andExpect(MockMvcResultMatchers.header().string("Content-Type", CoreMatchers.is("application/problem+json")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.type", CoreMatchers.is("urn:problem-type:not-found")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.title", CoreMatchers.is("Not found")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.status", CoreMatchers.is(404)))
        .andExpect(
            MockMvcResultMatchers.jsonPath("$.detail", CoreMatchers.is("Entity with identifier 'dummy' is not found")))
        .andExpect(MockMvcResultMatchers.jsonPath("$.id", CoreMatchers.is("dummy"))).andExpect(
            MockMvcResultMatchers.jsonPath("$.instance", CoreMatchers.is("http://localhost/api/github/users/dummy")));
    verifyMockServerHttpRequest("GET", "/users/.*", 1);
  }

  private void mockGetUserFromGithub(String username) {
    mockServerClient.when(HttpRequest.request().withMethod("GET").withPath("/.*"))
        .respond(HttpResponse.response().withStatusCode(200)
            .withHeaders(new Header("Content-Type", "application/json; charset=utf-8"))
            .withBody(JsonBody.json("{ " + "\"login\": \"" + username + "\", " + "\"name\": \"GitHub User\", "
                + "\"public_repos\": 100 " + "}")));
  }

  private void mockUserNotFoundFromGithub() {
    mockServerClient.when(HttpRequest.request().withMethod("GET").withPath("/users/.*"))
        .respond(HttpResponse.response().withStatusCode(404));
  }
}
