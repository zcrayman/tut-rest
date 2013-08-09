package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.CoreConfig;
import com.yummynoodlebar.core.events.orders.AllOrdersEvent;
import com.yummynoodlebar.core.events.orders.RequestAllOrdersEvent;
import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.MVCConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.test.context.ContextConfiguration;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.test.context.web.WebAppConfiguration;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import org.springframework.web.context.WebApplicationContext;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.mockito.Mockito.*;

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MVCConfig.class})
//@WebAppConfiguration
public class OrdersControllerIntegrationTest {


  @Mock
  private OrderService orderService;

  @InjectMocks
  OrdersController controller;

  private MockMvc mockMvc;

  @Before
  public void setup() {

    this.mockMvc = standaloneSetup(controller).build();

    when(orderService.requestAllOrders(any(RequestAllOrdersEvent.class))).thenReturn(new AllOrdersEvent(null));
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
