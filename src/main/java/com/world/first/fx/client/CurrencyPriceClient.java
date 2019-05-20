package com.world.first.fx.client;

import java.util.HashMap;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;

@Component
@PropertySource("classpath:apiendpoints.properties")
public class CurrencyPriceClient {
	
	@Value("${currency.price.end.point}")
	private String currencyPriceEndPoint;

	@Autowired
	private RestTemplate restTemplate;
	
	/**
	 * The Method calls currency API to get the conversion rate between currencies
	 * @param fromCurr
	 * @param toCurr
	 * @return
	 */
	public String getCurrencyConversion(final String fromCurr, final String toCurr) {
		String currResp = "";
		Map<String,String> uriVariables = new HashMap<>();
		uriVariables.put("fromCurr", fromCurr);
		uriVariables.put("toCurr", toCurr);
		
		ResponseEntity<String> currResponse =
					restTemplate.getForEntity(currencyPriceEndPoint, String.class, uriVariables);
		
		if(HttpStatus.OK == currResponse.getStatusCode()) {
			currResp = currResponse.getBody();
		}
		
		return currResp;
	}
	
}
