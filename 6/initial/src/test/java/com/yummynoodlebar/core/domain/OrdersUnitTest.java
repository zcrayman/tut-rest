package com.yummynoodlebar.core.domain;

import com.yummynoodlebar.core.domain.fixtures.OrdersFixtures;
import com.yummynoodlebar.core.events.orders.*;
import com.yummynoodlebar.core.repository.OrdersMemoryRepository;
import org.junit.Before;
import org.junit.Test;

import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

import static org.junit.Assert.assertEquals;

public class OrdersUnitTest {

  OrdersMemoryRepository uut;

  @Before
  public void setupUnitUnderTest() {
    Map<UUID, Order> emptyOrderList = new HashMap<UUID, Order>();
    uut = new OrdersMemoryRepository(emptyOrderList);
  }

  @Test
  public void addASingleOrderToTheOrders() {

    assertEquals(0, uut.findAll().size());

    uut.save(OrdersFixtures.standardOrder());

    assertEquals(1, uut.findAll().size());
  }

  @Test
  public void removeASingleOrder() {

    UUID key = UUID.randomUUID();

    uut = new OrdersMemoryRepository(Collections.singletonMap(key, OrdersFixtures.standardOrder()));

    assertEquals(1, uut.findAll().size());

    uut.delete(key);

    assertEquals(0, uut.findAll().size());
  }
}
