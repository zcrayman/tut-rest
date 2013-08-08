package com.yummynoodlebar.services;

import com.yummynoodlebar.events.orders.*;

public interface OrderService {

    public AllOrdersEvent requestAllOrders(RequestAllOrdersEvent requestAllCurrentOrdersEvent);

    public OrderDetailsEvent requestOrderDetails(RequestOrderDetailsEvent requestOrderDetailsEvent);

    public OrderCreatedEvent createOrder(CreateOrderEvent event);

    public OrderUpdatedEvent updateOrder(UpdateOrderEvent updateOrderEvent);

    public OrderDeletedEvent deleteOrder(DeleteOrderEvent deleteOrderEvent);
}
