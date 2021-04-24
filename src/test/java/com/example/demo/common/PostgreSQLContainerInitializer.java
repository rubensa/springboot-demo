package com.example.demo.common;

import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.PostgreSQLContainer;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class PostgreSQLContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {

  private static PostgreSQLContainer<?> sqlContainer;

  static {
    sqlContainer = new PostgreSQLContainer<>("postgres:10.7").withDatabaseName("integration-tests-db")
        .withUsername("sa").withPassword("sa");
    sqlContainer.start();
  }

  public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
    log.info("spring.datasource.url=" + sqlContainer.getJdbcUrl());
    log.info("spring.datasource.username=" + sqlContainer.getUsername());
    log.info("spring.datasource.password=" + sqlContainer.getPassword());
    TestPropertyValues
        .of("spring.datasource.url=" + sqlContainer.getJdbcUrl(),
            "spring.datasource.username=" + sqlContainer.getUsername(),
            "spring.datasource.password=" + sqlContainer.getPassword())
        .applyTo(configurableApplicationContext.getEnvironment());
  }

}
