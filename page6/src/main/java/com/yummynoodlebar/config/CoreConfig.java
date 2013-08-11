package com.yummynoodlebar.config;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.repository.OrdersMemoryRepository;
import com.yummynoodlebar.core.repository.OrdersRepository;
import com.yummynoodlebar.core.services.OrderEventHandler;
import com.yummynoodlebar.core.services.OrderService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.HashMap;
import java.util.UUID;

@Configuration
public class CoreConfig {


  @Bean
  public OrderService createService(OrdersRepository repo) {
    return new OrderEventHandler(repo);
  }

  @Bean
  public OrdersRepository createRepo() {
    return new OrdersMemoryRepository(new HashMap<UUID, Order>());
  }

}
