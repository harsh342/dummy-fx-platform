package com.world.first.fx.controller;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.world.first.fx.bean.OrderDataBean;
import com.world.first.fx.bean.OrderRequest;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/dummyplatform/v1/order/summary")
public class OrderSummaryController {
	
	@ApiOperation(value = "Api Gives Summary of the Transactions",
			 notes = "Valid input values are MATCH or NOTMATCH. "
			 		+ "If no input is given API will provide both MATCH and NOTMATCH response.")
	
	@RequestMapping(path = {"/","/{requestType}"}, method = RequestMethod.GET,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<Map<String,Set<OrderRequest>>> 
		getOrderSummary(@PathVariable(name = "requestType", required = false) String requestType){
		
		ResponseEntity<Map<String,Set<OrderRequest>>> summaryResponse = null;
		Map<String,Set<OrderRequest>> responseMap = null;
		
		OrderDataBean orderDataBean = OrderDataBean.getInstance();
		
		if(StringUtils.isEmpty(requestType)) {
			responseMap = orderDataBean.getOrderDataMap();
		}else {
			Set<OrderRequest> responseSet = orderDataBean.getOrderDataMap().get(requestType);
			responseMap = new HashMap<>();
			responseMap.put(requestType, responseSet);
		}
		
		if(responseMap!=null && !responseMap.isEmpty()) {
			summaryResponse = new ResponseEntity<>(responseMap,HttpStatus.OK);
		}else {
			summaryResponse = new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}
		
		return summaryResponse;
	}

}
