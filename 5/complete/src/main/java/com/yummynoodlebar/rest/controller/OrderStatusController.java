package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.events.orders.OrderStatusEvent;
import com.yummynoodlebar.core.events.orders.RequestOrderStatusEvent;
import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.domain.OrderStatus;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import java.util.UUID;

@Controller
@RequestMapping("/aggregators/orders/{id}/status")
public class OrderStatusController {

  @Autowired
  private OrderService orderService;

  @RequestMapping(method = RequestMethod.GET)
  public ResponseEntity<OrderStatus> getOrderStatus(@PathVariable String id) {

    OrderStatusEvent orderStatusEvent = orderService.requestOrderStatus(new RequestOrderStatusEvent(UUID.fromString(id)));

    if (!orderStatusEvent.isEntityFound()) {
      return new ResponseEntity<OrderStatus>(HttpStatus.NOT_FOUND);
    }

    return new ResponseEntity<OrderStatus>(
            OrderStatus.fromOrderStatusDetails(
                    orderStatusEvent.getKey(),
                    orderStatusEvent.getOrderStatus()),
            HttpStatus.OK);
  }
}
