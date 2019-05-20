package com.world.first.fx.controller;

import java.util.NoSuchElementException;

import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import com.world.first.fx.bean.CurrencyPrices;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;
import io.swagger.annotations.ApiParam;

@Api
@RestController
@RequestMapping(path = "/dummyplatform/v1/currency")
public class CurrentPriceController {
	
	@ApiOperation(value = "Api Gives the Current Forex Conversion Rate",
			 notes = "Valid input values are GBP and USD only")
	@RequestMapping(path = "/prices/from/{fromCurr}/to/{toCurr}", method = RequestMethod.GET,
			produces = MediaType.TEXT_PLAIN_VALUE)
	public ResponseEntity<String> getCurrenyPrice(
				@ApiParam(name = "fromCurr", allowableValues = "GBP, USD")
				@PathVariable(name = "fromCurr") String fromCurr,
				@ApiParam(name = "toCurr", allowableValues = "GBP, USD")
				@PathVariable(name = "toCurr") String toCurr) {
		
		ResponseEntity<String> responseEntity = null;
				
			try {
				//getting the latest rate from the data system
				String currPrice = 
							CurrencyPrices.getConversionPrice(fromCurr.toUpperCase(),
									toCurr.toUpperCase());
				
				if(!StringUtils.isEmpty(currPrice)) {
					responseEntity = new ResponseEntity<String>(currPrice,HttpStatus.OK);
				}
			}catch (NoSuchElementException e) {
				responseEntity = new ResponseEntity<String>(HttpStatus.NOT_FOUND);
			}
					
				return responseEntity;
	}

}
