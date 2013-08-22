package com.yummynoodlebar.core.events.orders;

import com.yummynoodlebar.core.events.UpdateEvent;

import java.util.UUID;

public class SetPaymentEvent extends UpdateEvent {

  private UUID key;
  private OrderDetails orderDetails;

  public SetPaymentEvent(UUID key, OrderDetails orderDetails) {
    this.key = key;
    this.orderDetails = orderDetails;
  }

  public UUID getKey() {
    return key;
  }

  public OrderDetails getOrderDetails() {
    return orderDetails;
  }
}
