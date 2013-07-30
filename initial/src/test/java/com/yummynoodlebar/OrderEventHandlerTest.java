package com.yummynoodlebar;

import com.yummynoodlebar.core.Order;
import com.yummynoodlebar.core.Orders;
import com.yummynoodlebar.events.orders.*;
import com.yummynoodlebar.services.OrderEventHandler;
import org.junit.Before;
import org.junit.Test;

import java.util.*;

import static org.junit.Assert.*;

public class OrderEventHandlerTest {

    private OrderEventHandler uut;

    @Before
    public void setupUnitUnderTest() {
        Map<UUID, Order> emptyOrderList = new HashMap<UUID, Order>();
        Orders ordersAggregateRoot = new Orders(emptyOrderList);
        uut = new OrderEventHandler(ordersAggregateRoot);
    }

    @Test
    public void addANewOrderToTheSystem() {

        AllOrdersEvent allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());

        assertEquals(0, allOrdersEvent.getOrdersDetails().size());

        uut.createOrder(new CreateOrderEvent());

        allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());

        assertEquals(1, allOrdersEvent.getOrdersDetails().size());
    }

    @Test
    public void addTwoNewOrdersToTheSystem() {

        AllOrdersEvent allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());

        assertEquals(0, allOrdersEvent.getOrdersDetails().size());

        uut.createOrder(new CreateOrderEvent());
        uut.createOrder(new CreateOrderEvent());

        allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());

        assertEquals(2, allOrdersEvent.getOrdersDetails().size());
    }

    @Test
    public void removeAnOrderFromTheSystem() {

        AllOrdersEvent allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());

        assertEquals(0, allOrdersEvent.getOrdersDetails().size());

        OrderCreatedEvent orderCreatedEvent = uut.createOrder(new CreateOrderEvent());
        uut.deleteOrder(new DeleteOrderEvent(orderCreatedEvent.getNewOrderKey()));

        allOrdersEvent = uut.requestAllOrders(new RequestAllOrdersEvent());

        assertEquals(0, allOrdersEvent.getOrdersDetails().size());
    }
}
