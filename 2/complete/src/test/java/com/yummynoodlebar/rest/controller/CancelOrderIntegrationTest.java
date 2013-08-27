package com.yummynoodlebar.rest.controller;

import com.yummynoodlebar.core.events.orders.DeleteOrderEvent;
import com.yummynoodlebar.core.services.OrderService;
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

import static com.yummynoodlebar.rest.controller.fixture.RestEventFixtures.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.setup.MockMvcBuilders.standaloneSetup;

public class CancelOrderIntegrationTest {

  MockMvc mockMvc;

  @InjectMocks
  OrderCommandsController controller;

  @Mock
  OrderService orderService;

  UUID key = UUID.fromString("f3512d26-72f6-4290-9265-63ad69eccc13");

  @Before
  public void setup() {
    MockitoAnnotations.initMocks(this);

    this.mockMvc = standaloneSetup(controller)
            .setMessageConverters(new MappingJackson2HttpMessageConverter()).build();

  }

  // {!begin thatDeleteOrderUsesHttpOkOnSuccess}
  @Test
  public void thatDeleteOrderUsesHttpOkOnSuccess() throws Exception {

    when(orderService.deleteOrder(any(DeleteOrderEvent.class)))
            .thenReturn(
                    orderDeleted(key));

    this.mockMvc.perform(
            delete("/aggregators/orders/{id}", key.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isOk());

    verify(orderService).deleteOrder(argThat(
            Matchers.<DeleteOrderEvent>hasProperty("key",
                    Matchers.equalTo(key))));
  }
  // {!end thatDeleteOrderUsesHttpOkOnSuccess}

  @Test
  public void thatDeleteOrderUsesHttpNotFoundOnEntityLookupFailure() throws Exception {

    when(orderService.deleteOrder(any(DeleteOrderEvent.class)))
            .thenReturn(
                    orderDeletedNotFound(key));

    this.mockMvc.perform(
            delete("/aggregators/orders/{id}", key.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isNotFound());

  }

  @Test
  public void thatDeleteOrderUsesHttpForbiddenOnEntityDeletionFailure() throws Exception {

    when(orderService.deleteOrder(any(DeleteOrderEvent.class)))
            .thenReturn(
                    orderDeletedFailed(key));

    this.mockMvc.perform(
            delete("/aggregators/orders/{id}", key.toString())
                    .accept(MediaType.APPLICATION_JSON))
            .andDo(print())
            .andExpect(status().isForbidden());
  }
}
