package com.yummynoodlebar.core.events.orders;

import com.yummynoodlebar.core.events.DeletedEvent;

import java.util.UUID;

public class OrderDeletedEvent extends DeletedEvent {
  private UUID key;

  public OrderDeletedEvent(UUID key) {
    this.key = key;
  }

  public UUID getKey() {
    return key;
  }
}
