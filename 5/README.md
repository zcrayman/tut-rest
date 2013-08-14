# Step 5: Securing your Service with Spring Security

You've now got the Yummy Noodle Bar RESTful service all set in terms of functionality, the only problem is that it's perhaps a little *too* functional. 

You only want the right clients, the aggregators, to be able to submit and manage orders using the service, and so now you need to limit who can access the service to just those parties.

Once again, all of our changes here will be limited to the Configuration Domain on the Life Preserver whose current state is shown below:

![Life Preserver showing Configuration Domain with Initial Components](../images/life-preserver-initial-config-domain-focus.png)

## RESTful Security Basics

In order to limit access to the Yummy Noodle Bar RESTful service, you need to extend the initial RESTful design a little with the following rules:

* Use HTTP Basic for security credentials.
* Every HTTP Request must supply an Authorization Header with the client's credentials as those won't be stored between requests on the server.
* If a client is not authorized then the service should respond with an HTTP Response that contains a 403 HTTP Status Code http 403.

Spring Security will help you perform all of the above steps, without you having to change so much as a single controller!

## Adding Spring Security to your Project

The first step to using Spring Security is to add its dependencies to the project.

To do this you add the following entries to your `build.gradle` script:

    repositories {
        mavenCentral()
        maven { url 'http://repo.springsource.org/milestone/'}
    }
    
    dependencies {
        ...
        compile 'org.springframework.security:spring-security-web:3.2.0.M2'
        compile 'org.springframework.security:spring-security-core:3.2.0.M2'
        compile 'org.springframework.security:spring-security-config:3.2.0.M2'
        ...
    }    

First you've added the Pivotal milestone repository so that you can use Spring Security 3.2.0.M2. This gives you the capability of using some of the dynamic configuration features of Spring Security, including setting up the web security through JavaConfig.

Now you just need to secure your controllers by adding a little configuration… Hang On! Up until now we've been writing tests first before we need to make any code changes, including configuration! 

Instead of diving straight into adding your security configuration, let's instead create a test so that you'll know when your security is being applied correctly.

## Testing for Security

You already have a functional test from the previous section that attempts to access your running RESTful service as shown in the code below:

	package com.yummynoodlebar.rest.functional;

	import com.yummynoodlebar.rest.controller.fixture.RestDataFixture;
	import com.yummynoodlebar.rest.domain.Order;
	import org.junit.Test;
	import org.springframework.http.*;
	import org.springframework.web.client.RestTemplate;

	import java.util.Arrays;

	import static junit.framework.TestCase.assertEquals;
	import static junit.framework.TestCase.assertTrue;

	public class OrderTests {

  	@Test
  	public void thatOrdersCanBeAddedAndQueried() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    	headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    	RestTemplate template = new RestTemplate();

    	HttpEntity<String> requestEntity = new HttpEntity<String>(
        RestDataFixture.standardOrderJSON(),headers);

    	ResponseEntity<Order> entity = template.postForEntity(
        "http://localhost:8080/aggregators/order",
        requestEntity, Order.class);

    	String path = entity.getHeaders().getLocation().getPath();

    	assertEquals(HttpStatus.CREATED, entity.getStatusCode());
    	assertTrue(path.startsWith("/aggregators/order/"));
    	Order order = entity.getBody();

    	System.out.println ("The Order ID is " + order.getKey());
    	System.out.println ("The Location is " + entity.getHeaders().getLocation());

    	assertEquals(2, order.getItems().size());
  }
}

As HTTP Basic authentication requires you to add some new headers, and so creating those headers is going to become more involved, you'll first extract the creation of those headers for the HTTP request into a separate method called `getHeaders`:
    
    static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        
        return headers;
    }

Now you can add an authorization header and capture the value as an argument to the `getHeaders` method to allow you to pass in different security parameters from different test methods:

    static HttpHeaders getHeaders(String auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    
        byte[] encodedAuthorisation = Base64.encode(auth.getBytes());
        headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
    
        return headers;
    }

Finally you can update the original test to use the new `getHeaders` helper method to add security headers to your outgoing HTTP Request:

    @Test
    public void thatOrdersCanBeAddedAndQueried() {
        HttpEntity<String> requestEntity = new HttpEntity<String>(
            RestDataFixture.standardOrderJSON(),
            getHeaders("letsnosh" + ":" + "noshing"));
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<Order> entity = template.postForEntity(
        "http://localhost:8080/aggregators/order",
        requestEntity, Order.class);
        
        String path = entity.getHeaders().getLocation().getPath();
        
        assertEquals(HttpStatus.CREATED, entity.getStatusCode());
        assertTrue(path.startsWith("/aggregators/order/"));
        Order order = entity.getBody();
        
        System.out.println ("The Order ID is " + order.getKey());
        System.out.println ("The Location is " + entity.getHeaders().getLocation());
        
        assertEquals(2, order.getItems().size());
    }

At the moment this test will still pass as the Authorization header will simply be ignored by our application. But following our own rules, ideally you really need to make a *failing* test.

You can do this by creating a request that you know should fail if security is enabled, and check that it does. 

Add in the following test method where you will deliberately corrupt the password to something different to the previous `thatOrdersCanBeAddedAndQueried ` test method so that we can expect this request to fail:

    @Test
    public void thatOrdersCannotBeAddedAndQueriedWithBadUser() {
        HttpEntity<String> requestEntity = new HttpEntity<String>(
            RestDataFixture.standardOrderJSON(),
            getHeaders("letsnosh" + ":" + "BADPASSWORD"));
        
        RestTemplate template = new RestTemplate();
        ResponseEntity<Order> entity = template.postForEntity(
        "http://localhost:8080/aggregators/order",
        requestEntity, Order.class);
        
        assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
    }

In this test we're explicitly expecting an HTTP Response that contains an HTTP Status Code of 403 (Forbidden). However if you run this test against your service as it currently stands it will fail as the security credentials are still being ignored.

The full code for your functional test, including security, will now look like the following:

    package com.yummynoodlebar.rest.functional;
    
    
    import com.yummynoodlebar.rest.controller.fixture.RestDataFixture;
    import com.yummynoodlebar.rest.domain.Order;
    import org.junit.Test;
    import org.springframework.http.*;
    import org.springframework.security.crypto.codec.Base64;
    import org.springframework.web.client.RestTemplate;
    
    import java.util.Arrays;
    
    import static junit.framework.TestCase.assertEquals;
    import static junit.framework.TestCase.assertTrue;
    
    public class OrderTests {

        @Test
        public void thatOrdersCanBeAddedAndQueried() {
            
            HttpEntity<String> requestEntity = new HttpEntity<String>(
                RestDataFixture.standardOrderJSON(),
                getHeaders("letsnosh" + ":" + "noshing"));
            
            RestTemplate template = new RestTemplate();
            ResponseEntity<Order> entity = template.postForEntity(
            "http://localhost:8080/aggregators/order",
            requestEntity, Order.class);
            
            String path = entity.getHeaders().getLocation().getPath();
            
            assertEquals(HttpStatus.CREATED, entity.getStatusCode());
            assertTrue(path.startsWith("/aggregators/order/"));
            Order order = entity.getBody();
            
            System.out.println ("The Order ID is " + order.getKey());
            System.out.println ("The Location is " + entity.getHeaders().getLocation());
            
            assertEquals(2, order.getItems().size());
        }
        
        @Test
        public void thatOrdersCannotBeAddedAndQueriedWithBadUser() {
        
            HttpEntity<String> requestEntity = new HttpEntity<String>(
                RestDataFixture.standardOrderJSON(),
                getHeaders("letsnosh" + ":" + "BADPASSWORD"));
            
            RestTemplate template = new RestTemplate();
            ResponseEntity<Order> entity = template.postForEntity(
            "http://localhost:8080/aggregators/order",
            requestEntity, Order.class);
            
            assertEquals(HttpStatus.FORBIDDEN, entity.getStatusCode());
        }
        
        static HttpHeaders getHeaders(String auth) {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            
            byte[] encodedAuthorisation = Base64.encode(auth.getBytes());
            headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
            
            return headers;
        }
    }


You now have your failing test! You now have a justifying failing test to change the code in your application. Now it's time to dive into adding security to your RESTful service.

## Securing Your Resources 

As we have imported Spring Security 3.2, we can use Java Config to set it all up. Create a new Spring configuration in com.yummynoodlebar.config named `SecurityConfig`.

    package com.yummynoodlebar.config;
    
    import org.springframework.context.annotation.Configuration;
    import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
    import org.springframework.security.config.annotation.web.builders.HttpSecurity;
    import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
    import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
    
    @EnableWebSecurity
    @Configuration
    public class SecurityConfig extends WebSecurityConfigurerAdapter {
    
        @Override
        protected void registerAuthentication(AuthenticationManagerBuilder auth) throws Exception {
            auth.inMemoryAuthentication()
                .withUser("letsnosh").password("noshing").roles("USER");
        }
        
        @Override
        protected void configure(HttpSecurity http) throws Exception {
            http.authorizeUrls()
                .antMatchers("/aggregators/**").hasRole("USER")
                .anyRequest().anonymous()
                .and()
                .httpBasic();
        }
    
    }

 This config create an authentication with an in memory database of users, containing a single user, 'letsnosh' with the USER role.

It protects the /aggregators/** urls to ensure that only users with the USER role can access them.

It enables only HTTP BASIC authentication, and does not implement any challenge/ login system.

So, all accesses to the page must include an HTTP BASIC Authorization Header. Any request without this header will be responded to with a 403 Forbidden status.

Once this is up and running, your browser will be denied access by default.  


To actually use this in the system, we have to include this in our web application setup.  open up `WebAppInitializer` again.

Include `SecurityConfig` into the creation of the `rootContext`

    rootContext.register(CoreConfig.class, SecurityConfig.class);

Spring Seucurity operates as a Java Servlet Filter.  This means that it executes before any servlets, including the Spring MVC DispatcherServlet.

So, to include it, we need to create a new method to setup a Spring Security Filter.

    private void configureSpringSecurity(ServletContext servletContext, WebApplicationContext rootContext) {
        FilterRegistration.Dynamic springSecurity = 
                servletContext.addFilter("springSecurityFilterChain", 
                        new DelegatingFilterProxy("springSecurityFilterChain", rootContext));
        springSecurity.addMappingForUrlPatterns(null, true, "/*");
    }

This sets up a Spring `DelegatingFilterProxy` with the `rootContext`.  Given the name `springSecurityFilterChain`, this filter will pass all calls down to a Spring managed bean named `springSecurityFilterChain` that it finds in `rootContext`.
This bean is set up by the `@Configuration` Spring Config class `SecurityConfig`.

Try running up the application again, using `./gradlew tomcatRunWar` and run the tests in `OrderTests` again.  Assuming they pass, your application is now secured!

As mentioned above, your browser now won't be able to access the REST api, as it does not implement any kind of challenge/ response mechanism (an HTTP 401/ Authorization Required).

To be able to access the API using a browser, you must include the headers pre-emptively.  Extensions exist for common browser that allow you to do this, notably [Header Hacker for Chrome](https://chrome.google.com/webstore/detail/header-hacker/phnffahgegfkcobeaapbenpmdnkifigc?hl=en) and [Modify Headers for Firefox](http://www.garethhunt.com/modifyheaders/)

Now that we have a security REST service that allows us to access data in the format of our choosing, we're done, right?

Wrong.  The next level is to re-introduce a fundamental part of the web, linking between resources. This allows users and automated clients to step around your API and discover related resources, by using the oddly named technique of HATEOAS.

[Next… Make your Service Discoverable using Spring HATEOAS](../6/)
