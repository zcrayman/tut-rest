package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.rest.domain.Order;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/aggregators/orders")
public class OrdersController {

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody List<Order> getAllOrders() {
    return null;
  }

}
