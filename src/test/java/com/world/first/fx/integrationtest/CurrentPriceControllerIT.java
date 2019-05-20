package com.world.first.fx.integrationtest;

import java.util.HashMap;
import java.util.Map;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.context.SpringBootTest.WebEnvironment;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.reactive.server.WebTestClient;

@RunWith(SpringRunner.class)
@SpringBootTest(webEnvironment = WebEnvironment.RANDOM_PORT)
public class CurrentPriceControllerIT {

	@Autowired
	private WebTestClient webClient;

	@Test
	public void testGetCurrenyPriceUSD() {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("fromCurr", "GBP");
		uriVariables.put("toCurr", "USD");
		webClient.get().uri("/dummyplatform/v1/currency/prices/from/{fromCurr}/to/{toCurr}", uriVariables).exchange()
				.expectBody(String.class).isEqualTo("1.2100");
	}
	
	@Test
	public void testGetCurrenyPriceGBP() {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("fromCurr", "USD");
		uriVariables.put("toCurr", "GBP");
		webClient.get().uri("/dummyplatform/v1/currency/prices/from/{fromCurr}/to/{toCurr}", uriVariables).exchange()
				.expectBody(String.class).isEqualTo(".8264");
	}
	
	@Test
	public void testGetCurrenyPriceErr() {
		Map<String, String> uriVariables = new HashMap<>();
		uriVariables.put("fromCurr", "GBP");
		uriVariables.put("toCurr", "INR");
		webClient.get().uri("/dummyplatform/v1/currency/prices/from/{fromCurr}/to/{toCurr}", uriVariables).exchange()
				.expectStatus().is4xxClientError();
	}
}
