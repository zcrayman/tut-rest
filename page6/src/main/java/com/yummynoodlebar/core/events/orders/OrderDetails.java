package com.yummynoodlebar.core.events.orders;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class OrderDetails {

  private UUID key;
  private Date dateTimeOfSubmission;
  private Map<String, Integer> orderItems;

  public OrderDetails() {
    key = null;
  }

  public OrderDetails(UUID key) {
    this.key = key;
  }

  public Date getDateTimeOfSubmission() {
    return this.dateTimeOfSubmission;
  }

  public void setDateTimeOfSubmission(Date dateTimeOfSubmission) {
    this.dateTimeOfSubmission = dateTimeOfSubmission;
  }

  public Map<String, Integer> getOrderItems() {
    return orderItems;
  }

  public void setOrderItems(Map<String, Integer> orderItems) {
    if (orderItems == null) {
      this.orderItems = Collections.emptyMap();
    } else {
      this.orderItems = Collections.unmodifiableMap(orderItems);
    }
  }

  public UUID getKey() {
    return key;
  }

  public void setKey(UUID key) {
    this.key = key;
  }
}
