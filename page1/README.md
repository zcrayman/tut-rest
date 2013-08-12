## Step 1: Modelling the RESTful Service Domain

![Life Preserver showing Core Domain](../images/life-preserver-core-domain-focus.png)

Opening up the 'initial' project you'll be able to see that under src/main/java/com/yummynoodlebar the core, application-internal domain of the Yummy Noodle Bar is made up of the following components:

* **Order**

    An individual order in the system that has an associated status and status history for tracking purposes.

* **OrderStatus**

    The current allocated status to an order.

* **Payment**

    The Payment that a customer wants to make for a given Order.

* **PaymentDetails**

    The Details of the Payment that a customer wants to make for a given Order.

* **PaymentStatus**

    The current status of a Payment that a customer wants to make for a given Order.

Focussing for this tutorial on the Order domain classes, these can be acted upon by a number of events under the com.yummynoodlebar/events/orders package as shown on the following diagram:

![Life Preserver showing Orders Sub-Domain in Events Domain](../images/life-preserver-event-domain-focus-with-orders.png)

Events in this case decouple out the domain concepts in the core  of the Yummy Noodle Bar application from the various integrations that may need to access and work upon the core. 

The event components associated with Orders include:

* **RequestAllOrdersEvent** and **AllOrdersEvent** 

    Corresponding events to request the associated OrderDetails about all Orders and the response to that request.

* **CreateOrderEvent** and **OrderCreatedEvent**

    Corresponding events to request the creation of a new Order, and a confirmation that the new Order has been created.

* **DeleteOrderEvent** and **OrderDeletedEvent**

    Corresponding events to delete an existing Order and then to confirm that the Order has been deleted.

* **RequestOrderDetailsEvent** and **OrderDetailsEvent**

    Corresponding events to request the current details of an Order, and then to receive those details.

* **RequestOrderStatusEvent** and **OrderStatusEvent**

    Corresponding events to request the current status of an Order, and then to receive the current status.

* **SetOrderPaymentEvent**

    Event is triggered when Payment is to be set on an existing Order.

* **OrderUpdatedEvent** 

    Event is triggered when an Order is updated.

## Modelling the RESTful Service Domain

For the first version of your new Yummy Noodle Bar RESTful service, the ability to create, update and remove Orders is the focus.

It can be tempting to simply expose the core Order domain to the outside world and work from there, but that would ignore the boundary between the Core and the RESTful service domain (TODO highlight this boundary on a focus on the Life Preserver).

The public API of your service that you are going to expose to the aggregators will need to change at a rate that is friendly to those clients, and the core will need to evolve at whatever rate the Yummy Noodle bar system need to internally evolve at. So there is potentially friction between the two domains as they may need to evolve at different rates.

To manage this friction you need to create concepts and components in the RESTful Service domain that are unique to, and can evolve at the rate needed by, the RESTful domain itself. This may result in similar types of components but since their purpose will be very different the similarities are superficial.

## Modelling the Orders and Order Resources

There are three stages to modelling your RESTful Service domain, they are:

* Designing your Resources - What resources you need to expose to the outside world
* Designing your URIs - How your resources will be publicly addressed
* Adding the verbs - what operations can you perform on your RESTfully exposed resources

### Designing your Resources

When looking for the resources that you are going to support through your RESTful Service the first step is to look for the relevant nouns in your domain. In the case of the Yummy Noodle Bar, the following nouns are candidates for exposure to the outside world from the core domain:

* Customer
* Order
* OrderStatus
* OrderStatusHistory
* Payment
* PaymentDetails
* PaymentStatus

The purpose of the Yummy Noodle Restful Service is to allow aggregators and partners to submit and track orders as they are executed and delivered from the Yummy Noodle Bar. To do this a subset of the available domain concepts make up your initial cut of the resources you are going to expose:

* Order
* OrderStatus
* PaymentDetails
* PaymentStatus

The following updated Life Preserver shows these domain components and where they live in the design.

![Life Preserver RESTful Domain Focus](../images/life-preserver-rest-domain-concepts-focus.png)

Although these concepts existing in the Core domain and the REST Domain, this is not exactly repetition as the purpose of the implementations are very different from Core to REST.

In the Core Domain the concepts are captured as part of the internal ubiquitous language of the application's domain. In the REST domain the concepts are captured as they are used purely for the purpose of exposing the public RESTful interface. 

### Designing your URIs

Each resource needs to be addressable using a URI. In addition, the address implies the relationship between each of the resources.

For your Yummy Noodle Bar RESTful Service domain, the resources will have the following URIs:

* All Orders

        http://www.yummynoodlebar.com/aggregators/orders

* An Order

        http://www.yummynoodlebar.com/aggregators/order/{Order ID}

* All MenuItems

        http://www.yummynoodlebar.com/aggregators/menu

* A specific MenuItem

        http://www.yummynoodlebar.com/aggregators/menu/{MenuItem ID}

* All MenuItems associated with an Order

        http://www.yummynoodlebar.com/aggregators/order/{Order ID}/items

* A specific MenuItem associated with an Order

        http://www.yummynoodlebar.com/aggregators/order/{Order ID}/item/{MenuItem ID}

* The current OrderStatus

        http://www.yummynoodlebar.com/aggregators/order/{Order ID}/status

* The PaymentDetails for an Order

        http://www.yummynoodlebar.com/aggregators/order/{Order ID}/paymentdetails

* PaymentStatus

        http://www.yummynoodlebar.com/aggregators/order/{Order ID}/paymentstatus

Each of the above URI's are expressed as *templates*; they contain blocks demarcated with {} in the URI. Since the URI in a RESTful service should completely address the resource, without any additional query parameters, then there will be parts of the URI that are specific to the resource itself. An example would be 

Here we've used the {} notation to specify where part of the URL will be unique when identifying specific resources. As an example, an Order with Order ID of 1 would have the following specific URL once the URI template is furnished with the Order Number:

    http://www.yummynoodlebar.com/aggregators/order/1

An Order with an Order ID of 37 would have the following specific URI:

    http://www.yummynoodlebar.com/aggregators/order/37

This quality of the URI changing to work with specific resources is what gives a resource the quality of being *addressable*.

### Adding the verbs

According the HTTP 1.1 Specification [ref]

**POST is defined as**

    The POST method is used to request that the origin server accept the entity enclosed in the request as a new subordinate of the resource identified by the Request-URI in the Request-Line

**PUT is defined as**

    The PUT method requests that the enclosed entity be stored under the supplied Request-URI. 
    If the Request-URI refers to an already existing resource, the enclosed entity
    SHOULD be considered as a modified version of the one residing on the origin server. 
    If the Request-URI does not point to an existing resource, and that URI is capable 
    of being defined as a new resource by the requesting user agent, the origin server can create 
    the resource with that URI.

Summarised, POST is used to create new entities without knowing the final URI, and PUT is used to create and update entities in a previously known URI.

We will follow this usage throughout the tutorial.

## Understanding Status Codes

Discussion on correct status code usage, esp the 4XX codes that we will use in managing URI mutation and access control