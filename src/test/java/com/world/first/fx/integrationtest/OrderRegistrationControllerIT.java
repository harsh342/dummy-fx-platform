package com.world.first.fx.integrationtest;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

import com.world.first.fx.bean.OrderRegistrationResponse;
import com.world.first.fx.bean.OrderRequest;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class OrderRegistrationControllerIT {

	@Autowired
	private WebTestClient webTestClient;
	
	@Test
	public void testRegisterOrderMatch() {
		
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency("USD");
		order.setOrderType("BID");
		order.setAmount("2000");
		order.setPrice("1.2100");
		
		 String response = webTestClient.post().uri("/dummyplatform/v1/order/registrations").
		syncBody(order).header("Content-Type", "application/json").exchange().expectBody(OrderRegistrationResponse.class)
		.returnResult().getResponseBody().getResponseStatus();
		 
		 assertEquals("2001", response);
		
	}
	
	@Test
	public void testRegisterOrderNoMatch() {
		
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency("USD");
		order.setOrderType("BID");
		order.setAmount("2000");
		order.setPrice("1.200");
		
		String response = webTestClient.post().uri("/dummyplatform/v1/order/registrations").
				syncBody(order).header("Content-Type", "application/json").exchange().expectBody(OrderRegistrationResponse.class)
				.returnResult().getResponseBody().getResponseStatus();
				 
				 assertEquals("2002", response);
		
	}
	
	@Test
	public void testRegisterOrderBadRequest() {
		
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency("USD");
		order.setOrderType("Wrong");
		order.setAmount("2000");
		order.setPrice("1.200");
		
		webTestClient.post().uri("/dummyplatform/v1/order/registrations").
				syncBody(order).header("Content-Type", "application/json").exchange()
				.expectStatus().is4xxClientError();
		
	}
	
	@Test
	public void testCancelOrder() {
		
		webTestClient.post().uri("/dummyplatform/v1/order/cancel").
		syncBody(1).header("Content-Type", "application/json").exchange()
		.expectStatus().isOk();
	}
	
	@Test
	public void testCancelOrderNotFound() {
		
		webTestClient.post().uri("/dummyplatform/v1/order/cancel").
		syncBody(100).header("Content-Type", "application/json").exchange()
		.expectStatus().is4xxClientError();
	}
}
