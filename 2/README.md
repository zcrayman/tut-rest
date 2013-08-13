# Step 2: Building Your First RESTful Service using Spring MVC

It's time to implement your Yummy Noodle Bar RESTful service. The first step in building a service with Spring MVC is to construct and test one or more Controllers that will be responsible for handling each of the incoming HTTP Requests that you defined for your service in the previous step.

## Constructing a Controller for Orders

The first controller you're going to create will handle the incoming requests associated with manipulating Orders. These requests can be split into two categories:

* Requests that read, or query, the current state of an Order
* Requests that change the state of a new or existing Order

It's possible to implement these two categories of interactions using one controller but the [Command Query Responsibility Segregation (CQRS)](http://martinfowler.com/bliki/CQRS.html) pattern advises us to split these responsibilities into different routes through our application, and so in this tutorial we'll implement these controllers separately.

### Implementing the OrderCommandsController

	@Controller
	@RequestMapping("/aggregators/orders")
	public class OrderCommandsController {



(the below is copy and paste, rewrite)
NB.  Java Config, as set up in MVCConfig, will detect the existence of Jackson and JAXB 2 on the classpath and automatically creates and registers default JSON and XML converters. The functionality of the annotation is equivalent to the XML version:

<mvc:annotation-driven />

This is a shortcut, and though it may be useful in many situations, it’s not perfect. When more complex configuration is needed, remove the annotation and extend WebMvcConfigurationSupport directly.

^^^^^^^
The above has been moved to page3, leaving this page purely in the MockMVC tests

Mention the split of Commands from Queries across two controllers, for mutating requests and non-mutating ones.

[Next… Wiring Up and Deploying your Service](../3/)