package com.yummynoodlebar.core.domain;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.domain.Orders;
import com.yummynoodlebar.core.events.orders.AllOrdersEvent;
import com.yummynoodlebar.core.events.orders.CreateOrderEvent;
import com.yummynoodlebar.core.events.orders.RequestAllOrdersEvent;
import org.junit.Before;
import org.junit.Test;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class OrdersUnitTest {

    private Orders uut;

    @Before
    public void setupUnitUnderTest() {
        Map<UUID, Order> emptyOrderList = new HashMap<UUID, Order>();
        uut = new Orders(emptyOrderList);
    }

    @Test
    public void addASingleOrderToTheOrders() {

        AllOrdersEvent allOrdersEvent = uut.processEvent(new RequestAllOrdersEvent());

        assertEquals(0, allOrdersEvent.getOrdersDetails().size());

        uut.processEvent(new CreateOrderEvent());

        allOrdersEvent = uut.processEvent(new RequestAllOrdersEvent());

        assertEquals(1, allOrdersEvent.getOrdersDetails().size());
    }
}
