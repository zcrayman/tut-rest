package com.yummynoodlebar.rest.domain;

import com.yummynoodlebar.core.events.orders.OrderDetails;
import org.springframework.hateoas.ResourceSupport;
import com.yummynoodlebar.rest.controller.*;

import javax.xml.bind.annotation.XmlRootElement;
import java.io.Serializable;
import java.util.Collections;
import java.util.Date;
import java.util.Map;
import java.util.UUID;

import static org.springframework.hateoas.mvc.ControllerLinkBuilder.linkTo;

//TODOCUMENT This is added so that we can do jaxb serialisation.
//this type of annotation is fine here, as this
//Order implementation is made for integration with things like this.

@XmlRootElement
public class Order extends ResourceSupport implements Serializable {

  private Date dateTimeOfSubmission;

  private Map<String, Integer> items;

  private UUID key;

  public Date getDateTimeOfSubmission() {
    return dateTimeOfSubmission;
  }

  public UUID getKey() {
    return key;
  }

  public Map<String, Integer> getItems() {
    return items;
  }

  public void setItems(Map<String, Integer> items) {
    if (items == null) {
      this.items = Collections.emptyMap();
    } else {
      this.items = Collections.unmodifiableMap(items);
    }
  }

  public void setDateTimeOfSubmission(Date dateTimeOfSubmission) {
    this.dateTimeOfSubmission = dateTimeOfSubmission;
  }

  public void setKey(UUID key) {
    this.key = key;
  }

  public OrderDetails toOrderDetails() {
    OrderDetails details = new OrderDetails();

    details.setOrderItems(items);
    details.setKey(key);
    details.setDateTimeOfSubmission(dateTimeOfSubmission);

    return details;
  }


  public static Order fromOrderDetails(OrderDetails orderDetails) {
    Order order = new Order();

    order.dateTimeOfSubmission = orderDetails.getDateTimeOfSubmission();
    order.key = orderDetails.getKey();
    order.setItems(orderDetails.getOrderItems());

    //TODOCUMENT.  Adding the library, the above extends ResourceSupport and
    //this section is all that is actually needed in our model to add hateoas support.

    //Much of the rest of the framework is helping deal with the blending of domains that happens in many spring apps
    //We have explicitly avoided that.
    order.add(linkTo(OrderQueriesController.class).slash(order.key).withSelfRel());
    order.add(linkTo(OrderQueriesController.class).slash(order.key).slash("status").withRel("Order Status"));
    order.add(linkTo(OrderQueriesController.class).slash(order.key).slash("paymentdetails").withRel("Payment Details"));

    return order;
  }
}
