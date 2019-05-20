package com.world.first.fx.integrationtest;

import org.junit.Before;
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
public class OrderSummaryControllerIT {
	
	@Autowired
	private WebTestClient webTestClient;
	
	@Before
	public void init() {
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency("USD");
		order.setOrderType("BID");
		order.setAmount("2000");
		order.setPrice("1.2100");
		
		 webTestClient.post().uri("/dummyplatform/v1/order/registrations").
		syncBody(order).header("Content-Type", "application/json").exchange().expectBody(OrderRegistrationResponse.class)
		.returnResult().getResponseBody().getResponseStatus();
	}
	
	@Test
	public void testGetOrderSummaryAll() {
		webTestClient.get().uri("/dummyplatform/v1/order/summary/{requestType}","MATCH")
		.exchange().expectStatus().isOk();
	}
	
	@Test
	public void testGetOrderSummaryNotFound() {
		webTestClient.get().uri("/dummyplatform/v1/order/summary/{requestType}","")
		.exchange().expectStatus().is4xxClientError();
	}

}
