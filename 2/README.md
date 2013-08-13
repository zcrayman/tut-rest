# Step 2: Building Your First RESTful Service using Spring MVC

It's time to implement your Yummy Noodle Bar RESTful service. The first step in building a service with Spring MVC is to construct and test one or more Controllers that will be responsible for handling each of the incoming HTTP Requests that you defined for your service in the previous step.

## Starting with a (failing) test

[Test Driven Development (TDD)]() teaches us that if you haven't got a failing test then there's no code to write! So before we dive into implementing our service, let's create a couple of tests that justifies and encourages us to write some code to make the test pass.

### Splitting Commands from Queries

Before you start creating tests, you need to think a little more about the categories of requests that our service is going to have to respond to. You are going to be writing tests that look for all the HTTP Restful interactions we designed in the previous section.

These interactions can be split into X categories:

* Requests that read, or query, the current state of an Order
* Requests that change the state of a new or existing Order
* Requests that read an Order's status
* Requests that query Payment Details
* Requests that change the state of an Order's Payment Details

This can for our purposes be simplified into two further categories of interactions:

* Requests that change a resource's state (a Command)
* Requests that query a resource's state (a Query)

It's possible to implement these two categories of interactions using one controller for each resource but the [Command Query Responsibility Segregation (CQRS)](http://martinfowler.com/bliki/CQRS.html) pattern advises us to split these responsibilities into different routes through our application, and so in this tutorial we'll implement these concerns separately.

### Implementing failing test(s) for a Controller with MockMVC

We won't implement all of the tests needed for your RESTful service here, the full source is available for download separately. Instead we'll look at two unit tests that look for an example of each of the categories of interaction through the RESTful service, commands and queries.

The first test is going to target ensuring that a request to view an order's details is possible, so let's call the class ViewOrderIntegrationTest.

	public class ViewOrderIntegrationTest {

Why an 'integration' test? Because we're going to be testing the controller within the constraints of a mock Spring MVC environment. This way we can test the mappings of our incoming requests to the appropriate handler methods while still getting all the speed benefits of testing out of a real container.

The next step will be to add an instance of MockMvc to our test class and to set up a mock controller and OrderService.

public class ViewOrderIntegrationTest {

  	MockMvc mockMvc;

  	@InjectMocks
  	OrderQueriesController controller;

  	@Mock
  	OrderService orderService;

  	UUID key = UUID.fromString("f3512d26-72f6-4290-9265-63ad69eccc13");

  	@Before
  	public void setup() {
    	MockitoAnnotations.initMocks(this);

    	this.mockMvc = standaloneSetup(controller)
            .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
  	}

In the @Before annotated setup() method, we're setting up Mockito as well as generating a mock Spring MVC environment, including adding JSON message conversion as we'll be expecting JSON back when we ask for the current state of an Order.

Finally we can implement a test method that performs an HTTP Request on our controller and asserts that the response from that invocation contains the JSON that was requested.

	@Test
  	public void thatViewOrderRendersCorrectly() throws Exception {

	  when(orderService.requestOrderDetails(any(RequestOrderDetailsEvent.class))).thenReturn(
            orderDetailsEvent(key));

    	this.mockMvc.perform(
            get("/aggregators/orders/{id}", key.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.items['" + YUMMY_ITEM + "']").value(12))
            .andExpect(jsonPath("$.key").value(key.toString()));
  	}

It's worth at this point looking at the final call in the above method, the usage of MockMVC, in a little more detail.

In order, the mockMvc object is performing the following:

* Performing a mock HTTP Request with a GET HTTP Method on the URI /aggregators/orders/{id}.
* Specifying in the 'accept' HTTP Header that the service should respond with JSON.
* Analysing the content of the returned JSON to ensure that some mocked data is present, as provided by the mock collaborators that were set up at the start of the test method.

The Spring MockMVC component makes it possible to do this testing where you can be sure that for a given URI a given rendered content in the response will be returned, all executed in a unit test from within your IDE or Continuous Integration environments.

Finally let's take a look at a test implemented in exactly the same fashion, but performing the job of cancelling an Order by sending a HTTP Request with a DELETE HTTP Method to the Order's URI (the full code for this can be found in the `CancelOrderIntegrationTest` test class):

  	@Test
  	public void thatDeleteOrderUsesHttpOkOnSuccess() throws Exception {

    	when(orderService.deleteOrder(any(DeleteOrderEvent.class)))
            .thenReturn(
                    orderDeleted(key));

    	this.mockMvc.perform(
            delete("/aggregators/orders/{id}", key.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

    	verify(orderService).deleteOrder(argThat(
            Matchers.<DeleteOrderEvent>hasProperty("key",
                    Matchers.equalTo(key))));
  	}

  	}


## Making the tests pass: implementing the Controllers

### Implementing the OrderQueriesController

Let's start by implementing the controller that is responsible for handling requests that simply read the current state of the Order resources.

The first step is to map the root URI to the controller as shown in the following code snippet:

	@Controller
	@RequestMapping("/aggregators/orders")
	class OrderQueriesController {

Next we need to implement two handler methods that response to a request with a GET HTTP Method on the root URI of /aggregators/orders:

	    @RequestMapping(method = RequestMethod.GET)
    	@ResponseStatus(HttpStatus.OK)
    	@ResponseBody
    	public List<Order> getAllOrders() {
        	List<Order> orders = new ArrayList<Order>();
        	for (OrderDetails detail : orderService.requestAllOrders(new RequestAllOrdersEvent()).getOrdersDetails()) {
            	orders.add(Order.fromOrderDetails(detail));
        	}
        	return orders;
    	}

Notice how a controller handler method implementation is kept very clean as all interactions with the underlying system occur via firing events into the core domain. You should always avoid having business logic in your controllers and delegate that responsibility to a collaborating component.

This handler method is, by default, listening for requests that arrive targeting the root URI as specified at the controller class level, in this case that is /aggregator/orders.

You're using @RequestMapping to specify that this handler method responds only to those HTTP requests that contain the GET HTTP Method. We're also using the @ResponseStatus annotation for the first time here to set the default, successful execution Http Status code for the HTTP response that is returned to the client.

Finally we can implement the last query method for Orders on our service that will respond to a request for a specific Order.

	    @RequestMapping(method = RequestMethod.GET, value = "/{id}")
    	public ResponseEntity<Order> viewOrder(@PathVariable String id) {

        	OrderDetailsEvent details = orderService.requestOrderDetails(new RequestOrderDetailsEvent(UUID.fromString(id)));

	        if (!details.isEntityFound()) {
	            return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
	        }

	        Order order = Order.fromOrderDetails(details.getOrderDetails());

	        return new ResponseEntity<Order>(order, HttpStatus.OK);
 	   }

To be concluded…

That's all the handler methods that we need for directly requesting information about the current state of your service's Orders. The next step is to support that state being manipulated by requests that instigate commands.

### Implementing the OrderCommandsController

To encapsulate all the requests that change the state of your Orders, we're going to create a class called `OrderCommandsController` and map it to the root URL for Order resources /aggregators/orders as shown below.

	@Controller
	@RequestMapping("/aggregators/orders")
	public class OrderCommandsController {

Next we need to field a request for all the Orders available in the system. To 

## Where did the response content get generated from?

### Using JAXB to marshall Objects into Content

Mention the split of Commands from Queries across two controllers, for mutating requests and non-mutating ones.

[Next… Wiring Up and Deploying your Service](../3/)