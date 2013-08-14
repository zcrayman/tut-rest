package com.yummynoodlebar.rest.domain;

import com.yummynoodlebar.core.events.orders.OrderStatusDetails;
import com.yummynoodlebar.rest.controller.OrderQueriesController;
import com.yummynoodlebar.rest.controller.OrderStatusController;
import org.springframework.hateoas.ResourceSupport;

import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlRootElement;
import java.util.Date;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

@XmlRootElement
public class OrderStatus extends ResourceSupport {

  @XmlElement
  private UUID orderId;

  @XmlElement
  private Date statusDate;

  @XmlElement
  private String status;

  public static OrderStatus fromOrderStatusDetails(UUID key, OrderStatusDetails orderDetails) {
    OrderStatus status = new OrderStatus();

    status.orderId = key;
    status.status = orderDetails.getStatus();
    status.statusDate = orderDetails.getStatusDate();

    status.add(linkTo(OrderStatusController.class, key.toString()).withSelfRel());
    status.add(linkTo(OrderQueriesController.class).slash(key).withRel("Order"));

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
