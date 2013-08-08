package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.domain.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/aggregators/orders")
public class OrdersController {

  private OrderService orderService;

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody List<Order> getAllOrders() {
    return null;
  }

}
