package com.yummynoodlebar.rest.controller.fixture;

import com.yummynoodlebar.core.events.orders.AllOrdersEvent;
import com.yummynoodlebar.core.events.orders.OrderDetails;
import com.yummynoodlebar.rest.domain.Order;

import java.util.*;

public class OrdersFixture {
  public static AllOrdersEvent allOrders() {
    List<OrderDetails> orders = new ArrayList<OrderDetails>();

    return new AllOrdersEvent(orders);
  }

  public static Order standardOrder() {
    Order order = new Order();

    order.setItems(Collections.singletonMap("yummy1", 12));

    return order;
  }
  public static OrderDetails standardOrderDetails() {
    OrderDetails orderdetails = new OrderDetails();

    orderdetails.setOrderItems(Collections.singletonMap("yummy1", 12));

    return orderdetails;
  }
}
