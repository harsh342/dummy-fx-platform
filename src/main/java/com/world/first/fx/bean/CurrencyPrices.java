package com.world.first.fx.bean;

import java.util.stream.Stream;

public enum CurrencyPrices {
	
	GBP_TO_USD("GBP","USD","1.2100")
	,USD_TO_GBP("USD","GBP",".8264");
	
	private String price;
	private String fromCurr;
	private String toCurr;

	public String getPrice() {
		return price;
	}
	

	public String getFromCurr() {
		return fromCurr;
	}


	public String getToCurr() {
		return toCurr;
	}


	private CurrencyPrices(String fromCurr, String toCurr, String price) {
		this.price = price;
		this.fromCurr = fromCurr;
		this.toCurr = toCurr;
	}


	public static String getConversionPrice(final String fromCurr, final String toCurr) {
		return Stream.of(CurrencyPrices.values()).filter(
				((curr)-> curr.getToCurr().equals(toCurr)
						&& curr.getFromCurr().equals(fromCurr)))
										.findFirst().get().getPrice();
	}
	
	
}
