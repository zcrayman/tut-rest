package com.yummynoodlebar.core.events.orders;

import java.util.Date;
import java.util.Map;

public class OrderDetails {

  private Date dateTimeOfSubmission;
  private Map<String, Integer> orderItems;

  public Date getDateTimeOfSubmission() {
    return this.dateTimeOfSubmission;
  }

  void setDateTimeOfSubmission(Date dateTimeOfSubmission) {
    this.dateTimeOfSubmission = dateTimeOfSubmission;
  }
}
