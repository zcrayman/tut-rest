package com.yummynoodlebar.core.domain;

import com.yummynoodlebar.core.events.CreateEvent;
import com.yummynoodlebar.core.events.DeleteEvent;
import com.yummynoodlebar.core.events.RequestReadEvent;
import com.yummynoodlebar.core.events.UpdatedEvent;
import com.yummynoodlebar.core.events.orders.*;

import java.util.*;

public class Orders implements AggregateRoot {

  private Map<UUID, Order> orders;

  public Orders(final Map<UUID, Order> orders) {
    this.orders = Collections.unmodifiableMap(orders);
  }

  @Override
  public AllOrdersEvent processEvent(final RequestReadEvent requestReadEvent) {
    List<OrderDetails> generatedDetails = new ArrayList<OrderDetails>();
    for (Order order : orders.values()) {
      generatedDetails.add(order.toOrderDetails());
    }
    return new AllOrdersEvent(generatedDetails);
  }

  @Override
  public synchronized OrderCreatedEvent processEvent(final CreateEvent createEvent) {
    if (!(createEvent instanceof CreateOrderEvent)) {
      throw new IllegalArgumentException("Orders can only accept CreateOrderEvent");
    }
    CreateOrderEvent createOrderEvent = (CreateOrderEvent) createEvent;
    Map<UUID, Order> modifiableOrders = new HashMap<UUID, Order>(orders);
    Order newOrder = Order.fromOrderDetails(createOrderEvent.getDetails());
    modifiableOrders.put(newOrder.getKey(), newOrder);
    this.orders = Collections.unmodifiableMap(modifiableOrders);
    return new OrderCreatedEvent(newOrder.getKey(), createOrderEvent.getDetails());
  }

  @Override
  public synchronized UpdatedEvent processEvent(UpdatedEvent updatedEvent) {
    //TODO, is this right?
    throw new IllegalStateException("Orders may not be updated in the current system");
  }

  @Override
  public synchronized OrderDeletedEvent processEvent(DeleteEvent deleteEvent) {
    if (!(deleteEvent instanceof DeleteOrderEvent)) {
      throw new IllegalArgumentException("Orders can only accept DeleteOrderEvent");
    }
    DeleteOrderEvent deleteOrderEvent = (DeleteOrderEvent) deleteEvent;
    Map<UUID, Order> modifiableOrders = new HashMap<UUID, Order>(orders);
    Order order = modifiableOrders.remove(deleteOrderEvent.getKey());
    this.orders = Collections.unmodifiableMap(modifiableOrders);

    OrderDetails details = null;
    if (order != null) {
      details = order.toOrderDetails();
    }

    return new OrderDeletedEvent(deleteOrderEvent.getKey(), details, details != null);
  }

}
