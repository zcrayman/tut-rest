package com.yummynoodlebar.core.events.orders;

import com.yummynoodlebar.core.domain.Order;
import org.springframework.beans.BeanUtils;

import java.util.Date;

public class OrderDetails {

  private Date dateTimeOfSubmission;

  public OrderDetails(Order order) {
    BeanUtils.copyProperties(order, this);
  }

  public Date getDateTimeOfSubmission() {
    return this.dateTimeOfSubmission;
  }

  void setDateTimeOfSubmission(Date dateTimeOfSubmission) {
    this.dateTimeOfSubmission = dateTimeOfSubmission;
  }
}
