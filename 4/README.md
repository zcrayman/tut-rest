Your RESTful service is now running in Tomcat and life is good, right? Everything is working fine, right? 

It's time to prove it.

# Step 4: Testing your Service with Spring's REStTemplate

Full-stack functional tests provide the last check-in-the-box that your service is fully integrated and functional.

There should be as few tests as possible to prove that the full stack works.  Ideally you would have just one, testing your '*happy path*', or the code path that ***must work*** for you to make money.

You'll use Spring's `REStTemplate` to create functional tests. Following the above rule, you test one code path, placing orders, which is the core part of the REST interface. If this doesn't work, Yummy Noodle Bar will go out of business. 

## Implement a functional test with RestTemplate

Create a new class `com.yummynoodlebar.functional.OrderTests` and enter the following code:

```java
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
```

Stepping through this, the first piece for you to understand is this :-

```java
@Test
public void thatOrdersCanBeAddedAndQueried() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
    
    RestTemplate template = new RestTemplate();
```
    
Here you're setting up an initial Spring `RestTemplate`. You've created some initial HTTP Headers that set the content type to JSON and set the accept header also to JSON in anticipation of receiving JSON in the response.

Next you prepare an `HTTPEntity` to contain the request that you'll submit to your service:

```java
	HttpEntity<String> requestEntity = new HttpEntity<String>(
        RestDataFixture.standardOrderJSON(),headers);
```
This `HTTPEntity` uses a test fixture class to generate some JSON data that will form the content of your POST request to your service.

Now it's time to exercise your running service:
```java
	ResponseEntity<Order> entity = template.postForEntity(
        "http://localhost:8080/aggregators/order",
        requestEntity, Order.class);
```
You are executing an HTTP Request with a POST HTTP Method against your service that you set running in the previous section. If you have any problems running the test, check that your service is still running in Tomcat and that the connection details are correct.

With your `ResponseEntity` to hand, you can now inspect the response to ensure that it is valid.



## Summary

The Spring `RestTemplate` is a powerful means of functionally interacting with your RESTful services, regardless of your testing framework. Here you've implemented a minimal set of functional tests, looking specifically at ensuring that new Order resources can be created. Your REST service is moving closer to production use by all those high-profile aggregators.

There's just one hitch. How do you ensure that only legitimate aggregators can submit Orders to your system? It's time to secure your RESTful service.

[Next… Securing your Service with Spring Security](../5/)
