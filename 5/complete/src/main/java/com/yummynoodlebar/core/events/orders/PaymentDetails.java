package com.yummynoodlebar.core.events.orders;

import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

public class PaymentDetails {

  private UUID key;
  private Date dateTimeOfSubmission;



  public PaymentDetails() {
    key = null;
  }

  public PaymentDetails(UUID key) {
    this.key = key;
  }

  public Date getDateTimeOfSubmission() {
    return this.dateTimeOfSubmission;
  }

  public void setDateTimeOfSubmission(Date dateTimeOfSubmission) {
    this.dateTimeOfSubmission = dateTimeOfSubmission;
  }

  public UUID getKey() {
    return key;
  }

  public void setKey(UUID key) {
    this.key = key;
  }
}
