package com.example.SpringBootTurialVip.enums;

public enum OrderStatus {

	IN_PROGRESS(1, "In Progress"),
	ORDER_RECEIVED(2, "Order Received"),
	OUT_FOR_DELIVERY(3, "Out for Stock"),
	CANCEL(4,"Cancelled"),
	SUCCESS(5,"Success");

	private Integer id;

	private String name;

	private OrderStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() {
		return id;
	}

	public void setId(Integer id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

}
