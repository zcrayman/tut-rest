## Step 6: Make your Service Discoverable using Spring HATEOAS

HATEOS, Hypermedia As The Engine of Application State, may be one of the worst acronyms but it is also a crucial technology and approach to building flexible RESTful services.

HATEOS allows you to build services that *support the discovery of resources* and then the *discovery of what can be done to those resources*.

From our example, you can see what HATEOS enables by appending out interaction as defined using HTTP verbs with the word "Discover". Beforehand our interaction was described as:

* See all the Orders by sending a GET request to the Orders URI
* Cancel all the Orders by sending a DELETE request to the Orders URI
* Create a new Order by sending a POST request to the Orders URI
* Show the details of an existing order by sending a GET request to a specific Order's URI
* Update an Order by sending a PUT request to a specific Order's URI
* See an Order's status by sending a GET request to an Order's Status URI
* Cancel an Order by sending a DELETE request to a specific Order's URI

HATEOS allows a client to:
* *Discover* that they can see all the Orders by sending a GET request to the Orders URI
* *Discover* that they can cancel all the Orders by sending a DELETE request to the Orders URI
* *Discover* that they can create a new Order by sending a POST request to the Orders URI 
* *Discover* that they can show the details of an existing order by sending a GET request to a specific Order's URI
* *Discover* that they can update an Order by sending a PUT request to a specific Order's URI
* *Discover* that they can see an Order's status by sending a GET request to an Order's Status URI
* *Discover* that they can cancel an Order by sending a DELETE request to a specific Order's URI

HATEOS provides a consistent mechanism for you to describe what resources you have and what can be done with those resources. A client that understands HATEOS and its corresponding links will be able to effectively discover and react to what resources and actions on those resources are supported at a given moment in time *without having to agree this all up front*.

### Simplifying JSON and XML representation generation

### Using Link and LinkDiscoverer to discover Resources

### Simplifying URI Generation using ControllerLinkBuilder

TBD Implementing Hypermedia links to give hints as to how your resources can be interacted with.
TBD Testing for Hypermedia links using REST Template.