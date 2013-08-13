# Step 3: Wiring Up and Deploying your Service

Now you have written and tested your controllers, it's time to bring the whole application together by:

* Creating configuration for the core of your application
* Creating a configuration for the your REST components
* Initialising your Web Application itself
* Running up your complete RESTful service

## Creating a Configuration for your Application's Core Domain using Spring JavaConfig

The Yummy Noodle Bar application contains a core set of components that include domain classes and services.

You could just create a configuration for these components but, since we've been applying Test Driven Development from the start, you're going to apply the same approach to your configuration.

### Testing your Core Configuration

First, construct an integration test that contains the following:

	package com.yummynoodlebar.config;

	import com.yummynoodlebar.core.events.orders.AllOrdersEvent;
	import com.yummynoodlebar.core.events.orders.CreateOrderEvent;
	import com.yummynoodlebar.core.events.orders.OrderDetails;
	import com.yummynoodlebar.core.events.orders.RequestAllOrdersEvent;
	import com.yummynoodlebar.core.services.OrderService;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.test.context.ContextConfiguration;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

	import static junit.framework.TestCase.*;

	@RunWith(SpringJUnit4ClassRunner.class)
	@ContextConfiguration(classes = {CoreConfig.class})
	public class CoreDomainIntegrationTest {

  	@Autowired
  	OrderService orderService;

  	//TODOCUMENT We have already asserted the correctness of the collaboration.
  	//This is to check that the wiring in CoreConfig works.
  	//We do this by inference.
  	@Test
  	public void addANewOrderToTheSystem() {

    	CreateOrderEvent ev = new CreateOrderEvent(new OrderDetails());

    	orderService.createOrder(ev);

    	AllOrdersEvent allOrders = orderService.requestAllOrders(new RequestAllOrdersEvent());

    	assertEquals(1, allOrders.getOrdersDetails().size());
  }
}

This integration test simply constructs an `ApplicationContext` using JavaConfig as specified on the `@ContextConfiguration` annotation. The Core domain's configuration will be created using Spring JavaConfig in a class called `CoreConfig`.

With that `ApplicationContext` constructed, the test can have it's `OrderService` test entry point dependency injected, ready for the following test methods.

Finally you have one test method that asserts that an `orderService` dependency has been provided and appears to work correctly.

Next stop, creating the Core domain configuration.

### Implementing your Core Domain Configuration

The Core Domain configuration for the Yummy Noodle Bar application only contains one service and one dependency that needs to be configured for that service.

The following code shows the complete configuration class:

	package com.yummynoodlebar.config;

	import com.yummynoodlebar.core.domain.Order;
	import com.yummynoodlebar.core.repository.OrdersMemoryRepository;
	import com.yummynoodlebar.core.repository.OrdersRepository;
	import com.yummynoodlebar.core.services.OrderEventHandler;
	import com.yummynoodlebar.core.services.OrderService;
	import org.springframework.context.annotation.Bean;
	import org.springframework.context.annotation.Configuration;

	import java.util.HashMap;
	import java.util.UUID;

	@Configuration
	public class CoreConfig {

  	@Bean
  	public OrderService createService(OrdersRepository repo) {
    	return new OrderEventHandler(repo);
  	}

  	@Bean
  	public OrdersRepository createRepo() {
    	return new OrdersMemoryRepository(new HashMap<UUID, Order>());
  	}
	}

Spring JavaConfig will detect each of the `@Bean` annotated methods as methods that generate configured Spring Beans.

In order, Spring will create the `OrdersRepository` bean first, and then use that as the single parameter into the `createService` method in order to create the `OrderService` bean.

Running the `CoreDomainIntegrationTest` in the `com.yummynoodlebar.config` test package will verify that your Core Domain configuration is good to go.

## Creating a configuration for the your REST components

Configuring your new set of controller's is very straightforward as you have used `@Controller` on each of the controller classes. To initialise your RESTful Domain's components, all you need to do is turn on component scanning so that Spring can find and initialise these Spring Beans.

## Implementing your RESTful Domain Configuration

You can create the following Spring JavaConfig to execute component scanning for the components in your application's RESTful domain:

	package com.yummynoodlebar.config;

	import org.springframework.context.annotation.ComponentScan;
	import org.springframework.context.annotation.Configuration;
	import org.springframework.web.servlet.config.annotation.EnableWebMvc;

	@Configuration
	@EnableWebMvc
	@ComponentScan(basePackages = {"com.yummynoodlebar.rest.controller"})
	public class MVCConfig {}

The `@ComponentScan` attribute in JavaConfig specifies that your components should be found underneath the base Java package of `com.yummynoodlebar.rest.controllers`. It's always a good idea to be as specific as possible when defining the place where component scanning should occur so that you don't accidentally end up initialising components you didn't expect!

## Testing your RESTful Domain Configuration

No configuration should be trusted without an accompanying test. The following test is the full implementation that asserts that the output of the RESTful configuration is as it should be:

	package com.yummynoodlebar.config;

	import com.yummynoodlebar.rest.controller.fixture.RestDataFixture;
	import org.junit.Before;
	import org.junit.Test;
	import org.junit.runner.RunWith;
	import org.springframework.beans.factory.annotation.Autowired;
	import org.springframework.http.MediaType;
	import org.springframework.test.context.ContextConfiguration;
	import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
	import org.springframework.test.context.web.WebAppConfiguration;
	import org.springframework.test.web.servlet.MockMvc;
	import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

	import static com.yummynoodlebar.rest.controller.fixture.RestDataFixture.standardOrderJSON;
	import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
	import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
	import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
	import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
	import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

	@RunWith(SpringJUnit4ClassRunner.class)
	@WebAppConfiguration
	@ContextConfiguration(classes = {CoreConfig.class, MVCConfig.class})
	public class RestDomainIntegrationTest {

  	@Autowired
  	WebApplicationContext wac;

  	private MockMvc mockMvc;

  	@Before
  	public void setup() {
    	this.mockMvc = MockMvcBuilders.webAppContextSetup(this.wac).build();
  	}

  	@Test
  	public void addANewOrderToTheSystem() throws Exception  {
    	this.mockMvc.perform(
            post("/aggregators/order")
                    .content(standardOrderJSON())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isCreated());

    	this.mockMvc.perform(
            get("/aggregators/orders")
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk())
            .andExpect(jsonPath("$[0].items['" + RestDataFixture.YUMMY_ITEM + "']").value(12));

  	}
	}
 

To be continued… include:
//TODOCUMENT We have already asserted the correctness of the collaboration.
//This is to check that the wiring in MVCConfig works.
//We do this by inference, via hitting URLs in the system and checking they work as expected
//given a well known infrastructure and system state.
//this is a minimal set, as we've checked the actual behaviour of rendering, http status handling
//and URL mapping separately.

Create a Config covering the rest components. - MVCConfig
Create an integration that runs up both configurations together and exercises the MVC stack fully integrated with the core - RestDomainIntegrationTest.  
THis is outside of a web container, but includes the DispatcherServlet (as did the controller integration tests written on page 2)

Setting up a web.xml - we use JavaCOnfig, so use AnnotationConfigWebApplicationContext as contextClass init param
 - we have created a configuration domain, com.yummynoodlebar.core.config

to be able to build a war 

build.gradle
apply plugin: 'war'

to be able to run up in dev mode

build.gradle
apply plugin: 'tomcat'

Change context path otherwise the default is the directory name */
build.gradle
tomcatRunWar.contextPath = ''

We can now make a working war file 

./gradlew war


We can start the app in dev mode using gradle

./gradlew tomcatRunWar

Then visit http://localhost:8080/aggregators/orders

You will get a response JSON that is an empty list 

We need to check that this is all set up validly, taking us on to the next subject, functional testing using RestTemplate

Notes from prior step:

(the below is copy and paste, rewrite)
NB.  Java Config, as set up in MVCConfig, will detect the existence of Jackson and JAXB 2 on the classpath and automatically creates and registers default JSON and XML converters. The functionality of the annotation is equivalent to the XML version:

<mvc:annotation-driven />

This is a shortcut, and though it may be useful in many situations, it’s not perfect. When more complex configuration is needed, remove the annotation and extend WebMvcConfigurationSupport directly.

^^^^^^^
The above has been moved to page3, leaving this page purely in the MockMVC tests


[Next… Testing your Service using RESTTemplate](../4/)
