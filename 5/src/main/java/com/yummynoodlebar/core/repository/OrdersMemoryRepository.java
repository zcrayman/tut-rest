package com.yummynoodlebar.core.repository;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.events.CreateEvent;
import com.yummynoodlebar.core.events.DeleteEvent;
import com.yummynoodlebar.core.events.RequestReadEvent;
import com.yummynoodlebar.core.events.UpdatedEvent;
import com.yummynoodlebar.core.events.orders.*;

import java.util.*;

public class OrdersMemoryRepository implements OrdersRepository {

  private Map<UUID, Order> orders;

  public OrdersMemoryRepository(final Map<UUID, Order> orders) {
    this.orders = Collections.unmodifiableMap(orders);
  }

  @Override
  public synchronized Order save(Order order) {

    Map<UUID, Order> modifiableOrders = new HashMap<UUID, Order>(orders);
    modifiableOrders.put(order.getKey(), order);
    this.orders = Collections.unmodifiableMap(modifiableOrders);

    return order;
  }

  @Override
  public synchronized void delete(UUID key) {
    if (orders.containsKey(key)) {
      Map<UUID, Order> modifiableOrders = new HashMap<UUID, Order>(orders);
      modifiableOrders.remove(key);
      this.orders = Collections.unmodifiableMap(modifiableOrders);
    }
  }

  @Override
  public Order findById(UUID key) {
    return orders.get(key);
  }

  @Override
  public List<Order> findAll() {
    return Collections.unmodifiableList(new ArrayList<Order>(orders.values()));
  }
}
