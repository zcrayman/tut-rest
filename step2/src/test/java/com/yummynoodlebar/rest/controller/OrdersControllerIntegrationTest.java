package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.SpringConfig;
import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.MVCConfig;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
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

@RunWith(SpringJUnit4ClassRunner.class)
@ContextConfiguration(classes = {MVCConfig.class, SpringConfig.class})
@WebAppConfiguration
public class OrdersControllerIntegrationTest {
  @Autowired
  private WebApplicationContext wac;

  @Autowired
  private OrderService orderService;

  private MockMvc mockMvc;

  @Before
  public void setup() {
    this.mockMvc = webAppContextSetup(this.wac).build();

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
