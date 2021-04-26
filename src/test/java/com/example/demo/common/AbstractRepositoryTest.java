package com.example.demo.common;

import javax.persistence.EntityManager;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
public abstract class AbstractRepositoryTest {
  @Autowired
  protected EntityManager entityManager;
}
