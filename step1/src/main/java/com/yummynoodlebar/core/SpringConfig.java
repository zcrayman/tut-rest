package com.yummynoodlebar.core;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.domain.Orders;
import com.yummynoodlebar.core.services.OrderEventHandler;
import com.yummynoodlebar.core.services.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.Collections;
import java.util.UUID;

@Configuration
public class SpringConfig {
  @Bean
  public OrderService orderService() {
    return new OrderEventHandler(new Orders(Collections.<UUID, Order>emptyMap()));
  }
}
