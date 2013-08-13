# Step 4: Testing your Service using RESTTemplate

Your RESTful service is now happily running in Tomcat and life is good, right? Everything is working fine, right? You're sure…

It's time to prove it.

Full stack Functional tests provide the last check-in-the-box that your service's functionality is all in place and works fully integrated together.

The rule for these is that there should be as few as possible to prove that the full stack works.  Ideally you would have just one, testing your '*happy path*', or the code path that ***must work*** for you to make money.

In order to show how simple it is to create functional tests you're going to be using Spring's `RESTTemplate`.   Following the above rule, we have one code path tested, placing orders, which is the core part of the rest interface, if this doesn't work then the Yummy Noodle Bar will go out of business.

## Implementing a Functional Test using RestTemplate

Open the `OrderTests` test class in the `com.yummynoodlebar.functional` package and you should see one method that begins according to the following code snippet:

    @Test
    public void thatOrdersCanBeAddedAndQueried() {
        HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
        headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    	RestTemplate template = new RestTemplate();

Here you're setting up an initial Spring `RestTemplate`. You've created some initial HTTP Headers that set the content type to JSON and set the accept header also to JSON in anticipation of receiving JSON in the response.

Next you prepare an `HTTPEntity` to contain the request that you'll submit to your service:

	HttpEntity<String> requestEntity = new HttpEntity<String>(
        RestDataFixture.standardOrderJSON(),headers);

This `HTTPEntity` uses a test fixture class to generate some JSON data that will form the content of your POST request to your service.

Now it's time to exercise your running service:

	ResponseEntity<Order> entity = template.postForEntity(
        "http://localhost:8080/aggregators/order",
        requestEntity, Order.class);

You are executing an HTTP Request with a POST HTTP Method against your service that you set running in the previous section. If you have any problems running the test, check that your service is still running in Tomcat and that the connection details are correct.

With your `ResponseEntity` to hand, you can now inspect the response to ensure that it is valid. The complete functional test is provided in the following code:

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

## Summary

The Spring `RestTemplate` provides a powerful means of functionally interacting with your RESTful services, regardless of your choice of testing framework.

Here you've implemented a minimal set of functional tests, looking specifically at ensuring that new Order resources can be created. Your REST service is moving closer and closer to production and opening the doors to usage by all those high-profile aggregators.

There's just one hitch, how do you ensure that only legitimate aggregators can submit Orders to your system? It's time to secure your RESTful service.

[Next… Securing your Service with Spring Security](../5/)
