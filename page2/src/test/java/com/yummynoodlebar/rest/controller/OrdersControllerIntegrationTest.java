package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.events.orders.RequestAllOrdersEvent;
import com.yummynoodlebar.core.services.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;
import static com.yummynoodlebar.rest.controller.fixture.OrdersFixture.*;

public class OrdersControllerIntegrationTest {

  private MockMvc mockMvc;

  @InjectMocks
  private OrdersController controller;

  @Mock
  private OrderService orderService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = standaloneSetup(controller).build();

    when(orderService.requestAllOrders(any(RequestAllOrdersEvent.class))).thenReturn(allOrders());
  }

  @Test
  public void getOrders() throws Exception {
    //TODO, extend this with proper data when we flesh out the order model.

    this.mockMvc.perform(get("/aggregators/orders")
        .accept(MediaType.APPLICATION_JSON))
        .andExpect(status().isOk())
        .andExpect(jsonPath("$[0].items['yumm1']").value(12));
  }




}
