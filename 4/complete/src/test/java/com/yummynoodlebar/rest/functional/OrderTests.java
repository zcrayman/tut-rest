package com.yummynoodlebar.rest.functional;

import com.yummynoodlebar.rest.controller.fixture.RestDataFixture;
import com.yummynoodlebar.rest.domain.Order;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class OrderTests {

  // {!begin first}
  @Test
  public void thatOrdersCanBeAddedAndQueried() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    RestTemplate template = new RestTemplate();
  // {!end first}

    // {!begin second}
    HttpEntity<String> requestEntity = new HttpEntity<String>(
        RestDataFixture.standardOrderJSON(),headers);
    // {!end second}

    // {!begin third}
    ResponseEntity<Order> entity = template.postForEntity(
        "http://localhost:8080/aggregators/orders",
        requestEntity, Order.class);
    // {!end third}

    String path = entity.getHeaders().getLocation().getPath();

    assertEquals(HttpStatus.CREATED, entity.getStatusCode());
    assertTrue(path.startsWith("/aggregators/orders/"));
    Order order = entity.getBody();

    System.out.println ("The Order ID is " + order.getKey());
    System.out.println ("The Location is " + entity.getHeaders().getLocation());

    assertEquals(2, order.getItems().size());
  }
}
