package com.yummynoodlebar.core.events.orders;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.events.ReadEvent;

import java.util.*;

public class AllOrdersEvent extends ReadEvent {

    private final List<OrderDetails> ordersDetails;

    public AllOrdersEvent(Map<UUID, Order> orders) {
        List<OrderDetails> currentOrdersDetails = new ArrayList<OrderDetails>();
        for (Order order : orders.values()) {
            currentOrdersDetails.add(new OrderDetails(order.getDateTimeOfSubmission()));
        }
        this.ordersDetails = Collections.unmodifiableList(currentOrdersDetails);
    }

    public List<OrderDetails> getOrdersDetails() {
        return this.ordersDetails;
    }
}
