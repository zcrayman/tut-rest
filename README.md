
In this tutorial you'll use Spring to create a production-strength RESTful web service. 

## Why RESTful web services?

From integrating with [Amazon Web Services](http://aws.amazon.com) to syndicating multiple feeds of data, RESTful web services that follow the guidelines of [Roy Fielding's architectural style](http://www.ics.uci.edu/~fielding/pubs/dissertation/top.htm) provide simple, effective web APIs that scale from a few users to millions.

It's likely that you want to implement a RESTful web service because:

* You're creating an API that clients need to consume across the web.
* You want to open up your organization's data to consumption by varied clients across the web.
* You need to integrate your application with other applications inside your own organization, but you don't have control over the languages, tools, or frameworks for those applications.

Or maybe you're just curious to see what a Spring-implemented RESTful web service looks like. Whatever your reason, you've come to the right place.

## What you'll build

Yummy Noodle Bar is going global. It wants to provide a RESTful web service to a set of aggregators, in particular Let’s Nosh, a massive brand of aggregator that will bring big business to the small-scale noodle bar. You'll extend Yummy Noodle Bar's internal application by creating a new public Web API for submitting, tracking, canceling, and amending orders.

![Yummy Noodle Bar](images/yummynoodle.jpg)

## What you'll need

* About an hour.
* An installation of the [Gradle](http://www.gradle.org) build tool, version 1.6 or later.
* A copy of the code (TODO - downloadable as Zip and/or git clone).
* An IDE of your choice; Spring recommends [Spring Tool Suite](http://www.springsource.org/sts), which is a [free download](http://www.springsource.org/sts).

## Yummy Noodle Bar application architecture and the Core domain

The current architecture of the application is shown in the following "life preserver" diagram:

![Life Preserver showing Core packages](images/life-preserver-initial.png)

Open the Initial project and you'll see that the life preserver diagram maps to the different packages under src/main/java. 

Under the application's top-level packages, that is, com.yummynoodlebar, here's what the packages contain:

* **domain**. Components that cleanly capture the application's Core domain concepts. These classes are a manifestation of the [ubiquitous language](http://martinfowler.com/bliki/UbiquitousLanguage.html) of the Core domain.

* **repository**. Components that store and retrieve the current state of the system's domain objects.

* **event**. Components that are the events that the domain can receive and process.

* **service**. Components that handle the actions that can be performed when an event is received.

Take a moment to familiarize yourself with the components in each package. The tests for the core domain components are available in the src/test area in the `initial` project. They will give you an idea of how these components will be used.

## RESTful web services domain

RESTful web services integrate your application and the myriad possible clients that need to consume your services. As such, RESTful services live in their own integration domain on the periphery of your application's core, as shown in the following update to your life preserver.

![Life Preserver showing Core and REST domain](images/life-preserver-rest-domain-intro.png)

Given the integration between your application and the outside world, consider the following design and implementation constraints:

* Your RESTful service component's primary purpose is to implement the necessary functionality for the specific RESTful service you are exposing, and are not necessarily a one-to-one exposure of the internals of your application.
* The components that make up your RESTful services need to evolve at a rate that is appropriate for the many consumers that rely on your services.
* Your RESTful service components should not contain any core logic for your application, but they will collaborate with other components in the Core domains of your application in order to orchestrate the necessary functionality for the service interface.

## Tutorial road map

* [Step 1: Modeling a RESTful Web Service Domain](1/)
* [Step 2: Building Your First RESTful Web Service](2/)
* [Step 3: Wiring Up and Deploying your Service](3/)
* [Step 4: Testing Your Service with Spring's REStTemplate](4/)
* [Step 5: Securing Your Service with Spring Security](5/)
* [Step 6: Making your Service Discoverable with Spring HATEOAS](6/)
* [Recap and What's Next?](7/)


