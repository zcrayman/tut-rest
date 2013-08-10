package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.services.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static junit.framework.TestCase.*;

public class OrderControllerTest {

  @Mock
  OrderService orderService;

  @InjectMocks
  OrdersController controller;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  //createOrder
  //getOrder
  //cancelOrder

  //getOrder negative
  //cancelOrder negative
  //createOrder - validation?

  @Test
  public void thatControllerGetsAllOrderDetailsAndConvertsToRestOrders() {

    fail("Implement me!");
  }
}
