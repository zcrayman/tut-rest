package com.yummynoodlebar.events.orders;

import com.yummynoodlebar.events.DeleteEvent;

import java.util.UUID;

public class DeleteOrderEvent extends DeleteEvent {

    private final UUID key;

    public DeleteOrderEvent(final UUID key) {
        this.key = key;
    }

    public UUID getKey() {
        return key;
    }
}
