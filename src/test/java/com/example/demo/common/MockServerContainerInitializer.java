package com.example.demo.common;

import org.mockserver.client.MockServerClient;
import org.springframework.boot.test.util.TestPropertyValues;
import org.springframework.context.ApplicationContextInitializer;
import org.springframework.context.ConfigurableApplicationContext;
import org.testcontainers.containers.MockServerContainer;
import org.testcontainers.utility.DockerImageName;

import lombok.extern.slf4j.Slf4j;

@Slf4j
public class MockServerContainerInitializer implements ApplicationContextInitializer<ConfigurableApplicationContext> {
  private static MockServerContainer mockServerContainer;

  static {
    mockServerContainer = new MockServerContainer(DockerImageName.parse("jamesdbloom/mockserver"));
    mockServerContainer.start();
  }

  protected MockServerClient mockServerClient = new MockServerClient(mockServerContainer.getContainerIpAddress(),
      mockServerContainer.getServerPort());

  @Override
  public void initialize(ConfigurableApplicationContext configurableApplicationContext) {
    log.info("github.api.base-url=" + mockServerContainer.getEndpoint());
    TestPropertyValues.of("github.api.base-url=" + mockServerContainer.getEndpoint())
        .applyTo(configurableApplicationContext.getEnvironment());
    configurableApplicationContext.getBeanFactory().registerSingleton("mockServiceClient", mockServerClient);
  }

}
