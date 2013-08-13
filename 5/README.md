# Step 5: Securing your Service with Spring Security

... some spiel on HTTP Security. ...

* using http basic.
* every request must pre-emptively supply an Authorization Header.
* if not authorized, supply http 403.


Add spring security to the build.gradle.  This includes adding the spring milestone repository.

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


Following our method of creating a test first. We will update the functional test we created in the last section to check the use of HTTP BASIC security

First extract the header creation into a seperate method.
    
    static HttpHeaders getHeaders() {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
        
        return headers;
    }

Add an authorization header, and supply the value as an argument, to allow us to pass in parameters from different tests

    static HttpHeaders getHeaders(String auth) {
        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    
        byte[] encodedAuthorisation = Base64.encode(auth.getBytes());
        headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
    
        return headers;
    }

Update the original test to use this new helper method.

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

This test will still pass, the Authorization header will simply be ignored by our application as it stands.  We need to make a *failing* test.

We do this by creating a request that we know should fail if security is enabled, and check that it does.  Add in the following, noting that we have corrupted the password to something different to the above, one that we expect to fail.

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

This test will fail unless Spring Security is enabled and restricting access.

The full test class is now 

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


Start the system using `./gradlew tomcatRunwar` and then run the tests.  You'll notice that one passes and one fails, as we expect.

Now that we have a failing test, we can set up Spring Security.

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
