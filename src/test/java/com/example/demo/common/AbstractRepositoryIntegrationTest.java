package com.example.demo.common;

import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.test.context.ContextConfiguration;

@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
@ContextConfiguration(initializers = { PostgreSQLContainerInitializer.class })
public abstract class AbstractRepositoryIntegrationTest extends AbstractRepositoryTest {
}
