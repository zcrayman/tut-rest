package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.domain.Order;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/aggregators/order")
class OrderController {

  private OrderService orderService;

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  @RequestMapping(method = RequestMethod.POST)
  @ResponseStatus( HttpStatus.CREATED )
  public @ResponseBody Order createOrder() {
    //TODO use a command object?
    //TODO generate the new order
    //TODO ensure that the Locaton header is set to the new order URI

    return null;
  }

  @RequestMapping(method = RequestMethod.GET, value = "/{id}")
  public @ResponseBody Order getOrder(@PathVariable String id) {
    //TODO, obtain the order
    //TODO, set up response mapping

    return null;
  }

  @RequestMapping(method = RequestMethod.DELETE, value = "/{id}")
  @ResponseStatus( HttpStatus.OK )
  public @ResponseBody Order cancelOrder(@PathVariable String id) {
    //TODO, obtain the order
    //TODO, check if it can be cancelled.  if so, instruct to cancel.

    //TODO, if not, issue a 403/Forbidden
    return null;
  }

}
