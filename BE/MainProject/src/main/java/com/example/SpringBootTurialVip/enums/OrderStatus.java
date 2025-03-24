package com.example.SpringBootTurialVip.enums;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(description = "Danh sách trạng thái đơn hàng")
public enum OrderStatus {

	IN_PROGRESS(1, "In Progress"),
	ORDER_RECEIVED(2, "Order Received"),
	OUT_FOR_DELIVERY(3, "Out for Stock"),
	CANCEL(4, "Cancelled"),
	SUCCESS(5, "Success");

	private final Integer id;
	private final String name;

	OrderStatus(Integer id, String name) {
		this.id = id;
		this.name = name;
	}

	public Integer getId() { return id; }

	public String getName() { return name; }
}
