## Step 6: Make your Service Discoverable using Spring HATEOAS

HATEOAS, Hypermedia As The Engine of Application State, may be one of the worst acronyms you may come across, but it is also a crucial technology and approach to building flexible RESTful services.

HATEOS allows you to build services that *support the discovery of resources* and re-introduce the linking of resources together that is an essential part of hypertext systems.

HATEOS provides a consistent mechanism for you to describe what resources you have and what resources relate to them. A client that understands HATEOS and its corresponding links will be able to effectively discover and react to what resources are supported at a given moment in time *without having to agree this all up front*.

### Resources and Linking them together

Building a REST service is all about creating resources, representing them to clients and providing consistent locations for them to be accessed at.  Naturally, many of your resources will be related together.  For Yummy Noodle Bar, an Order resource has a related Status and Payment Detail resources.  In advance, we could tell the developers of the clients that the uri of the status was `/orders/{id}/status`.   

What if we didn't have to do this?  If we would embed the location of the status within the Order resource itself.  
This is the natural way that hypertext systems work, and HTTP is no exception.  It naturally deals in URLs, and the most native system delivered over HTTP, HTML, has linking deeply integrated.  This is the way that the web works.   If we want to build RESTful service, including links between our resources would seem to be a good thing.

How can we do this?

Enter HATEOAS.  In short, this adds support to be able to turn this 

    { 
        "name": "Derek",
        "age": 15
    }
    
into this

    {
        "name": "Derek",
        "age": 15
        
        links : [ 
            { rel : "self", href : "http://myhost/people/derek" },
            { rel : "Mother", href : "http://myhost/people/shirley" },
            { rel : "Father", href : "http://myhost/people/brad" }
        ]
    }

This introduces a rich linking into the JSON model (or XML, if you prefer angle brackets).  These follow a specification and are automatically discoverable by a client that understands HATEOAS

### Implementing for Yummy Noodle Bar

First, we need to import spring-hateoas into our new project.

Update `build.gradle` with the following

    dependencies {
        ...
        compile 'org.springframework.hateoas:spring-hateoas:0.7.0.RELEASE'
        ...
    }


Following our practice of creating a test before altering the codebase, add this into your OrdersTest test.

    @Test
    public void thatOrdersHaveCorrectHateoasLinks() {
    
        ResponseEntity<Order> entity = ...
        
        Order order = entity.getBody();
        
        String orderBase = "/aggregators/order/" + order.getKey();
        
        assertEquals(entity.getHeaders().getLocation().toString(), order.getLink("self").getHref());
        assertTrue(order.getLink("Order Status").getHref().endsWith(orderBase + "/status"));
    }
    
This will not compile, as the Order class won't have a getLink() method.
The full test class will now look something like (we've extracted the order generation into a helper, as its duplicated 3 times now)

    package com.yummynoodlebar.rest.functional;
    
    
    import com.yummynoodlebar.rest.controller.fixture.RestDataFixture;
    import com.yummynoodlebar.rest.domain.Order;
    import com.yummynoodlebar.rest.domain.OrderStatus;
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
        
            ResponseEntity<Order> entity = createOrder();
            
            String path = entity.getHeaders().getLocation().getPath();
            
            assertEquals(HttpStatus.CREATED, entity.getStatusCode());
            assertTrue(path.startsWith("/aggregators/order/"));
            Order order = entity.getBody();
            
            System.out.println ("The Order ID is " + order.getKey());
            System.out.println ("The Location is " + entity.getHeaders().getLocation());
            
            assertEquals(2, order.getItems().size());
        }
        
        @Test
        public void thatOrdersHaveCorrectHateoasLinks() {
        
            ResponseEntity<Order> entity = createOrder();
            
            Order order = entity.getBody();
            
            String orderBase = "/aggregators/order/" + order.getKey();
            
            assertEquals(entity.getHeaders().getLocation().toString(), order.getLink("self").getHref());
            assertTrue(order.getLink("Order Status").getHref().endsWith(orderBase + "/status"));
        }
        
        @Test
        public void thatNewOrderHasOrdersStatusCreated() {
        
            ResponseEntity<Order> entity = createOrder();
            
            Order order = entity.getBody();
            
            HttpEntity<String> requestEntity = new HttpEntity<String>(
                RestDataFixture.standardOrderJSON(),getHeaders());
            
            RestTemplate template = new RestTemplate();
            
            ResponseEntity<OrderStatus> response = template.exchange(
                order.getLink("Order Status").getHref(),
                HttpMethod.GET,
                requestEntity, OrderStatus.class);
            
            assertEquals(HttpStatus.OK, response.getStatusCode());
            assertEquals("Order Created", response.getBody().getStatus());
        }
        
        private ResponseEntity<Order> createOrder() {
            HttpEntity<String> requestEntity = new HttpEntity<String>(
                RestDataFixture.standardOrderJSON(),getHeaders());
            
            RestTemplate template = new RestTemplate();
            return template.postForEntity(
                "http://localhost:8080/aggregators/order",
                requestEntity, Order.class);
        }
        
        static HttpHeaders getHeaders() {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_JSON);
            headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));
            
            String authorisation = "letsnosh" + ":" + "noshing";
            byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes());
            headers.add("Authorization", "Basic " + new String(encodedAuthorisation));
            
            return headers;
        }
    
    }


This test will now not compile.

To rectify this, but still leave a failing test, update `rest.domain.Order` to read 
    ..
    import org.springframework.hateoas.ResourceSupport;

    public class Order extends ResourceSupport implements Serializable {
        ...


This will now compile, and the new test will fail, as your provided links will be empty.

Lets add some links!

The interesting ones for an order are, as mentioned, the Status and the Payment Details resources.  So, lets add those in.

Spring Hateoas make this very easy, using the class we just imported.

At this point, its worth re-iterating, adding in these kind of very view specific technologies and additions to `rest.domain.Order` is very natural, and to be expected.  The entire job of this class is to be the representation of a particular resource. Its entire job is to support integration with the REST concerns.  Persistence, validation and business logic are all handled elsewhere.

So, to add some links.  Adding a link in Spring Hateoas looks like this :-

    order.add(linkTo(OrderQueriesController.class).slash(key).withSelfRel());
    
This is quite terse and readable.  

Firstly, it defines where the link is going.  Namely, to the OrderQueriesController, followed by an ID (UUID in our case).  This will work with the template URI that we defined on OrderQueriesController earlier to map the URI to OrderQueriesController.viewOrder().  Although at this point, we aren't that concerned.

The .withSelfRel() indicates that this is the definitive URI for this Resource, a self reference.   This is most useful if you store a Resource without its url, and want to reference it again later, or if you have just created a new Resource, and want to navigate to its location.  In the second case, the Location header should also be available.

To add links that are not self references, the syntax is :-

    order.add(linkTo(OrderStatusController.class).slash(key).slash("status").withRel("Order Status"));

This creates an explicit relation link, that of 'Order Status'.  This can be queried for explicitly by a client.

In the life preserver model we have been using, the rest.domain.Order class is the focus for all view and representation concerns that relate the to Order Resources.   Given that, its reasonable to embed the addition of links into that class. 

Extend `rest.domain.Order` to include link generation.

    public static Order fromOrderDetails(OrderDetails orderDetails) {
        Order order = new Order();
        
        order.dateTimeOfSubmission = orderDetails.getDateTimeOfSubmission();
        order.key = orderDetails.getKey();
        order.setItems(orderDetails.getOrderItems());
        
        order.add(linkTo(OrderController.class).slash(order.key).withSelfRel());
        order.add(linkTo(OrderController.class).slash(order.key).slash("status").withRel("Order Status"));
        order.add(linkTo(OrderController.class).slash(order.key).slash("paymentdetails").withRel("Payment Details"));
        
        return order;
    }


If you run up the application `./gradlew tomcatRunWar` and the run `OrderTests` again, you should find that it passes, meaning that the JSON contains the appropriate Links.


[Next… Recap and Where to go Next](../7/)