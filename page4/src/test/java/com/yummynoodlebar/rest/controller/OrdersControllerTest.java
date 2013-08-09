package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.services.OrderService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;

public class OrdersControllerTest {

  OrderService orderService;
  OrdersController controller;

  @Before
  public void setup() {
    controller = new OrdersController();
    orderService = mock(OrderService.class);
    controller.setOrderService(orderService);
  }

  @Test
  void getsAllOrders() {


  }
}
