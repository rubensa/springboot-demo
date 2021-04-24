package com.example.demo.service;

import com.example.demo.config.ApplicationProperties;
import com.example.demo.model.GithubUser;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import lombok.extern.slf4j.Slf4j;

@Service
@Slf4j
public class GithubUserService {
  private final ApplicationProperties properties;
  private final RestTemplate restTemplate;

  @Autowired
  public GithubUserService(ApplicationProperties properties, RestTemplate restTemplate) {
    this.properties = properties;
    this.restTemplate = restTemplate;
  }

  public GithubUser getGithubUserProfile(String username) {
    log.info("GithubBaseUrl:" + properties.getGithubBaseUrl());
    return this.restTemplate.getForObject(properties.getGithubBaseUrl() + "/users/" + username, GithubUser.class);
  }
}
