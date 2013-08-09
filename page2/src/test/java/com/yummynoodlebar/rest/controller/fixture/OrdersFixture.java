package com.yummynoodlebar.rest.controller.fixture;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.events.orders.AllOrdersEvent;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class OrdersFixture {
  public static AllOrdersEvent allOrders() {
    //TODO, exposing the core domain in the tests is nasty. AllOrdersEvent needs to remove coupling directly.
    Map<UUID, Order> orders = new HashMap<UUID, Order>();

    orders.put(UUID.randomUUID(), new Order(new Date()));

    return new AllOrdersEvent(orders);
  }
}
