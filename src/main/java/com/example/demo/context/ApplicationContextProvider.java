package com.example.demo.context;

import org.springframework.beans.BeansException;
import org.springframework.context.ApplicationContext;
import org.springframework.context.ApplicationContextAware;
import org.springframework.stereotype.Component;

/**
 * Wrapper to always return a reference to the Spring Application Context from
 * within non-Spring enabled beans. Unlike Spring MVC's
 * WebApplicationContextUtils we do not need a reference to the Servlet context
 * for this. All we need is for this bean to be initialized during application
 * startup.
 */
@Component
public class ApplicationContextProvider implements ApplicationContextAware {

  /** The context. */
  private static ApplicationContext context;

  /*
   * This method is called from within the ApplicationContext once it is done
   * starting up, it will stick a reference to itself into this bean.
   *
   * @param ac a reference to the ApplicationContext.
   */
  @Override
  public void setApplicationContext(ApplicationContext ac) throws BeansException {
    ApplicationContextProvider.context = ac;
  }

  /**
   * Gets the application context.
   *
   * @return the application context
   */
  public static ApplicationContext getApplicationContext() {
    return context;
  }

  /**
   * This is about the same as context.getBean("beanName"), except it has its own
   * static handle to the Spring context, so calling this method statically will
   * give access to the beans by name in the Spring application context. As in the
   * context.getBean("beanName") call, the caller must cast to the appropriate
   * target class. If the bean does not exist, then a Runtime error will be
   * thrown.
   *
   * @param beanName the name of the bean to get.
   * @return an Object reference to the named bean.
   */
  public static Object getBean(String beanName) {
    return context.getBean(beanName);
  }
}
