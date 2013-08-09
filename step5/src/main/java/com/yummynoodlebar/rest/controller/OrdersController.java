package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.domain.Order;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.Arrays;
import java.util.Date;
import java.util.List;

@Controller
@RequestMapping("/aggregators/orders")
class OrdersController {

  @Autowired
  private OrderService orderService;

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  @RequestMapping(method = RequestMethod.GET)
  @ResponseStatus( HttpStatus.OK )
  @ResponseBody public List<Order> getAllOrders() {
    return Arrays.asList(new Order());
  }

}
