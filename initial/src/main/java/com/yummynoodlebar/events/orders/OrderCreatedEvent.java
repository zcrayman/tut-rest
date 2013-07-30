package com.yummynoodlebar.events.orders;

import com.yummynoodlebar.events.CreatedEvent;

import java.util.UUID;

public class OrderCreatedEvent extends CreatedEvent {

    private final UUID newOrderKey;

    public OrderCreatedEvent(final UUID newOrderKey) {
        this.newOrderKey = newOrderKey;
    }

    public UUID getNewOrderKey() {
        return newOrderKey;
    }
}
