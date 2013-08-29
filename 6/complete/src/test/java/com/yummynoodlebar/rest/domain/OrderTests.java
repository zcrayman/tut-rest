package com.yummynoodlebar.rest.domain;

import com.yummynoodlebar.core.events.orders.OrderDetails;
import com.yummynoodlebar.rest.controller.fixture.RestDataFixture;
import org.junit.Before;
import org.junit.Test;
import org.springframework.mock.web.MockHttpServletRequest;
import org.springframework.web.context.request.RequestContextHolder;
import org.springframework.web.context.request.ServletRequestAttributes;

import static junit.framework.Assert.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class OrderTests {

  // {!begin supportHateoas}
  @Before
  public void setup() {
    MockHttpServletRequest request = new MockHttpServletRequest();
    RequestContextHolder.setRequestAttributes(new ServletRequestAttributes(request));
  }
  // {!end supportHateoas}

  @Test
  public void thatOrderCanConvertToOrderDetails() {
    Order order = RestDataFixture.standardOrder();

    OrderDetails details = order.toOrderDetails();

    assertEquals(order.getKey(), details.getKey());
    assertEquals(order.getDateTimeOfSubmission(), details.getDateTimeOfSubmission());
    assertEquals(details.getOrderItems().size(), details.getOrderItems().size());
    assertTrue(details.getOrderItems().containsKey(RestDataFixture.YUMMY_ITEM));
    assertEquals(details.getOrderItems().get(RestDataFixture.YUMMY_ITEM), order.getItems().get(RestDataFixture.YUMMY_ITEM));
  }

  @Test
  public void thatOrderCanConvertFromOrderDetails() {
    OrderDetails details = RestDataFixture.standardOrderDetails();

    Order order = Order.fromOrderDetails(details);

    assertEquals(order.getKey(), details.getKey());
    assertEquals(order.getDateTimeOfSubmission(), details.getDateTimeOfSubmission());
    assertEquals(order.getItems().size(), details.getOrderItems().size());
    assertTrue(order.getItems().containsKey(RestDataFixture.YUMMY_ITEM));
    assertEquals(details.getOrderItems().get(RestDataFixture.YUMMY_ITEM), order.getItems().get(RestDataFixture.YUMMY_ITEM));
  }
}
