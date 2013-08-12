package com.yummynoodlebar.core.events.orders;

import com.yummynoodlebar.core.events.UpdateEvent;

import java.util.UUID;

public class UpdateOrderEvent extends UpdateEvent {

  private UUID key;
  private OrderDetails orderDetails;

  public UpdateOrderEvent(UUID key, OrderDetails orderDetails) {
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
