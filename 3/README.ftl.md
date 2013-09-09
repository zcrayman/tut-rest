Now that you have [written and tested your controllers](../2/), proudly added to your Life Preserver as shown below, it's time to bring the whole application together.

![Life Preserver Full showing Core Domain and REST Domain](../images/life-preserver-8.png)

# Step 3: Configuring and deploying your service

At this point you are ready to:

* Configure the core of your application
* Configure your REST components
* Initialize your RESTful Service's web infrastructure
* Run your RESTful service in a web container

To complete these tasks, you'll need a new domain, the Configuration domain.

![Life Preserver showing Configuration Domain](../images/life-preserver-9.png)

As with `core` and `rest`, the Configuration domain will have its own code package `com.yummynoodlebar.config`.

## Create a configuration for your application's Core domain using Spring JavaConfig

The Yummy Noodle Bar application contains a core set of components that include domain classes and services.

You could just create a configuration for these components; however, as in the previous step, you'll apply the Test Driven Development approach to your configuration.

### Test your Core configuration

First, create an empty placeholder class `com.yummynoodlebar.config.CoreConfig`.

Next, construct an integration test that contains the following:

    <@snippet path="src/test/java/com/yummynoodlebar/config/CoreDomainIntegrationTest.java" prefix="complete"/>

This integration test constructs an `ApplicationContext` using JavaConfig as specified on the `@ContextConfiguration` annotation. The Core domain's configuration will be created using Spring JavaConfig in a class called `CoreConfig`.

With the `ApplicationContext` constructed, the test can have its `OrderService` test entry point autowired, ready for the test methods.

Finally you have one test method that asserts that an `orderService` dependency has been provided and appears to work correctly.

Next, create the Core domain configuration.

### Implement your Core domain configuration

The Core domain configuration for the Yummy Noodle Bar application only contains one service and one dependency that needs to be configured for that service.

The following code shows the complete configuration class:

    <@snippet path="src/main/java/com/yummynoodlebar/config/CoreConfig.java" prefix="complete"/>

Spring JavaConfig will detect each `@Bean` annotated method as a method that generates configured Spring Beans.

Spring will create the `OrdersRepository` bean first, and then use that as the single parameter into the `createService` method to create the `OrderService` bean.

Running the `CoreDomainIntegrationTest` in the `com.yummynoodlebar.config` test package will verify that your Core Domain configuration is good to go.

## Create a configuration for your REST components

Configuring your new set of controllers is very straightforward as you have used `@Controller` on each of the controller classes. To initialize your RESTful domain's components, all you need to do is turn on component scanning so that Spring can find and initialize these Spring beans.

### Implement your RESTful domain configuration

You can create the following Spring JavaConfig to execute component scanning for the components in your application's RESTful domain:

    <@snippet path="src/main/java/com/yummynoodlebar/config/MVCConfig.java" prefix="complete"/>

The `@ComponentScan` attribute in JavaConfig specifies that your components should be found underneath the base Java package of `com.yummynoodlebar.rest.controllers`. 

> **Note:** It's always a good idea to be as specific as possible when defining the place where component scanning should occur so that you don't accidentally initialize components you didn't expect!

### Test your RESTful domain configuration

No configuration should be trusted without an accompanying test. The following test asserts that the output of the RESTful configuration is as it should be:

    <@snippet path="src/test/java/com/yummynoodlebar/config/RestDomainIntegrationTest.java" prefix="complete"/>

You've already asserted the correctness of the collaboration between your controllers and the underlying service components in the Core Domain. 

This test ensures that once everything is wired together, the wiring in the `MVCConfig` is correct and the appropriate controllers are in attendance.

The test validates the `MVCConfig` by mocking requests which exercise the handler mappings. The full responses are also confirmed to be correct. More testing could be done, but you've already asserted that your controllers should work appropriately in the previous steps. This test is simply there to show you that now you are configuring those components using Spring JavaConfig properly.

## Initialize your RESTful service web infrastructure

As of Spring 3.2, if you're using a web container that supports the Servlet 3 specification such as Tomcat 7+, it's possible to initialize the underlying web infrastructure for your application without writing a line of XML.

Here you're going to use the `WebApplicationInitializer` to set up your application's web application context parameters to bootstrap your application's web infrastructure as shown in the following code.

First you create a new piece of configuration as a class inside `com.yummynoodlebar.config` called `WebAppInitializer` that extends the `WebApplicationInitializer` from Spring as shown below.

    <@snippet "src/main/java/com/yummynoodlebar/config/WebAppInitializer.java" "top" "/complete"/>

The `LOG` attribute shows that you can even log messages as your web infrastructure is initialised, despite having no XML settings.

Next you override the `onStartup` method which in turn sets up your root Spring Application Context by calling `createRootContext` and then finally request the configuration of SpringMvc by calling `configureSpringMvc`.

    <@snippet "src/main/java/com/yummynoodlebar/config/WebAppInitializer.java" "onStartup" "/complete"/>

The root Spring Application Context will contain the majority of your components including your Core Domain. In the `createRootContext` method an instance of the `AnnotationConfigWebApplicationContext` class is constructed and then configured by calling `register` indicating the JavaConfig classes to be applied. In your case the root context can be initialised simply with the `CoreConfig` class.

    <@snippet "src/main/java/com/yummynoodlebar/config/WebAppInitializer.java" "createRootContext" "/complete"/>

Now with a root Application Context already to hand, in the `configureSpringMvc` method you can configure the REST Domain components in a new `AnnotationConfigWebApplicationContext` application context, connecting this new context to the root application context so that your REST Domain components can see and be dependency-injected with components from the root application context.

    <@snippet "src/main/java/com/yummynoodlebar/config/WebAppInitializer.java" "configureTop" "/complete"/>

Finally, using the `servletContext` you can dynamically initialise the Spring MVC `DispatcherServlet`, in this case mapping the `DispatcherServlet` to the root of the newly registered application.

    <@snippet "src/main/java/com/yummynoodlebar/config/WebAppInitializer.java" "configureBottom" "/complete"/>

The `DispatcherServlet` is a 'front controller' servlet that receives all incoming requests that should be considered for the various controllers registered. The DispatcherServlet then is the overall orchestrator of how each incoming request is channelled to the appropriate handler method on the available controllers.

The full `WebAppInitializer` source code is shown below:

    <@snippet path="src/main/java/com/yummynoodlebar/config/WebAppInitializer.java" prefix="complete"/>


## Running your RESTful service in a Web Container

It's the moment of truth: can you execute your new RESTful service? 

To find out, first tell Gradle that you will use Tomcat. Update your `build.gradle` file to look like this:

    <@snippet path="build.gradle" prefix="complete"/>

You may notice at the bottom of the build file a setting to ensure the app runs at the root context:

```groovy 
tomcatRunWar.contextPath = ''   
```

Now you can run the following from the command line to execute the new service, on port 8080 by default:

```sh
$ ./gradlew tomcatRunWar
```

Then, if you visit [http://localhost:8080/aggregators/orders](http://localhost:8080/aggregators/orders), you should get the following JSON response, which indicates that you don't yet have any Orders in the application:

```json
[]
```

If you need to set your web application to run on a different port or configure other settings, that information is available on the [Gradle Tomcat Plugin](https://github.com/bmuschko/gradle-tomcat-plugin/) project page.

If you plan to execute your service in another container and want to generate a WAR file instead, run the following command:

```sh
$ ./gradlew war
```

## Summary

You've come a long way! You've now got a fully configured RESTful web service that is running in Tomcat and can be packaged for distribution in a WAR file.

You've added two new components to your Configuration domain, `CoreConfig` and `MVCConfig` as shown in the updated life preserver below.

![Life Preserver showing Configuration Domain with Initial Components](../images/life-preserver-10.png)

Your full Life Preserver should now look like the following:

![Life Preserver showing Configuration Domain with Initial Components](../images/life-preserver-11.png)

But how do you really know that when you've deployed your service it really works? That's the job of functional testing, and that's your task in the next section of this tutorial.

[Next… Testing your Service using RestTemplate](../4/)

