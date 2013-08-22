package com.yummynoodlebar.core.services;

import com.yummynoodlebar.core.domain.Order;
import com.yummynoodlebar.core.repository.OrdersMemoryRepository;
import com.yummynoodlebar.core.events.orders.*;
import org.junit.Before;
import org.junit.Test;

import java.util.Date;
import java.util.UUID;
import static junit.framework.TestCase.*;

import static junit.framework.TestCase.assertEquals;
import static org.mockito.Mockito.*;

public class OrderEventHandlerUnitTest {

  OrderEventHandler uut;
  OrdersMemoryRepository mockOrdersMemoryRepository;

  @Before
  public void setupUnitUnderTest() {
    mockOrdersMemoryRepository = mock(OrdersMemoryRepository.class);
    uut = new OrderEventHandler(mockOrdersMemoryRepository);
  }

  @Test
  public void addANewOrderToTheSystem() {

    when(mockOrdersMemoryRepository.save(any(Order.class))).thenReturn(new Order(new Date()));

    CreateOrderEvent ev = new CreateOrderEvent(new OrderDetails());

    uut.createOrder(ev);

    verify(mockOrdersMemoryRepository).save(any(Order.class));
    verifyNoMoreInteractions(mockOrdersMemoryRepository);
  }

  @Test
  public void addTwoNewOrdersToTheSystem() {

    when(mockOrdersMemoryRepository.save(any(Order.class))).thenReturn(new Order(new Date()));

    CreateOrderEvent ev = new CreateOrderEvent(new OrderDetails());

    uut.createOrder(ev);
    uut.createOrder(ev);

    verify(mockOrdersMemoryRepository, times(2)).save(any(Order.class));
    verifyNoMoreInteractions(mockOrdersMemoryRepository);
  }

  @Test
  public void removeAnOrderFromTheSystemFailsIfNotPresent() {
    UUID key = UUID.randomUUID();

    when(mockOrdersMemoryRepository.findById(key)).thenReturn(null);


  }

  @Test
  public void removeAnOrderFromTheSystemFailsIfNotPermitted() {
    UUID key = UUID.randomUUID();

    Order order = new Order(new Date()) {
      @Override
      public boolean canBeDeleted() {
        return false;
      }
    };

    when(mockOrdersMemoryRepository.findById(key)).thenReturn(order);

    DeleteOrderEvent ev = new DeleteOrderEvent(key);

    OrderDeletedEvent orderDeletedEvent = uut.deleteOrder(ev);

    verify(mockOrdersMemoryRepository, never()).delete(ev.getKey());

    assertTrue(orderDeletedEvent.isEntityFound());
    assertFalse(orderDeletedEvent.isDeletionCompleted());
    assertEquals(order.getDateTimeOfSubmission(), orderDeletedEvent.getDetails().getDateTimeOfSubmission());
  }

  @Test
  public void removeAnOrderFromTheSystemWorksIfExists() {

    UUID key = UUID.randomUUID();
    Order order = new Order(new Date());

    when(mockOrdersMemoryRepository.findById(key)).thenReturn(order);

    DeleteOrderEvent ev = new DeleteOrderEvent(key);

    OrderDeletedEvent orderDeletedEvent = uut.deleteOrder(ev);

    verify(mockOrdersMemoryRepository).delete(ev.getKey());

    assertTrue(orderDeletedEvent.isEntityFound());
    assertTrue(orderDeletedEvent.isDeletionCompleted());
    assertEquals(order.getDateTimeOfSubmission(), orderDeletedEvent.getDetails().getDateTimeOfSubmission());
  }
}
