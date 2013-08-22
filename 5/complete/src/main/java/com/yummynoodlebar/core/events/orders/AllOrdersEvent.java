package com.yummynoodlebar.core.events.orders;

import com.yummynoodlebar.core.events.ReadEvent;

import java.util.Collection;
import java.util.Collections;
import java.util.List;

public class AllOrdersEvent extends ReadEvent {

  private final List<OrderDetails> ordersDetails;

  public AllOrdersEvent(List<OrderDetails> orders) {
    this.ordersDetails = Collections.unmodifiableList(orders);
  }

  public Collection<OrderDetails> getOrdersDetails() {
    return this.ordersDetails;
  }
}
