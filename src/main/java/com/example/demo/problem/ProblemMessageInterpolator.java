package com.example.demo.problem;

import java.util.Collections;
import java.util.Locale;
import java.util.Map;

import javax.annotation.Nullable;
import javax.validation.MessageInterpolator;
import javax.validation.Path;
import javax.validation.Validator;
import javax.validation.metadata.ConstraintDescriptor;

import com.example.demo.context.ApplicationContextProvider;

import org.hibernate.validator.internal.engine.MessageInterpolatorContext;
import org.hibernate.validator.messageinterpolation.ResourceBundleMessageInterpolator;
import org.springframework.beans.BeanWrapper;
import org.springframework.beans.PropertyAccessorFactory;
import org.springframework.context.ApplicationContext;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.context.support.ResourceBundleMessageSource;
import org.springframework.core.io.ClassPathResource;
import org.springframework.validation.beanvalidation.MessageSourceResourceBundleLocator;

public class ProblemMessageInterpolator {
  private static MessageInterpolator messageInterpolator;

  public static String interpolateMessage(@Nullable final String message,
      @Nullable Map<String, Object> messageParameters) {
    if (message == null) {
      return null;
    }
    MessageInterpolator messageInterpolator = getMessageInterpolator();
    if (messageInterpolator == null) {
      return message;
    }
    ConstraintDescriptor<?> constraintDescriptor = null;
    Object validatedValue = null;
    Class<?> rootBeanType = null;
    Path propertyPath = null;
    messageParameters = messageParameters != null ? messageParameters : Collections.emptyMap();
    Map<String, Object> expressionVariables = Collections.emptyMap();
    MessageInterpolatorContext messageInterpolatorContext = new MessageInterpolatorContext(constraintDescriptor,
        validatedValue, rootBeanType, propertyPath, messageParameters, expressionVariables);
    return messageInterpolator.interpolate(message, messageInterpolatorContext, getLocale());
  }

  protected static MessageInterpolator getMessageInterpolator() {
    // If already defined
    if (messageInterpolator != null) {
      return messageInterpolator;
    }
    // If not defined, create a new one from ProblemMessages.properties if exists in
    // the classpath
    messageInterpolator = buildInterpolatorFromProblemMessages();
    if (messageInterpolator != null) {
      return messageInterpolator;
    }
    // If not found, get interpolator from configured Validator
    messageInterpolator = getValidatorInterpolator();
    if (messageInterpolator != null) {
      return messageInterpolator;
    }
    // If not found, create a new one from configured MessageSource
    messageInterpolator = buildInterpolatorFromMessageSource();
    return messageInterpolator;
  }

  private static MessageInterpolator buildInterpolatorFromProblemMessages() {
    ClassPathResource problemMessagesResource = new ClassPathResource("ProblemMessages.properties");
    if (problemMessagesResource.exists()) {
      ResourceBundleMessageSource messageSource = new ResourceBundleMessageSource();
      messageSource.setBasenames("ProblemMessages");
      return new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
    }
    return null;
  }

  private static MessageInterpolator getValidatorInterpolator() {
    Validator validator = getValidator();
    BeanWrapper validatorWrapper = PropertyAccessorFactory.forBeanPropertyAccess(validator);
    if (validatorWrapper.isReadableProperty("messageInterpolator")) {
      return (MessageInterpolator) validatorWrapper.getPropertyValue("messageInterpolator");
    }
    return null;
  }

  private static Validator getValidator() {
    ApplicationContext applicationContext = getApplicationContext();
    if (applicationContext == null) {
      return null;
    }
    return (Validator) applicationContext.getBean(Validator.class);
  }

  private static MessageInterpolator buildInterpolatorFromMessageSource() {
    MessageSource messageSource = getMessageSource();
    if (messageSource == null) {
      return null;
    }
    return new ResourceBundleMessageInterpolator(new MessageSourceResourceBundleLocator(messageSource));
  }

  private static MessageSource getMessageSource() {
    ApplicationContext applicationContext = getApplicationContext();
    if (applicationContext == null) {
      return null;
    }
    return (MessageSource) applicationContext.getBean("messageSource");
  }

  private static ApplicationContext getApplicationContext() {
    return ApplicationContextProvider.getApplicationContext();
  }

  private static Locale getLocale() {
    return LocaleContextHolder.getLocale();
  }

}
