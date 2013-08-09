package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.events.orders.OrderDetails;
import com.yummynoodlebar.core.events.orders.RequestAllOrdersEvent;
import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.domain.Order;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/aggregators/orders")
class OrdersController {

  static Logger LOG = LoggerFactory.getLogger(OrdersController.class);

  @Autowired
  private OrderService orderService;

  public void setOrderService(OrderService orderService) {
    this.orderService = orderService;
  }

  @RequestMapping(method = RequestMethod.GET)
  @ResponseStatus( HttpStatus.OK )
  @ResponseBody public List<Order> getAllOrders() {
    List<Order> orders = new ArrayList<Order>();
    for (OrderDetails detail : orderService.requestAllOrders(new RequestAllOrdersEvent()).getOrdersDetails()) {
      orders.add(new Order());
    }
    return orders;
  }

}
