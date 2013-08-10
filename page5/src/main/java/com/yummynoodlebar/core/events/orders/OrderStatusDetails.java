package com.yummynoodlebar.core.events.orders;

import java.util.Date;

public class OrderStatusDetails {

  private Date statusDate;
  private String status;

  public OrderStatusDetails(Date statusDate, String status) {
    this.status = status;
    this.statusDate = statusDate;
  }

  public Date getStatusDate() {
    return statusDate;
  }

  public String getStatus() {
    return status;
  }
}
