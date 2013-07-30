package com.yummynoodlebar.services;

import com.yummynoodlebar.core.Order;
import com.yummynoodlebar.core.Orders;
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
}
