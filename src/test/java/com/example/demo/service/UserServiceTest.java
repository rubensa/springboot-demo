package com.example.demo.service;

import java.util.Optional;

import com.example.demo.entity.User;
import com.example.demo.exception.UserRegistrationException;
import com.example.demo.repository.UserRepository;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.BDDMockito;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
public class UserServiceTest {
  @Mock
  private UserRepository userRepository;

  @InjectMocks
  private UserService userService;

  @Test
  void shouldSavedUserSuccessfully() {
    User user = new User(null, "user@demo.com", "user", "User");
    BDDMockito.given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.empty());
    BDDMockito.given(userRepository.save(user)).willAnswer(invocation -> invocation.getArgument(0));

    User savedUser = userService.createUser(user);

    org.assertj.core.api.Assertions.assertThat(savedUser).isNotNull();

    Mockito.verify(userRepository).save(ArgumentMatchers.any(User.class));
  }

  @Test
  void shouldThrowErrorWhenSaveUserWithExistingEmail() {
    User user = new User(1L, "user@demo.com", "user", "User");
    BDDMockito.given(userRepository.findByEmail(user.getEmail())).willReturn(Optional.of(user));

    Assertions.assertThrows(UserRegistrationException.class, () -> {
      userService.createUser(user);
    });

    Mockito.verify(userRepository, Mockito.never()).save(ArgumentMatchers.any(User.class));
  }
}
