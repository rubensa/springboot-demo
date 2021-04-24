package com.example.demo.problem;

import java.net.URI;
import java.util.Collections;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.Status;

public class NotFoundProblem extends AbstractThrowableProblem {
  private static final long serialVersionUID = 7662154827584418806L;
  private static final URI TYPE = URI.create("urn:problem-type:not-found");

  public NotFoundProblem(final String id, final URI instance) {
    super(TYPE, "Not found", Status.NOT_FOUND, "Entity with identifier '" + id + "' is not found", instance, null,
        Collections.singletonMap("id", id));
  }
}
