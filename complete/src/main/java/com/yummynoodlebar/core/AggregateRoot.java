package com.yummynoodlebar.core;

import com.yummynoodlebar.events.*;
import com.yummynoodlebar.events.UpdatedEvent;

public interface AggregateRoot {

    public CreatedEvent processEvent(CreateEvent createEvent);

    public UpdatedEvent processEvent(UpdatedEvent updatedEvent);

    public DeletedEvent processEvent(DeleteEvent deleteEvent);

    public ReadEvent processEvent(RequestReadEvent requestReadEvent);
}
