package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.events.orders.*;
import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

@Controller
@RequestMapping("/aggregators/order")
class OrderController {

  private static Logger LOG = LoggerFactory.getLogger(OrderController.class);

  @Autowired
  private OrderService orderService;

  @RequestMapping(method = RequestMethod.POST)
  //TODOCUMENT using a response entity allows control of both the http status code and the headers.
  //we could use an annotation to control the status code, but we require ResponseEntity for the header control
  //we are returning the Location header back to the client, so thy know where the new order has been created.
  //we also embed the order as the body, so they can read the key UUID in the returned json.
  public ResponseEntity<Order> createOrder(@RequestBody Order order, UriComponentsBuilder builder) {

    OrderCreatedEvent orderCreated = orderService.createOrder(new CreateOrderEvent(order.toOrderDetails()));

    Order newOrder = Order.fromOrderDetails(orderCreated.getDetails());

    HttpHeaders headers = new HttpHeaders();
    headers.setLocation(
            builder.path("/aggregators/order/{id}")
                    .buildAndExpand(orderCreated.getNewOrderKey().toString()).toUri());

    return new ResponseEntity<Order>(newOrder, headers, HttpStatus.CREATED);
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public
  ResponseEntity<Order> viewOrder(@PathVariable String id) {

    OrderDetailsEvent details = orderService.requestOrderDetails(new RequestOrderDetailsEvent(UUID.fromString(id)));

    if (!details.isEntityFound()) {
      return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
    }

    Order order = Order.fromOrderDetails(details.getOrderDetails());

    return new ResponseEntity<Order>(order, HttpStatus.OK);
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  public
  ResponseEntity<Order> cancelOrder(@PathVariable String id) {

    OrderDeletedEvent orderDeleted = orderService.deleteOrder(new DeleteOrderEvent(UUID.fromString(id)));

    if (!orderDeleted.isEntityFound()) {
      return new ResponseEntity<Order>(HttpStatus.NOT_FOUND);
    }

    Order order = Order.fromOrderDetails(orderDeleted.getDetails());

    if (orderDeleted.isDeletionCompleted()) {
      return new ResponseEntity<Order>(order, HttpStatus.OK);
    }

    return new ResponseEntity<Order>(order, HttpStatus.FORBIDDEN);
  }
}
