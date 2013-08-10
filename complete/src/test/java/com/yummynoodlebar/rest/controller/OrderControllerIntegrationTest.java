package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.events.orders.CreateOrderEvent;
import com.yummynoodlebar.core.events.orders.RequestAllOrdersEvent;
import com.yummynoodlebar.core.services.OrderService;
import com.yummynoodlebar.rest.controller.fixture.RestFixture;
import org.hamcrest.Matchers;
import org.junit.Before;
import org.junit.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.web.servlet.MockMvc;

import java.util.UUID;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.*;
import static org.mockito.Mockito.*;
import static com.yummynoodlebar.rest.controller.fixture.RestFixture.*;

public class OrderControllerIntegrationTest {

  MockMvc mockMvc;

  @InjectMocks
  OrderController controller;

  @Mock
  OrderService orderService;

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = standaloneSetup(controller)
            .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();

    when(orderService.createOrder(any(CreateOrderEvent.class))).thenReturn(
            orderCreated(UUID.fromString("f3512d26-72f6-4290-9265-63ad69eccc13")));
  }

  //createOrder
  //getOrder
  //cancelOrder

  //getOrder negative
  //cancelOrder negative
  //createOrder - validation?

  @Test
  public void thatCreateOrderUsesHttpCreated() throws Exception {

    this.mockMvc.perform(
            post("/aggregators/order")
                    .content(RestFixture.standardOrderJSON())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            //This can be somewhat chatty, but is very useful for understanding the behaviour of the MVC framework
            //especially when running in a non app context environment as this test is.
            .andDo(print())
            .andExpect(status().isCreated());
  }

  @Test
  public void thatCreateOrderRendersAsJson() throws Exception {

    this.mockMvc.perform(
            post("/aggregators/order")
                    .content(RestFixture.standardOrderJSON())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
              .andExpect(jsonPath("$.items['" + RestFixture.YUMMY_ITEM + "']").value(12))
              .andExpect(jsonPath("$.key").value("f3512d26-72f6-4290-9265-63ad69eccc13"));
  }

  @Test
  public void thatCreateOrderPassesLocationHeader() throws Exception {

    this.mockMvc.perform(
            post("/aggregators/order")
                    .content(RestFixture.standardOrderJSON())
                    .contentType(MediaType.APPLICATION_JSON)
                    .accept(MediaType.APPLICATION_JSON))
            .andExpect(header().string("Location", Matchers.endsWith("/aggregators/order/f3512d26-72f6-4290-9265-63ad69eccc13")));
  }
}
