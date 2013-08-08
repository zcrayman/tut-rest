package com.yummynoodlebar.services;

import com.yummynoodlebar.core.Order;
import com.yummynoodlebar.core.Orders;
import com.yummynoodlebar.events.CreateEvent;
import com.yummynoodlebar.events.RequestReadEvent;
import com.yummynoodlebar.events.orders.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;
import static org.mockito.Matchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

public class OrderEventHandlerUnitTest {

    private OrderEventHandler uut;
    private Orders mockOrders;

    @Before
    public void setupUnitUnderTest() {
        mockOrders = mock(Orders.class);
        uut = new OrderEventHandler(mockOrders);
    }

    @Test
    public void addANewOrderToTheSystem() {

        Order cannedOrder = new Order(new Date());
        Map<UUID, Order> cannedOrders = new HashMap<UUID, Order>();
        cannedOrders.put(cannedOrder.getKey(), cannedOrder);
        when(mockOrders.processEvent(any(RequestReadEvent.class))).thenReturn(new AllOrdersEvent(new HashMap<UUID, Order>())).thenReturn(new AllOrdersEvent(cannedOrders));

        AllOrdersEvent allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());
        assertEquals(0, allOrdersEvent.getOrdersDetails().size());

        uut.createOrder(new CreateOrderEvent());

        allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());
        assertEquals(1, allOrdersEvent.getOrdersDetails().size());
    }

    @Test
    public void addTwoNewOrdersToTheSystem() {

        Order cannedOrder1 = new Order(new Date());
        Order cannedOrder2 = new Order(new Date());
        Map<UUID, Order> cannedOrders = new HashMap<UUID, Order>();
        cannedOrders.put(cannedOrder1.getKey(), cannedOrder1);
        cannedOrders.put(cannedOrder2.getKey(), cannedOrder2);
        when(mockOrders.processEvent(any(RequestReadEvent.class))).thenReturn(new AllOrdersEvent(new HashMap<UUID, Order>())).thenReturn(new AllOrdersEvent(cannedOrders));

        AllOrdersEvent allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());
        assertEquals(0, allOrdersEvent.getOrdersDetails().size());

        uut.createOrder(new CreateOrderEvent());
        uut.createOrder(new CreateOrderEvent());

        allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());
        assertEquals(2, allOrdersEvent.getOrdersDetails().size());
    }


    @Test
    public void removeAnOrderFromTheSystem() {

        when(mockOrders.processEvent(any(RequestReadEvent.class))).thenReturn(new AllOrdersEvent(new HashMap<UUID, Order>())).thenReturn(new AllOrdersEvent(new HashMap<UUID, Order>()));
        when(mockOrders.processEvent(any(CreateEvent.class))).thenReturn(new OrderCreatedEvent(UUID.randomUUID()));

        AllOrdersEvent allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());
        assertEquals(0, allOrdersEvent.getOrdersDetails().size());

        OrderCreatedEvent orderCreatedEvent = uut.createOrder(new CreateOrderEvent());
        uut.deleteOrder(new DeleteOrderEvent(orderCreatedEvent.getNewOrderKey()));

        allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());
        assertEquals(0, allOrdersEvent.getOrdersDetails().size());
    }
}
