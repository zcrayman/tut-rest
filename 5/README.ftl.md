
You've set up functionality for the Yummy Noodle Bar RESTful service and your Life Preserver is looking great at this point:

![Life Preserver showing Configuration Domain with Initial Components](../images/life-preserver-11.png)

The problem is, at this point the Yummy Noodle Bar RESTful service is a little *too* functional; you only want the right clients and aggregators to be able to submit and manage orders through the service. 

# Step 5: Securing your Service with Spring Security

Once again, all changes here are constrained to the Configuration domain:

![Life Preserver showing Configuration Domain with Initial Components](../images/life-preserver-10.png)

## RESTful security basics

To limit access to the Yummy Noodle Bar RESTful service, you extend the initial RESTful design as follows:

* Use HTTP Basic for security credentials.
* Every HTTP Request must supply an Authorization Header with the client's credentials as those won't be stored between requests on the server.
* If a client is not authorized, the service should respond with an HTTP Response that contains a 403 HTTP Status Code.

Spring Security helps you perform these steps without you having to change so much as a single controller!

## Add Spring Security to your project

First you add Spring Security dependencies to the project, by adding the following entries to your `build.gradle` script:

    <@snippet "build.gradle" "repos" "/complete" />
    
    <@snippet "build.gradle" "security" "/complete" />

You add the Pivotal milestone repository so that you can use Spring Security 3.2.0.M2. This lets you use some dynamic configuration features of Spring Security, including setting up the web security through JavaConfig.

Now you need to secure your controllers. Until now you've been writing tests first before making any code changes, including configuration. So, instead of immediately adding your security configuration, you'll create a test so that you'll know when your security is being applied correctly.

## Test for security

You already have a functional test from [Step 4](../4/) that attempts to access your running RESTful service. As HTTP Basic authentication requires you to add some new headers, you'll first extract the creation of those headers for the HTTP request into a separate method called `getHeaders`:

    <@snippet "src/test/java/com/yummynoodlebar/rest/functional/OrderTests.java" "httpHeaders" "/complete" />

While containing the previous code's JSON-based headers, it adds an Authorization header driven by an input parameter. This way, it is easy to test different security parameters from different test methods:

Finally you can update the original test to use the new `getHeaders` helper method to add security headers to your outgoing HTTP request:

    <@snippet "src/test/java/com/yummynoodlebar/rest/functional/OrderTests.java" "newWay" "/complete" />

You can do this by creating a request that you know should fail if security is enabled, and check that it does. 

Add in the following test method where you will deliberately corrupt the password to something different to the previous `thatOrdersCanBeAddedAndQueried ` test method so that we can expect this request to fail:

    <@snippet "src/test/java/com/yummynoodlebar/rest/functional/OrderTests.java" "badUser" "/complete" />

In this test we're explicitly expecting an HTTP Response that contains an HTTP Status Code of 403 (Forbidden). However if you run this test against your service as it currently stands it will fail as the security credentials are still being ignored.

The full code for your functional test, including security, will now look like the following:

    <@snippet path="src/test/java/com/yummynoodlebar/rest/functional/OrderTests.java" prefix="complete" />

You have your failing test that justifies changing the code in your application. Now it's time to add security to your RESTful service.

## Secure your resources 

You can now add a new concern to your application: security configuration. Create a new Spring configuration in com.yummynoodlebar.config named `SecurityConfig` that contains the following:

    <@snippet path="src/main/java/com/yummynoodlebar/config/SecurityConfig.java" prefix="complete" />

This configuration enables security using the `@EnableWebSecurity` annotation, and extends the `WebSecurityConfigurerAdapter` so that you can perform more detailed configuration of the web security you're applying.

The `registerAuthentication` method is overridden from `WebSecurityConfigurerAdapter` in order to configure an in-memory database of users that contains a single user, 'letsnosh', with the USER role. 

RESTful services are usually consumed programmatically via clients, such as your test code. It doesn't make much sense to implement a usual challenge+login page system as there is no one to enter the information into the login page if it was actually presented. 

Instead here inside the `configure` overridden method from `WebSecurityConfigurerAdapter` you've configured URL level protection using the `http.authorizeUrls()` method. The `http.authorizeUrls()` method protects the /aggregators/** urls, ensuring that only users with the USER role can access them. This means that access to your RESTful URIs must include an HTTP BASIC Authorization Header, and that any request without this header will be responded to with a 403 (Forbidden) HTTP Status Code.

## Configure the Spring Security filter chain

Spring Security relies on a Servlet filter to apply your security configuration. A filter is used so that security is applied before the Spring MVC Dispatcher Servlet gets involved in processing incoming requests. The Spring Security filter is referred to as the Spring Security filter chain as it actually delegates to a chain of filters internally that each apply one aspect of the security responsibility.

You now need to configure this filter chain by updating the web application configuration you created earlier. In the previous section, you configured things in the `WebAppInitializer` class.

The first step is to simply add your new `SecurityConfig` JavaConfig class to the root context inside the `createRootContext()` method:

    <@snippet "src/main/java/com/yummynoodlebar/config/WebAppInitializer.java" "addToRootContext" "/complete" />

Now you can add the Spring Security Filter:

    <@snippet "src/main/java/com/yummynoodlebar/config/WebAppInitializer.java" "configureSpringSecurity" "/complete" />

This sets up a Spring `DelegatingFilterProxy` with the `rootContext` and is called from the `onStartup()` method.

    <@snippet "src/main/java/com/yummynoodlebar/config/WebAppInitializer.java" "onStartup" "/complete" />

The name `springSecurityFilterChain` for the filter chain is important as this means that the filter will pass all calls down to a Spring Bean named `springSecurityFilterChain` that it finds in the `rootContext`. You configured this bean using `@Configuration` in the Spring JavaConfig class `SecurityConfig`.

## Run your new secure RESTful service

If you run the test suite by itself, it will fail:

```sh
$ cd 5/complete
$ ./gradlew test

com.yummynoodlebar.rest.functional.OrderTests > thatOrdersCannotBeAddedAndQueriedWithBadUser FAILED
    org.springframework.web.client.ResourceAccessException at OrderTests.java:55
        Caused by: java.net.SocketException at OrderTests.java:55

com.yummynoodlebar.rest.functional.OrderTests > thatOrdersCanBeAddedAndQueried FAILED
    org.springframework.web.client.ResourceAccessException at OrderTests.java:28
        Caused by: java.net.SocketException at OrderTests.java:28
```

That's because you need to be running the web application first. Run your RESTful service again, but now in secure mode:

```sh
$ ./gradlew tomcatRunWar
```

Then run your amended tests in the `OrderTests` class again, in another shell. Everything should pass and so now you've added security to your RESTful service!

```sh
$ ./gradlew test
```

Now the tests should all pass.


## Summary

You've extended your Configuration domain to secure your RESTful service as shown in the updated life preserver:

![Life Preserver showing Configuration Domain with Security Components](../images/life-preserver-12.png)

Your full Life Preserver should look like the following:

![Life Preserver showing Configuration Domain with Security Components](../images/life-preserver-13.png)

Now that you have a secure RESTful service that allows you to access data in your chosen format, it's time to think about *discovery*.

How does a client go about discovering what resources you're making available from your RESTful service, and what can be done with those resources? Without having to communicate all of that information up front and set it in stone for all of eternity?

It's time for you to implement a technology known by the worst acronym in the history of the web: it's time to implement [HATEOAS][gs-rest-hateoas].

[Next… Make your Service Discoverable with Spring HATEOAS](../6/)


[gs-rest-hateoas]: /guides/gs/rest-hateoas