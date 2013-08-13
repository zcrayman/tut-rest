package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.events.orders.RequestOrderDetailsEvent;
import com.yummynoodlebar.core.services.OrderService;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.mockito.Mockito.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;
import static com.yummynoodlebar.rest.controller.fixture.RestDataFixture.*;
import static com.yummynoodlebar.rest.controller.fixture.RestEventFixtures.*;

public class ViewOrderIntegrationTest {

  MockMvc mockMvc;

  @InjectMocks
  OrderQueriesController controller;

  @Mock
  OrderService orderService;

  UUID key = UUID.fromString("f3512d26-72f6-4290-9265-63ad69eccc13");

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = standaloneSetup(controller)
            .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();
  }

  @Test
  public void thatViewOrderUsesHttpNotFound() throws Exception {

    when(orderService.requestOrderDetails(any(RequestOrderDetailsEvent.class))).thenReturn(
            orderDetailsNotFound(key));

    this.mockMvc.perform(
            get("/aggregators/orders/{id}",  key.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());
  }

  @Test
  public void thatViewOrderUsesHttpOK() throws Exception {

    when(orderService.requestOrderDetails(any(RequestOrderDetailsEvent.class))).thenReturn(
            orderDetailsEvent(key));

    this.mockMvc.perform(
            get("/aggregators/orders/{id}", key.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isOk());
  }

  @Test
  public void thatViewOrderRendersCorrectly() throws Exception {

    when(orderService.requestOrderDetails(any(RequestOrderDetailsEvent.class))).thenReturn(
            orderDetailsEvent(key));

    this.mockMvc.perform(
            get("/aggregators/orders/{id}", key.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(jsonPath("$.items['" + YUMMY_ITEM + "']").value(12))
            .andExpect(jsonPath("$.key").value(key.toString()));
  }
}
