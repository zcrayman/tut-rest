package com.yummynoodlebar.rest.controller.fixture;

import com.yummynoodlebar.core.events.orders.AllOrdersEvent;
import com.yummynoodlebar.core.events.orders.OrderCreatedEvent;
import com.yummynoodlebar.core.events.orders.OrderDeletedEvent;
import com.yummynoodlebar.core.events.orders.OrderDetails;
import com.yummynoodlebar.rest.domain.Order;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.UUID;

public class RestFixture {
  public static final String YUMMY_ITEM = "yummy1";

  public static OrderCreatedEvent orderCreated(UUID key) {
    return new OrderCreatedEvent(key, customKeyOrderDetails(key));
  }
  public static OrderDeletedEvent orderDeleted(UUID key) {
    return new OrderDeletedEvent(key, standardOrderDetails(), true);
  }
  public static OrderDeletedEvent orderDeletedFailed(UUID key) {
    return new OrderDeletedEvent(key, standardOrderDetails(), false);
  }
  public static OrderDeletedEvent orderDeletedNotFound(UUID key) {
    return OrderDeletedEvent.notFound(key);
  }
  public static AllOrdersEvent allOrders() {
    List<OrderDetails> orders = new ArrayList<OrderDetails>();

    orders.add(standardOrderDetails());
    orders.add(standardOrderDetails());
    orders.add(standardOrderDetails());

    return new AllOrdersEvent(orders);
  }

  public static Order standardOrder() {
    Order order = new Order();

    order.setItems(Collections.singletonMap(YUMMY_ITEM, 12));

    return order;
  }

  public static OrderDetails customKeyOrderDetails(UUID key) {
    OrderDetails orderdetails = new OrderDetails(key);

    orderdetails.setOrderItems(Collections.singletonMap(YUMMY_ITEM, 12));

    return orderdetails;
  }
  public static OrderDetails standardOrderDetails() {
    return customKeyOrderDetails(UUID.randomUUID());
  }

  public static String standardOrderJSON() {
    return "{ \"items\": { \"yummy1\": 12, \"yummy15\": 42 } }";
  }
}
