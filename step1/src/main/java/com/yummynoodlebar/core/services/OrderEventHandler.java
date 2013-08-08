package com.yummynoodlebar.core.services;

import com.yummynoodlebar.core.domain.Orders;
import com.yummynoodlebar.core.events.orders.*;

public class OrderEventHandler implements OrderService {

    private final Orders orders;

    public OrderEventHandler(final Orders orders) {
       this.orders = orders;
    }

    @Override
    public OrderCreatedEvent createOrder(CreateOrderEvent createOrderEvent) {
        return this.orders.processEvent(createOrderEvent);
    }

    @Override
    public AllOrdersEvent requestAllOrders(RequestAllOrdersEvent requestAllCurrentOrdersEvent) {
       return this.orders.processEvent(requestAllCurrentOrdersEvent);
    }

    @Override
    public OrderDetailsEvent requestOrderDetails(RequestOrderDetailsEvent requestOrderDetailsEvent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OrderUpdatedEvent updateOrder(UpdateOrderEvent updateOrderEvent) {
        return null;  //To change body of implemented methods use File | Settings | File Templates.
    }

    @Override
    public OrderDeletedEvent deleteOrder(DeleteOrderEvent deleteOrderEvent) {
        return this.orders.processEvent(deleteOrderEvent);
    }
}
