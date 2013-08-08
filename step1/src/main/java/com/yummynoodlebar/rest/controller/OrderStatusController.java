package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.domain.OrderStatus;
import com.yummynoodlebar.rest.domain.PaymentDetails;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.ResponseBody;

@Controller
@RequestMapping("/aggregators/order/{id}/status")
public class OrderStatusController {

  private OrderService orderService;

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  @RequestMapping(method = RequestMethod.GET)
  public @ResponseBody OrderStatus getOrderStatus(@PathVariable String id) {
    //TODO obtain the order
    //TODO ensure status mapping is correct

    return null;
  }
}
