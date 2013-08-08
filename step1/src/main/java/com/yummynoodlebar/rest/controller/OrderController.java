package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.rest.domain.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
@RequestMapping("/aggregators/order")
public class OrderController {

  @RequestMapping(method = RequestMethod.POST)
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
  public @ResponseBody Order cancelOrder(@PathVariable String id) {
    //TODO, obtain the order
    //TODO, check if it can be cancelled.  if so, instruct to cancel.

    //TODO, if not, issue a 403/Forbidden
    return null;
  }

}
