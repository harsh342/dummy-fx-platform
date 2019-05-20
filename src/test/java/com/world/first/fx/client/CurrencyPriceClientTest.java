package com.world.first.fx.client;

import static org.junit.Assert.assertEquals;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.client.RestTemplate;

@RunWith(PowerMockRunner.class)
public class CurrencyPriceClientTest {
	
	@Mock
	private RestTemplate restTemplate;
	
	@InjectMocks
	private CurrencyPriceClient currencyPriceClient;
	
	@Before
	public void init() {
		
		ReflectionTestUtils.setField(currencyPriceClient, 
				"currencyPriceEndPoint",
				"http://localhost/dummyplatform/v1/currency/prices/from/{fromCurr}/to/{toCurr}");
		
	}
	
	@Test
	public void testGetCurrencyConversion() {
		ResponseEntity<String> currResponse = new ResponseEntity<String>("11.11",HttpStatus.OK);
		PowerMockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class), Mockito.anyMap())).thenReturn(currResponse);
		String currRes = currencyPriceClient.getCurrencyConversion("GBP", "USD");
		
		assertEquals("11.11", currRes);
	}
	
	@Test
	public void testGetCurrencyFailConversion() {
		ResponseEntity<String> currResponse = new ResponseEntity<String>("11.00",HttpStatus.NOT_FOUND);
		PowerMockito.when(restTemplate.getForEntity(Mockito.anyString(), Mockito.eq(String.class), Mockito.anyMap())).thenReturn(currResponse);
		String currRes = currencyPriceClient.getCurrencyConversion("GBP", "USD");
		
		assertEquals("", currRes);
	}

}
