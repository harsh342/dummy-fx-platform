package com.world.first.fx.controller;

import java.util.HashSet;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.world.first.fx.bean.OrderRequest;
import com.world.first.fx.bean.ResponseStatus;
import com.world.first.fx.bean.OrderDataBean;
import com.world.first.fx.bean.OrderRegistrationResponse;
import com.world.first.fx.client.CurrencyPriceClient;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@Api
@RestController
@RequestMapping(path = "/dummyplatform/v1/order")
public class OrderRegistrationController {
	
	@Autowired
	private CurrencyPriceClient currencyPriceClient;
	
	@ApiOperation(value = "Api Registers the order for BID or Request",
			 notes = "Valid input values for type is BID and ASK only"
			 		+ "and for currency is GBP or USD")
	@RequestMapping(path = "/registrations", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<OrderRegistrationResponse> 
						registerOrder(@RequestBody OrderRequest order){
		
		ResponseEntity<OrderRegistrationResponse> responseEntity = null;
		OrderRegistrationResponse orderRegResp = null;
		
		if(("ASK".equals(order.getOrderType()) || 
						"BID".equals(order.getOrderType()))
							&& ("GBP".equals(order.getCurrency()) || 
										"USD".equals(order.getCurrency()))) {
			
			String fromCurr = ("GBP".equals(order.getCurrency())?"USD":"GBP");
			
			//Calling Currency API to get the current price
			String currRate = 
					currencyPriceClient.getCurrencyConversion(fromCurr, order.getCurrency());
			
			//Matching the current currency price with the customers proposal
			String matchStatus = (order.getPrice()!=null && 
					Double.parseDouble(order.getPrice())==Double.parseDouble(currRate))?"MATCH":"NOTMATCH";
			
			OrderDataBean.getInstance().setOrderId(OrderDataBean.getInstance().getOrderId()+1);
			
			order.setOrderId(OrderDataBean.getInstance().getOrderId());
			
			order.setStatus(matchStatus);	
			
			//Adding the order and the status to data system/ DB
			OrderDataBean.getInstance().getOrderDataMap().
					compute(order.getStatus(),(key,value)->{
						Set<OrderRequest> orderReq = value;
						if(orderReq==null || orderReq.isEmpty()) {
							orderReq = new HashSet<OrderRequest>();
							orderReq.add(order);
						}else {
							orderReq.add(order);
						}
						return orderReq;
					});
			
			
			orderRegResp = new OrderRegistrationResponse();
			orderRegResp.setOrderId(String.valueOf(order.getOrderId()));
			
			if("MATCH".equals(order.getStatus())) {
				orderRegResp.setResponseStatus("2001");
				orderRegResp.setResponseMessage("Order Matched");
			}else {
				orderRegResp.setResponseStatus("2002");
				orderRegResp.setResponseMessage("Order Did Not Match");
			}
			
			responseEntity = new ResponseEntity<>(orderRegResp,HttpStatus.OK);
			
		}else {
			orderRegResp = new OrderRegistrationResponse();
			orderRegResp.setResponseStatus("6001");
			orderRegResp.setResponseMessage("Order Type Can be only ASK or BID");
			responseEntity = new ResponseEntity<>(orderRegResp,HttpStatus.BAD_REQUEST);
		}
		
		return responseEntity;
	}
	
	@ApiOperation(value = "Api is to cancle the submitted order")
	@RequestMapping(path = "/cancel", method = RequestMethod.POST,
			consumes = MediaType.APPLICATION_JSON_UTF8_VALUE,
			produces = MediaType.APPLICATION_JSON_UTF8_VALUE)
	public ResponseEntity<ResponseStatus> 
						cancelOrder(@RequestBody int orderId){
		
		ResponseEntity<ResponseStatus> responseEntity = null;
		ResponseStatus status = new ResponseStatus();
		
		OrderDataBean.getInstance().getOrderDataMap().forEach((key,value)->{
		
			boolean removeCheck = value.removeIf((orderRequest)->orderRequest.getOrderId()==orderId);
			if(removeCheck) {
				status.setResponseStatus("2001");
				status.setResponseMessage("Order Cancelled");
			}
		});
		
		if("2001".equals(status.getResponseStatus())) {
			responseEntity = new ResponseEntity<ResponseStatus>(status,HttpStatus.OK);
		}
		else {
			status.setResponseStatus("2002");
			status.setResponseMessage("Record Not Found");
			responseEntity = new ResponseEntity<ResponseStatus>(status,HttpStatus.NOT_FOUND);
		}
		return responseEntity;
	}
	

}
