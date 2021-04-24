package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.common.AbstractRepositoryTest;
import com.example.demo.entity.User;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;

public class UserRepositoryTest extends AbstractRepositoryTest {
  @Autowired
  private UserRepository userRepository;

  @Test
  void shouldReturnUserGivenValidCredentials() {
    User user = new User(null, "test@demo.com", "test", "Test");
    entityManager.persist(user);

    Optional<User> userOptional = userRepository.login("test@demo.com", "test");

    Assertions.assertThat(userOptional).isNotEmpty();
  }
}
