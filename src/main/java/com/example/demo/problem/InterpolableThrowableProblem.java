package com.example.demo.problem;

import java.net.URI;
import java.util.Map;

import javax.annotation.Nullable;

import org.zalando.problem.AbstractThrowableProblem;
import org.zalando.problem.StatusType;
import org.zalando.problem.ThrowableProblem;

public abstract class InterpolableThrowableProblem extends AbstractThrowableProblem {

  protected InterpolableThrowableProblem() {
    this(null);
  }

  protected InterpolableThrowableProblem(@Nullable final URI type) {
    this(type, null);
  }

  protected InterpolableThrowableProblem(@Nullable final URI type, @Nullable final String title) {
    this(type, title, null);
  }

  protected InterpolableThrowableProblem(@Nullable final URI type, @Nullable final String title,
      @Nullable final StatusType status) {
    this(type, title, status, null);
  }

  protected InterpolableThrowableProblem(@Nullable final URI type, @Nullable final String title,
      @Nullable final StatusType status, @Nullable final String detail) {
    this(type, title, status, detail, null);
  }

  protected InterpolableThrowableProblem(@Nullable final URI type, @Nullable final String title,
      @Nullable final StatusType status, @Nullable final String detail, @Nullable final URI instance) {
    this(type, title, status, detail, instance, null);
  }

  protected InterpolableThrowableProblem(@Nullable final URI type, @Nullable final String title,
      @Nullable final StatusType status, @Nullable final String detail, @Nullable final URI instance,
      @Nullable final ThrowableProblem cause) {
    this(type, title, status, detail, instance, cause, null);
  }

  protected InterpolableThrowableProblem(@Nullable final URI type, @Nullable final String title,
      @Nullable final StatusType status, @Nullable final String detail, @Nullable final URI instance,
      @Nullable final ThrowableProblem cause, @Nullable final Map<String, Object> parameters) {
    super(type, ProblemMessageInterpolator.interpolateMessage(title, parameters), status,
        ProblemMessageInterpolator.interpolateMessage(detail, parameters), instance, cause, parameters);
  }
}
