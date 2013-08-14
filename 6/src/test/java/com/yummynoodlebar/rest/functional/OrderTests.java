package com.yummynoodlebar.rest.functional;

import com.yummynoodlebar.rest.controller.fixture.RestDataFixture;
import com.yummynoodlebar.rest.domain.Order;
import com.yummynoodlebar.rest.domain.OrderStatus;
import org.junit.Test;
import org.springframework.http.*;
import org.springframework.security.crypto.codec.Base64;
import org.springframework.web.client.RestTemplate;

import java.util.Arrays;

import static junit.framework.TestCase.assertEquals;
import static junit.framework.TestCase.assertTrue;

public class OrderTests {

  @Test
  public void thatOrdersCanBeAddedAndQueried() {

    ResponseEntity<Order> entity = createOrder();

    String path = entity.getHeaders().getLocation().getPath();

    assertEquals(HttpStatus.CREATED, entity.getStatusCode());
    assertTrue(path.startsWith("/aggregators/order/"));
    Order order = entity.getBody();

    System.out.println ("The Order ID is " + order.getKey());
    System.out.println ("The Location is " + entity.getHeaders().getLocation());

    assertEquals(2, order.getItems().size());
  }

  @Test
  public void thatOrdersHaveCorrectHateoasLinks() {

    ResponseEntity<Order> entity = createOrder();

    Order order = entity.getBody();

    String orderBase = "/aggregators/order/" + order.getKey();

    assertEquals(entity.getHeaders().getLocation().toString(), order.getLink("self").getHref());
    assertTrue(order.getLink("Order Status").getHref().endsWith(orderBase + "/status"));
  }

  @Test
  public void thatNewOrderHasOrdersStatusCreated() {

    ResponseEntity<Order> entity = createOrder();

    Order order = entity.getBody();

    HttpEntity<String> requestEntity = new HttpEntity<String>(
        RestDataFixture.standardOrderJSON(),getHeaders());

    RestTemplate template = new RestTemplate();

    ResponseEntity<OrderStatus> response = template.exchange(
        order.getLink("Order Status").getHref(),
        HttpMethod.GET,
        requestEntity, OrderStatus.class);

    assertEquals(HttpStatus.OK, response.getStatusCode());
    assertEquals("Order Created", response.getBody().getStatus());
  }

  private ResponseEntity<Order> createOrder() {
    HttpEntity<String> requestEntity = new HttpEntity<String>(
        RestDataFixture.standardOrderJSON(),getHeaders());

    RestTemplate template = new RestTemplate();
    return template.postForEntity(
        "http://localhost:8080/aggregators/order",
        requestEntity, Order.class);
  }

  static HttpHeaders getHeaders() {
    HttpHeaders headers = new HttpHeaders();
    headers.setContentType(MediaType.APPLICATION_JSON);
    headers.setAccept(Arrays.asList(MediaType.APPLICATION_JSON));

    String authorisation = "letsnosh" + ":" + "noshing";
    byte[] encodedAuthorisation = Base64.encode(authorisation.getBytes());
    headers.add("Authorization", "Basic " + new String(encodedAuthorisation));

    return headers;
  }

}


