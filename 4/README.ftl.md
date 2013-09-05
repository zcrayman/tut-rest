Your RESTful service is now [running in Tomcat](../3/) and life is good, right? Everything is working fine, right? 

It's time to prove it.

# Step 4: Testing your Service with Spring's RestTemplate

Full-stack functional tests provide the last check-in-the-box proving your service is fully integrated and functional.

There should be as few tests as possible to prove that the full stack works.  Ideally you would have just one: testing your '*happy path*', or the code path that ***must work*** for you to make money.

You'll use Spring's `RestTemplate` to create functional tests. Following the above rule, you test one code path, placing orders, which is the core part of the REST interface. If this doesn't work, Yummy Noodle Bar will go out of business. 

## Implement a functional test with RestTemplate

Create a new class `com.yummynoodlebar.rest.functional.OrderTests` and enter the following code:

    <@snippet path="src/test/java/com/yummynoodlebar/rest/functional/OrderTests.java" prefix="complete"/>

Stepping through this, the first piece for you to understand is this :

    <@snippet "src/test/java/com/yummynoodlebar/rest/functional/OrderTests.java" "first" "/complete"/>

Here you're setting up an initial Spring `RestTemplate`. You've created HTTP Headers that set the content type and accept header to JSON in anticipation of receiving JSON in the response.

Next you prepare an `HTTPEntity` to contain the request that you'll submit to your service:

    <@snippet "src/test/java/com/yummynoodlebar/rest/functional/OrderTests.java" "second" "/complete"/>

This `HttpEntity` uses a test fixture (`RestDataFixture`) to generate some JSON data that will form the content of your POST request to your service.

Now it's time to exercise your running service:

    <@snippet "src/test/java/com/yummynoodlebar/rest/functional/OrderTests.java" "third" "/complete"/>

You are executing an HTTP Request with a POST HTTP Method against the service you created in the [previous section](../3/). If you have any problems running the test, check that your service is still running in Tomcat and that the connection details are correct.

With your `ResponseEntity` to hand, you can now inspect the response to ensure that it is valid.

If you run the test suite by itself, it will fail:

```sh
$ cd 4/complete
$ ./gradlew test

com.yummynoodlebar.rest.functional.OrderTests > thatOrdersCanBeAddedAndQueried FAILED
    org.springframework.web.client.ResourceAccessException at OrderTests.java:32
        Caused by: java.net.SocketException at OrderTests.java:32
```

That's because you need to be running the web application first.

```sh
$ ./gradlew tomcatRunWar
```

In another shell, run the test suite again:

```sh
$ ./gradlew test
```

Now the tests should all pass.


## Summary

The Spring `RestTemplate` is a powerful means of functionally interacting with your RESTful services, regardless of your testing framework. Here you've implemented a minimal set of functional tests, looking specifically at ensuring that new Order resources can be created. Your REST service is moving closer to production use by all those high-profile aggregators.

There's just one hitch. How do you ensure that only legitimate aggregators can submit Orders to your system? It's time to secure your RESTful service.

[Next… Securing your Service with Spring Security](../5/)
