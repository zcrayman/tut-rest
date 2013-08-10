package com.yummynoodlebar.core.services;

import com.yummynoodlebar.core.events.orders.*;

//TODOCUMENT THis is an event driven service.
// Used to interact with the core domain.
//All methods are guaranteed to return something, null will never be returned.
public interface OrderService {

  public AllOrdersEvent requestAllOrders(RequestAllOrdersEvent requestAllCurrentOrdersEvent);

  public OrderDetailsEvent requestOrderDetails(RequestOrderDetailsEvent requestOrderDetailsEvent);

  public OrderStatusEvent requestOrderStatus(RequestOrderStatusEvent requestOrderStatusEvent);

  public OrderCreatedEvent createOrder(CreateOrderEvent event);

  public OrderUpdatedEvent setOrderPayment(SetOrderPaymentEvent setOrderPaymentEvent);

  public OrderDeletedEvent deleteOrder(DeleteOrderEvent deleteOrderEvent);
}
