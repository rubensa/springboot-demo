package com.example.demo.web.controller;

import com.example.demo.model.GithubUser;
import com.example.demo.service.GithubUserService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/github/users")
public class GithubUserController {
  private final GithubUserService githubUserService;

  @Autowired
  public GithubUserController(GithubUserService githubUserService) {
    this.githubUserService = githubUserService;
  }

  @GetMapping("/{username}")
  public GithubUser getGithubUserProfile(@PathVariable String username) {
    return githubUserService.getGithubUserProfile(username);
  }
}
