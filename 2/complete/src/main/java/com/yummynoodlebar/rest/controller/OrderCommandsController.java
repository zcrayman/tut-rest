package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.events.orders.CreateOrderEvent;
import com.yummynoodlebar.core.events.orders.DeleteOrderEvent;
import com.yummynoodlebar.core.events.orders.OrderCreatedEvent;
import com.yummynoodlebar.core.events.orders.OrderDeletedEvent;
import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.util.UriComponentsBuilder;

import java.util.UUID;

// {!begin root}
@Controller
@RequestMapping("/aggregators/orders")
public class OrderCommandsController {
// {!end root}

    private static Logger LOG = LoggerFactory.getLogger(OrderCommandsController.class);

    @Autowired
    private OrderService orderService;

    // {!begin createOrder}
    @RequestMapping(method = RequestMethod.POST)
    public ResponseEntity<Order> createOrder(@RequestBody Order order, UriComponentsBuilder builder) {

        OrderCreatedEvent orderCreated = orderService.createOrder(new CreateOrderEvent(order.toOrderDetails()));

        Order newOrder = Order.fromOrderDetails(orderCreated.getDetails());

        HttpHeaders headers = new HttpHeaders();
        headers.setLocation(
                builder.path("/aggregators/orders/{id}")
                        .buildAndExpand(orderCreated.getNewOrderKey().toString()).toUri());

        return new ResponseEntity<Order>(newOrder, headers, HttpStatus.CREATED);
    }
    // {!end createOrder}

    // {!begin cancelOrder}
    @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
    public ResponseEntity<Order> cancelOrder(@PathVariable String id) {

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
    // {!end cancelOrder}
}
