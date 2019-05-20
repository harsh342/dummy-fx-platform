package com.world.first.fx.controller;

import static org.junit.Assert.assertEquals;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

@RunWith(PowerMockRunner.class)
public class CurrentPriceControllerTest {
	
	@InjectMocks
	private CurrentPriceController currentPriceController;
	
	@Test
	public void testGetCurrenyPriceFound() {
		
		ResponseEntity<String> currResp = 
				currentPriceController.getCurrenyPrice("USD", "GBP");
		
		assertEquals(HttpStatus.OK, currResp.getStatusCode());
	}
	
	@Test
	public void testGetCurrenyPriceNotFound() {
		
		ResponseEntity<String> currResp = 
				currentPriceController.getCurrenyPrice("USD", "INR");
		
		assertEquals(HttpStatus.NOT_FOUND, currResp.getStatusCode());
	}

}
