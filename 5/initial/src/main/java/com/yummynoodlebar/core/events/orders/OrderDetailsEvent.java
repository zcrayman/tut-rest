package com.yummynoodlebar.core.events.orders;

import com.yummynoodlebar.core.events.ReadEvent;

import java.util.UUID;

public class OrderDetailsEvent extends ReadEvent {
  private UUID key;
  private OrderDetails orderDetails;

  private OrderDetailsEvent(UUID key) {
    this.key = key;
  }

  public OrderDetailsEvent(UUID key, OrderDetails orderDetails) {
    this.key = key;
    this.orderDetails = orderDetails;
  }

  public UUID getKey() {
    return key;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }

  public static OrderDetailsEvent notFound(UUID key) {
    OrderDetailsEvent ev = new OrderDetailsEvent(key);
    ev.entityFound=false;
    return ev;
  }
}
