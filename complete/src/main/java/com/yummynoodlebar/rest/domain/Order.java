package com.yummynoodlebar.rest.domain;

import java.util.Date;
import java.util.UUID;

public class Order {

  private final Date dateTimeOfSubmission;

  private final UUID key;

  public Order(final Date dateTimeOfSubmission) {
    this.key = UUID.randomUUID();
    this.dateTimeOfSubmission = dateTimeOfSubmission;
  }

  public Date getDateTimeOfSubmission() {
    return dateTimeOfSubmission;
  }

  public UUID getKey() {
    return key;
  }
}
