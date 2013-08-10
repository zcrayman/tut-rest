package com.yummynoodlebar.core.domain;

import com.yummynoodlebar.core.events.*;

public interface AggregateRoot {

  public CreatedEvent processEvent(CreateEvent createEvent);

  public UpdatedEvent processEvent(UpdatedEvent updatedEvent);

  public DeletedEvent processEvent(DeleteEvent deleteEvent);

  public ReadEvent processEvent(RequestReadEvent requestReadEvent);
}
