package com.yummynoodlebar.rest.domain;

import com.yummynoodlebar.core.events.orders.OrderStatusDetails;

import java.util.Date;
import java.util.UUID;

public class OrderStatus {

  private UUID orderId;

  private Date statusDate;

  private String status;

  public static OrderStatus fromOrderStatusDetails(UUID key, OrderStatusDetails orderDetails) {
    OrderStatus status = new OrderStatus();

    status.orderId = key;
    status.status = orderDetails.getStatus();
    status.statusDate = orderDetails.getStatusDate();

    return status;
  }

  public UUID getOrderId() {
    return orderId;
  }

  public Date getStatusDate() {
    return statusDate;
  }

  public String getStatus() {
    return status;
  }
}
