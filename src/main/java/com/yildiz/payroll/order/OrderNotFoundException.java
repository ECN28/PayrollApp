package com.yildiz.payroll.order;

public class OrderNotFoundException extends RuntimeException {
	
	public OrderNotFoundException(Long id) {
		super("Order with Id: "+id+" not found!");
	}
}
