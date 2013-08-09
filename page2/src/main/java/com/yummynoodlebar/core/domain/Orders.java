package com.yummynoodlebar.core.domain;

import com.yummynoodlebar.core.events.*;
import com.yummynoodlebar.core.events.orders.AllOrdersEvent;
import com.yummynoodlebar.core.events.orders.DeleteOrderEvent;
import com.yummynoodlebar.core.events.orders.OrderCreatedEvent;
import com.yummynoodlebar.core.events.orders.OrderDeletedEvent;

import java.util.*;

public class Orders implements AggregateRoot {

    private Map<UUID, Order> orders;

    public Orders(final Map<UUID, Order> orders) {
        this.orders = Collections.unmodifiableMap(orders);
    }

    @Override
    public AllOrdersEvent processEvent(final RequestReadEvent requestReadEvent) {
        return new AllOrdersEvent(this.orders);
    }

    @Override
    public synchronized OrderCreatedEvent processEvent(final CreateEvent createEvent) {
        Map<UUID, Order> modifiableOrders = new HashMap<UUID, Order>(orders);
        Order newOrder = new Order(new Date());
        modifiableOrders.put(newOrder.getKey(), newOrder);
        this.orders = Collections.unmodifiableMap(modifiableOrders);
        return new OrderCreatedEvent(newOrder.getKey());
    }

    @Override
    public UpdatedEvent processEvent(UpdatedEvent updatedEvent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OrderDeletedEvent processEvent(DeleteEvent deleteEvent) {
        if (deleteEvent instanceof DeleteOrderEvent) {
            DeleteOrderEvent deleteOrderEvent = (DeleteOrderEvent) deleteEvent;
            Map<UUID, Order> modifiableOrders = new HashMap<UUID, Order>(orders);
            modifiableOrders.remove(deleteOrderEvent.getKey());
            this.orders = Collections.unmodifiableMap(modifiableOrders);
        }
        return new OrderDeletedEvent();
    }
}
