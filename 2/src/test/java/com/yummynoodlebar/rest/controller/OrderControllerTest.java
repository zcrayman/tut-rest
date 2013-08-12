package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.events.orders.RequestAllOrdersEvent;
import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.controller.fixture.RestDataFixture;
import com.yummynoodlebar.rest.domain.Order;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.List;

import static com.yummynoodlebar.rest.controller.fixture.RestDataFixture.*;
import static junit.framework.Assert.assertEquals;
import static org.mockito.Matchers.*;
import static org.mockito.Mockito.*;

public class OrderControllerTest {

  @Mock
  OrderService orderService;

  @InjectMocks
  OrderController controller;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);
  }

  @Test
  public void thatControllerGetsAllOrderDetailsAndConvertsToRestOrders() {
    when(orderService.requestAllOrders(any(RequestAllOrdersEvent.class))).thenReturn(allOrders());

    List<Order> list = controller.getAllOrders();

    assertEquals(3, list.size());
    assertEquals(12, (int) list.get(0).getItems().get(RestDataFixture.YUMMY_ITEM));
  }
}
