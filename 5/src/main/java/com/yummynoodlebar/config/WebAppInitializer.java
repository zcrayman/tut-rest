package com.yummynoodlebar.config;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.web.WebApplicationInitializer;
import org.springframework.web.context.ContextLoaderListener;
import org.springframework.web.context.support.AnnotationConfigWebApplicationContext;
import org.springframework.web.filter.DelegatingFilterProxy;
import org.springframework.web.servlet.DispatcherServlet;

import javax.servlet.FilterRegistration;
import javax.servlet.ServletContext;
import javax.servlet.ServletRegistration;
import java.util.Set;

public class WebAppInitializer implements WebApplicationInitializer {

  private static Logger LOG = LoggerFactory.getLogger(WebAppInitializer.class);

  @Override
  public void onStartup(ServletContext servletContext) {
    AnnotationConfigWebApplicationContext rootContext = new AnnotationConfigWebApplicationContext();
    rootContext.register(CoreConfig.class, SecurityConfig.class);
    rootContext.refresh();

    AnnotationConfigWebApplicationContext mvcContext = new AnnotationConfigWebApplicationContext();
    mvcContext.register(MVCConfig.class);

    mvcContext.setParent(rootContext);

    servletContext.addListener(new ContextLoaderListener(rootContext));
    servletContext.setInitParameter("defaultHtmlEscape", "true");

    // The main Spring MVC servlet.
    ServletRegistration.Dynamic appServlet = servletContext.addServlet(
        "webservice", new DispatcherServlet(mvcContext));
    appServlet.setLoadOnStartup(1);
    Set<String> mappingConflicts = appServlet.addMapping("/");

    if (!mappingConflicts.isEmpty()) {
      for (String s : mappingConflicts) {
        LOG.error("Mapping conflict: " + s);
      }
      throw new IllegalStateException(
          "'webservice' cannot be mapped to '/'");
    }

    FilterRegistration.Dynamic springSecurity =servletContext.addFilter("springSecurityFilterChain", new DelegatingFilterProxy("springSecurityFilterChain", rootContext));
    springSecurity.addMappingForUrlPatterns(null, true, "/*");
  }
}
