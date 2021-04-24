package com.example.demo.common;

import org.mockserver.client.MockServerClient;
import org.mockserver.model.HttpRequest;
import org.mockserver.verify.VerificationTimes;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.test.context.ContextConfiguration;

@ContextConfiguration(initializers = { PostgreSQLContainerInitializer.class, MockServerContainerInitializer.class })
public abstract class AbstractExternalIntegrationTest extends AbstractIntegrationTest {
  @Autowired
  protected MockServerClient mockServerClient;

  protected void verifyMockServerHttpRequest(String method, String path, int times) {
    mockServerClient.verify(HttpRequest.request().withMethod(method).withPath(path), VerificationTimes.exactly(times));
  }
}
