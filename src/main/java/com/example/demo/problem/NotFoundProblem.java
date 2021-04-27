package com.example.demo.problem;

import java.net.URI;
import java.util.Collections;

import org.zalando.problem.Status;

public class NotFoundProblem extends InterpolableThrowableProblem {
  private static final long serialVersionUID = 1L;
  private static final URI TYPE = URI.create("urn:problem-type:not-found");

  public NotFoundProblem(final String id, final URI instance) {
    super(TYPE, "{com.example.demo.problem.NotFoundProblem.message}", Status.NOT_FOUND,
        "{com.example.demo.problem.NotFoundProblem.detail.message}", instance, null,
        Collections.singletonMap("id", id));
  }
}
