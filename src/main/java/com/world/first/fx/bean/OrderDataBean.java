package com.world.first.fx.bean;

import java.util.HashMap;
import java.util.Map;
import java.util.Set;

public class OrderDataBean {
	
	private static volatile OrderDataBean orderDataBean;
	
	private int orderId;
	
	private OrderDataBean() {
		super();
	}

	public static OrderDataBean getInstance() {
		if(orderDataBean == null) {
			synchronized (OrderDataBean.class) {
				if(orderDataBean == null) {
					orderDataBean = new OrderDataBean();
				}
			}
		}
		return orderDataBean;
	}
	
	private Map<String,Set<OrderRequest>> orderDataMap;

	public Map<String, Set<OrderRequest>> getOrderDataMap() {
		
		if(orderDataMap==null) {
			orderDataMap = new HashMap<>();
		}
		return orderDataMap;
	}

	public int getOrderId() {
		return orderId;
	}

	public void setOrderId(int orderId) {
		this.orderId = orderId;
	}	
	
	
}
