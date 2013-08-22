package com.yummynoodlebar.core.domain;

import com.yummynoodlebar.core.events.orders.OrderStatusDetails;

import java.util.Date;
import java.util.UUID;

public class OrderStatus {

  private Date statusDate;
  private String status;

  public OrderStatus(final Date date, final String status) {
    this.status = status;
    this.statusDate = date;
  }

  public Date getStatusDate() {
    return statusDate;
  }

  public String getStatus() {
    return status;
  }

  public OrderStatusDetails toStatusDetails() {
    return new OrderStatusDetails(statusDate, status);
  }
}
