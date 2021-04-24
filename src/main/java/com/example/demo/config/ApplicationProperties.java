package com.example.demo.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import lombok.Data;

@Component
@Data
public class ApplicationProperties {
  @Value("${github.api.base-url}")
  private String githubBaseUrl;
}
