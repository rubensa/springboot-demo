package com.example.demo.repository;

import java.util.Optional;

import com.example.demo.entity.User;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

public interface UserRepository extends JpaRepository<User, Long> {
  @Query("select u from User u where u.email=?1 and u.password=?2")
  Optional<User> login(String email, String password);

  Optional<User> findByEmail(String email);
}
