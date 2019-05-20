package com.world.first.fx.controller;

import static org.junit.Assert.assertEquals;

import java.util.HashSet;
import java.util.Map;
import java.util.Set;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.powermock.modules.junit4.PowerMockRunner;
import org.springframework.http.ResponseEntity;

import com.world.first.fx.bean.OrderDataBean;
import com.world.first.fx.bean.OrderRequest;

@RunWith(PowerMockRunner.class)
public class OrderSummaryControllerTest {
	
	@InjectMocks
	private OrderSummaryController summaryController;

	@Test
	public void testgetOrderSummaryMatch() {
		
		Set<OrderRequest> orderSet = new HashSet<>();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(1);
		orderRequest.setOrderType("ASK");
		orderRequest.setPrice("1.2100");
		orderSet.add(orderRequest);
		OrderDataBean.getInstance().getOrderDataMap().put("MATCH", orderSet);
		
		ResponseEntity<Map<String,Set<OrderRequest>>> response = 
				summaryController.getOrderSummary("MATCH");
		
		assertEquals(1, response.getBody().get("MATCH").size());
		
	}
	
	@Test
	public void testgetOrderSummaryNotMatch() {
		
		Set<OrderRequest> orderSet = new HashSet<>();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(1);
		orderRequest.setOrderType("ASK");
		orderRequest.setPrice("1.2100");
		OrderRequest orderRequest1 = new OrderRequest();
		orderRequest1.setOrderId(2);
		orderRequest1.setOrderType("BID");
		orderRequest1.setPrice("1.2100");
		
		orderSet.add(orderRequest);
		orderSet.add(orderRequest1);
		
		OrderDataBean.getInstance().getOrderDataMap().put("NOTMATCH", orderSet);
		
		ResponseEntity<Map<String,Set<OrderRequest>>> response = 
				summaryController.getOrderSummary("NOTMATCH");
		
		assertEquals(2, response.getBody().get("NOTMATCH").size());
		
	}
	
	@Test
	public void testgetOrderSummary() {
		
		Set<OrderRequest> orderSetNoMatch = new HashSet<>();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(1);
		orderRequest.setOrderType("ASK");
		orderRequest.setPrice("1.2100");
		OrderRequest orderRequest1 = new OrderRequest();
		orderRequest1.setOrderId(2);
		orderRequest1.setOrderType("BID");
		orderRequest1.setPrice("1.2100");
		
		orderSetNoMatch.add(orderRequest);
		orderSetNoMatch.add(orderRequest1);
		
		OrderDataBean.getInstance().getOrderDataMap().put("NOTMATCH", orderSetNoMatch);
		
		Set<OrderRequest> orderSetMatch = new HashSet<>();
		OrderRequest orderRequest3 = new OrderRequest();
		orderRequest3.setOrderId(3);
		orderRequest3.setOrderType("ASK");
		orderRequest3.setPrice("1.2100");
		orderSetMatch.add(orderRequest3);
		OrderDataBean.getInstance().getOrderDataMap().put("MATCH", orderSetMatch);
		
		ResponseEntity<Map<String,Set<OrderRequest>>> response = 
				summaryController.getOrderSummary("");
		
		assertEquals(2, response.getBody().get("NOTMATCH").size());
		assertEquals(1, response.getBody().get("MATCH").size());
		
	}
	
	@Test
	public void testgetOrderSummaryNotFound() {
		
		Set<OrderRequest> orderSet = new HashSet<>();
		OrderRequest orderRequest = new OrderRequest();
		orderRequest.setOrderId(1);
		orderRequest.setOrderType("ASK");
		orderRequest.setPrice("1.2100");
		OrderRequest orderRequest1 = new OrderRequest();
		orderRequest1.setOrderId(2);
		orderRequest1.setOrderType("BID");
		orderRequest1.setPrice("1.2100");
		
		orderSet.add(orderRequest);
		orderSet.add(orderRequest1);
		
		OrderDataBean.getInstance().getOrderDataMap().put("NOTMATCH", orderSet);
		
		ResponseEntity<Map<String,Set<OrderRequest>>> response = 
				summaryController.getOrderSummary("ERR_REQ");
		
		assertEquals(null, response.getBody().get("ERR_REQ"));
		
	}
}
