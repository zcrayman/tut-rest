package com.yummynoodlebar.core.domain.fixtures;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.events.orders.OrderDetails;

import java.util.Collections;
import java.util.Date;

public class OrdersFixtures {

  public static final String YUMMY_ITEM = "yummy_core";

  public static Order standardOrder() {
    Order order = new Order(new Date());

    order.setOrderItems(Collections.singletonMap(YUMMY_ITEM, 12));

    return order;
  }

  /*
   * Twin of the above, to improve readability
   */
  public static OrderDetails standardOrderDetails() {
    return standardOrder().toOrderDetails();
  }

}
