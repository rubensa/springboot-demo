package com.example.demo.model;

import com.fasterxml.jackson.annotation.JsonProperty;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Setter
@Getter
@NoArgsConstructor
@AllArgsConstructor
public class GithubUser {
  private Long id;
  private String login;
  private String url;
  private String name;
  @JsonProperty("public_repos")
  private int publicRepos;
  private int followers;
  private int following;
}
