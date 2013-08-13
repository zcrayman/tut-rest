# Step 4: Testing your Service using RESTTemplate

Your RESTful service is now happily running in Tomcat and life is good, right? Everything is working fine, right? You're sure…

It's time to prove it.

Functional tests provide the last check-in-the-box that your service's functionality is all in place.

In order to show how simple it is to create functional tests you're going to be using Spring's `RESTTemplate`.

## Implementing a Functional Test using RESTTemplate

Open the `OrderTests` test class in the `com.yummynoodlebar.functional` package and you should see one method that begins according to the following code snippet:

	@Test
  	public void thatOrdersCanBeAddedAndQueried() {
    	HttpHeaders headers = new HttpHeaders();
    	headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    	RestTemplate template = new RestTemplate();

Here you're setting up an initial Spring `RESTTemplate`. You've created some initial HTTP Headers that set the content type to JSON and set the accept header also to JSON in anticipation of receiving JSON in the response.

Next you prepare an `HTTPEntity` to contain the request that you'll submit to your service:

	HttpEntity<String> requestEntity = new HttpEntity<String>(
        RestDataFixture.standardOrderJSON(),headers);

This `HTTPEntity` uses a test fixture class to generate some JSON data that will form the content of your POST request to your service.

Now it's time to exercise your running service:

	ResponseEntity<Order> entity = template.postForEntity(
        "http://localhost:8080/aggregators/order",
        requestEntity, Order.class);

[Next… Securing your Service with Spring Security](../5/)
