package com.world.first.fx.controller;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Set;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.powermock.api.mockito.PowerMockito;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;

import com.world.first.fx.bean.OrderRequest;
import com.world.first.fx.bean.ResponseStatus;
import com.world.first.fx.bean.OrderDataBean;
import com.world.first.fx.bean.OrderRegistrationResponse;
import com.world.first.fx.client.CurrencyPriceClient;

@RunWith(PowerMockRunner.class)
public class OrderRegistrationControllerTest {
	
	@InjectMocks
	private OrderRegistrationController orderController;
	
	@Mock
	private CurrencyPriceClient currencyPriceClient;
	
	@Before
	public void init() {
		
	}
	
	@Test
	public void testRegisterOrderMatch() {
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency("USD");
		order.setOrderType("BID");
		order.setAmount("2000");
		order.setPrice("1.2100");
		
		PowerMockito.when(currencyPriceClient.
				getCurrencyConversion(Mockito.anyString(), Mockito.anyString()))
		.thenReturn("1.2100");
		
		ResponseEntity<OrderRegistrationResponse> response = 
						orderController.registerOrder(order);
		
		assertEquals("2001", response.getBody().getResponseStatus());
	}
	
	@Test
	public void testRegisterOrderNoMatch() {
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency("USD");
		order.setOrderType("BID");
		order.setAmount("2000");
		order.setPrice("1.2100");
		
		PowerMockito.when(currencyPriceClient.
				getCurrencyConversion(Mockito.anyString(), Mockito.anyString()))
		.thenReturn("111");
		
		ResponseEntity<OrderRegistrationResponse> response = 
						orderController.registerOrder(order);
		
		assertEquals("2002", response.getBody().getResponseStatus());
	}
	
	@Test
	public void testRegisterOrderBadReq1() {
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency("USD");
		order.setOrderType("DEMO");
		order.setAmount("2000");
		order.setPrice("1.2100");
		
		PowerMockito.when(currencyPriceClient.
				getCurrencyConversion(Mockito.anyString(), Mockito.anyString()))
		.thenReturn("111");
		
		ResponseEntity<OrderRegistrationResponse> response = 
						orderController.registerOrder(order);
		
		assertEquals("6001", response.getBody().getResponseStatus());
	}
	
	@Test
	public void testRegisterOrderBadReq2() {
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency("DEMO");
		order.setOrderType("ASK");
		order.setAmount("2000");
		order.setPrice("1.2100");
		
		PowerMockito.when(currencyPriceClient.
				getCurrencyConversion(Mockito.anyString(), Mockito.anyString()))
		.thenReturn("111");
		
		ResponseEntity<OrderRegistrationResponse> response = 
						orderController.registerOrder(order);
		
		assertEquals("6001", response.getBody().getResponseStatus());
	}
	
	@Test
	public void testRegisterOrderNullOrder() {
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency(null);
		order.setOrderType("ASK");
		order.setAmount("2000");
		order.setPrice("1.2100");
		
		PowerMockito.when(currencyPriceClient.
				getCurrencyConversion(Mockito.anyString(), Mockito.anyString()))
		.thenReturn("111");
		
		ResponseEntity<OrderRegistrationResponse> response = 
						orderController.registerOrder(order);
		
		assertEquals("6001", response.getBody().getResponseStatus());
	}
	
	@Test
	public void testRegisterOrderNullType() {
		OrderRequest order  = new OrderRequest();
		order.setUserId("testuser");
		order.setCurrency("USD");
		order.setOrderType(null);
		order.setAmount("2000");
		order.setPrice("1.2100");
		
		PowerMockito.when(currencyPriceClient.
				getCurrencyConversion(Mockito.anyString(), Mockito.anyString()))
		.thenReturn("111");
		
		ResponseEntity<OrderRegistrationResponse> response = 
						orderController.registerOrder(order);
		
		assertEquals("6001", response.getBody().getResponseStatus());
	}
	
	@Test
	public void cancelOrderFound() {
		Set<OrderRequest> orderSet = new HashSet<>();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(1);
		orderSet.add(orderRequest);
		OrderDataBean.getInstance().getOrderDataMap().put("MATCH", orderSet);
		ResponseEntity<ResponseStatus> responseStatus = 
				orderController.cancelOrder(1);
		
		assertEquals("2001", responseStatus.getBody().getResponseStatus());
	}
	
	@Test
	public void cancelOrderNotFound() {
		Set<OrderRequest> orderSet = new HashSet<>();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(2);
		orderSet.add(orderRequest);
		OrderDataBean.getInstance().getOrderDataMap().put("MATCH", orderSet);
		ResponseEntity<ResponseStatus> responseStatus = 
				orderController.cancelOrder(10);
		
		assertEquals("2002", responseStatus.getBody().getResponseStatus());
	}

}
