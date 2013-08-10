package com.yummynoodlebar.core.repository;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.events.CreateEvent;
import com.yummynoodlebar.core.events.DeleteEvent;
import com.yummynoodlebar.core.events.RequestReadEvent;
import com.yummynoodlebar.core.events.UpdatedEvent;
import com.yummynoodlebar.core.events.orders.*;

import java.util.List;
import java.util.UUID;

//TODO, make this event based again, with persistence integration events.
public interface OrdersRepository {

  Order save(Order order);

  void delete(UUID key);

  Order findById(UUID key);

  List<Order> findAll();
}
