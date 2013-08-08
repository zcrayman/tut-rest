package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.services.OrderService;
import org.junit.Before;
import org.junit.Test;

import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;


public class OrdersControllerTest {

  OrderService orderService;
  OrdersController controller;

  @Before
  public void setup() {
    controller = new OrdersController();
    orderService = mock(OrderService.class);
    controller.setSecurityPriceService(mockSecurityPriceService);
  }

  @Test
  void getsAllOrders() {


  }
}
