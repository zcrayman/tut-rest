package com.yummynoodlebar.core;

import java.util.Set;

public class Orders {

    private final Set<Order> orders;

    public Orders(final Set<Order> orders) {
        this.orders = orders;
    }

    public void addOrder(final Order order) {
       orders.add(order);
    }

    public void removeOrder(final Order order) {
        orders.remove(order);
    }
}
